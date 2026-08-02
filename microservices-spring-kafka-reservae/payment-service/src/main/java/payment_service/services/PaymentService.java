package payment_service.services;

import org.springframework.stereotype.Service;
import payment_service.dtos.req.ProductRequestDTO;
import payment_service.dtos.res.PaymentSessionResponseDTO;
import payment_service.exceptions.models.BusinessException;
import payment_service.gateways.PaymentGateway;
import payment_service.gateways.model.PaymentSessionItemRequest;
import payment_service.gateways.model.PaymentSessionRequest;
import payment_service.messaging.event.PaymentFailedEvent;
import payment_service.messaging.event.PaymentRequestedEvent;
import payment_service.messaging.event.PaymentRequestedItemEvent;
import payment_service.messaging.event.PaymentSessionCreatedEvent;
import payment_service.messaging.mapper.PaymentEventMapper;
import payment_service.messaging.publisher.PaymentFailedPublisher;
import payment_service.messaging.publisher.PaymentSessionCreatedPublisher;
import payment_service.proxy.order.OrderProxy;
import payment_service.proxy.order.dtos.OrderResponseDTO;
import payment_service.proxy.order.dtos.OrderStatusEnum;

import java.time.Instant;
import java.util.List;

@Service
public class PaymentService {
    private final PaymentGateway paymentGateway;
    private final PaymentEventMapper paymentEventMapper;
    private final PaymentSessionCreatedPublisher paymentSessionCreatedPublisher;
    private final PaymentFailedPublisher paymentFailedPublisher;
    private final OrderProxy orderProxy;

    public PaymentService(
        PaymentGateway paymentGateway,
        PaymentEventMapper paymentEventMapper,
        PaymentSessionCreatedPublisher paymentSessionCreatedPublisher,
        PaymentFailedPublisher paymentFailedPublisher,
        OrderProxy orderProxy
    ) {
        this.paymentGateway = paymentGateway;
        this.paymentEventMapper = paymentEventMapper;
        this.paymentSessionCreatedPublisher = paymentSessionCreatedPublisher;
        this.paymentFailedPublisher = paymentFailedPublisher;
        this.orderProxy = orderProxy;
    }

    public PaymentSessionResponseDTO createPaymentSession(ProductRequestDTO request) {
        validateRequest(request);

        // Validar se pedido existe
        OrderResponseDTO orderFound = orderProxy.validateAndGetOrder(request.orderId());
        if (orderFound.status() != OrderStatusEnum.AWAITING_PAYMENT) {
            throw new BusinessException("Não é possível criar uma sessão de pagamento para um pedido que não esteja aguardando pagamento.");
        }

        // Validar se pedido é do usuário
        if (!orderFound.userId().equals(request.userId())) {
            throw new BusinessException("O pedido não pertence ao usuário informado.");
        }

        List<PaymentSessionItemRequest> orderItems = orderFound.itens().stream().map(
            item -> new PaymentSessionItemRequest(
                "Ingresso",
                item.appliedPrice()
                    .movePointRight(2)
                    .longValueExact(),
                (long) item.quantity()
            )).toList();

        PaymentSessionRequest paymentSessionRequest = new PaymentSessionRequest(
            request.orderId(),
            request.userId(),
            "BRL",
            "emaildocliente@gmail.com",
            orderItems
        );

        return paymentGateway.createPaymentSession(paymentSessionRequest);
    }

    public void processPaymentRequest(PaymentRequestedEvent event) {
        validateEvent(event);

        try {
            PaymentSessionRequest request =
                paymentEventMapper.toPaymentSessionRequest(event);

            PaymentSessionResponseDTO response =
                paymentGateway.createPaymentSession(request);

            PaymentSessionCreatedEvent sessionCreatedEvent =
                paymentEventMapper.toPaymentSessionCreatedEvent(response);

            paymentSessionCreatedPublisher.publish(sessionCreatedEvent);
        } catch (RuntimeException exception) {
            paymentFailedPublisher.publish(
                new PaymentFailedEvent(
                    event.orderId(),
                    null,
                    "Falha ao criar a sessão de pagamento.",
                    Instant.now()
                )
            );

            throw exception;
        }
    }

    private void validateRequest(ProductRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException(
                "Os dados do pagamento são obrigatórios."
            );
        }

        if (request.orderId() == null || request.orderId().isBlank()) {
            throw new IllegalArgumentException(
                "O identificador do pedido é obrigatório."
            );
        }

        if (request.userId() == null || request.userId().isBlank()) {
            throw new IllegalArgumentException(
                "O identificador do usuário é obrigatório."
            );
        }
    }

    private void validateEvent(PaymentRequestedEvent event) {
        if (event == null) {
            throw new IllegalArgumentException(
                "O evento de solicitação de pagamento é obrigatório."
            );
        }

        if (event.orderId() == null || event.orderId().isBlank()) {
            throw new IllegalArgumentException(
                "O identificador do pedido é obrigatório."
            );
        }

        if (event.userId() == null || event.userId().isBlank()) {
            throw new IllegalArgumentException(
                "O identificador do usuário é obrigatório."
            );
        }

        if (event.currency() == null || event.currency().isBlank()) {
            throw new IllegalArgumentException(
                "A moeda é obrigatória."
            );
        }

        if (event.items() == null || event.items().isEmpty()) {
            throw new IllegalArgumentException(
                "A solicitação de pagamento deve possuir ao menos um item."
            );
        }

        for (PaymentRequestedItemEvent item : event.items()) {
            if (item.name() == null || item.name().isBlank()) {
                throw new IllegalArgumentException(
                    "O nome do item de pagamento é obrigatório."
                );
            }

            if (item.unitAmount() == null || item.unitAmount() <= 0) {
                throw new IllegalArgumentException(
                    "O valor unitário do item deve ser maior que zero."
                );
            }

            if (item.quantity() == null || item.quantity() <= 0) {
                throw new IllegalArgumentException(
                    "A quantidade do item deve ser maior que zero."
                );
            }
        }
    }
}