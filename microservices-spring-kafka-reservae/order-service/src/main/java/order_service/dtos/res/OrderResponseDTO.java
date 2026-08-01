package order_service.dtos.res;

import order_service.entities.enums.OrderStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
    String orderId,
    String userId,

    String eventId,
    String eventTitle,
    LocalDateTime eventDate,
    String venueName,
    String venueCity,
    String venueState,

    LocalDateTime createdAt,

    BigDecimal totalAmount,
    OrderStatusEnum status,
    String paymentUrl,

    List<OrderItemResponseDTO> itens
) {
}
