package payment_service.proxy.order.dtos;

import java.math.BigDecimal;

public record OrderItemResponseDTO(
    String orderItemId,
    String sectorId,
    String reservationId,
    TicketType ticketType,
    int quantity,
    BigDecimal appliedPrice,
    BigDecimal subtotal
) {
}
