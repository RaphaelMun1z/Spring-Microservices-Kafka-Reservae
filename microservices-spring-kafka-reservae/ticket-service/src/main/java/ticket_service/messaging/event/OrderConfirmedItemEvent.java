package ticket_service.messaging.event;

public record OrderConfirmedItemEvent(
    String orderItemId,
    String sectorId,
    String reservationId,
    String ticketType,
    int quantity
) {
}