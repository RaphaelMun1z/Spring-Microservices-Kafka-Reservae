package order_service.controllers.contracts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import order_service.dtos.req.CheckoutRequestDTO;
import order_service.dtos.req.UpdateOrderStatusRequestDTO;
import order_service.dtos.res.OrderResponseDTO;
import order_service.dtos.res.OrderSummaryResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(
    name = "Order Endpoint",
    description = "Gerenciamento do ciclo de vida de pedidos, checkout, pagamento e acompanhamento de status"
)
public interface OrderContract {

    @Operation(
        summary = "Criar um novo pedido",
        description = "Cria o pedido e envia uma solicitação assíncrona para reservar ingressos no Inventory Service."
    )
    ResponseEntity<OrderSummaryResponseDTO> checkout(
        CheckoutRequestDTO dto
    );

    @Operation(summary = "Buscar os detalhes completos de um pedido pelo ID")
    ResponseEntity<OrderResponseDTO> findOrderById(
        @Parameter(
            description = "Identificador único do pedido",
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        String orderId
    );

    @Operation(summary = "Listar todos os pedidos associados a um evento")
    ResponseEntity<List<OrderSummaryResponseDTO>> findOrdersByEventId(
        @Parameter(
            description = "Identificador único do evento",
            example = "550e8400-e29b-41d4-a716-446655440001"
        )
        String eventId
    );

    @Operation(summary = "Buscar um pedido pelo ID da reserva vinculada")
    ResponseEntity<OrderSummaryResponseDTO> findOrderByReservationId(
        @Parameter(
            description = "Identificador da reserva retornada pelo Inventory Service",
            example = "550e8400-e29b-41d4-a716-446655440010"
        )
        String reservationId
    );

    @Operation(summary = "Atualizar o status de um pedido")
    ResponseEntity<OrderSummaryResponseDTO> updateOrderStatus(
        @Parameter(
            description = "Identificador único do pedido que terá o status atualizado",
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        String orderId,

        UpdateOrderStatusRequestDTO request
    );

    @Operation(summary = "Listar todos os pedidos de um usuário")
    ResponseEntity<List<OrderResponseDTO>> findOrdersByUserId(
        @Parameter(
            description = "Identificador único do usuário comprador",
            example = "550e8400-e29b-41d4-a716-446655440002"
        )
        String userId
    );
}