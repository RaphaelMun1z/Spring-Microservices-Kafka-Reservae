package payment_service.proxy.order.dtos;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponseDTO(
    String orderId,
    String userId,
    BigDecimal totalAmount,
    OrderStatusEnum status,
    String paymentUrl,
    List<OrderItemResponseDTO> itens
) {
}
