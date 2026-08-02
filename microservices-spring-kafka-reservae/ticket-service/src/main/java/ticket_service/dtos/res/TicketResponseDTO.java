package ticket_service.dtos.res;

import ticket_service.entities.enums.TicketStatusEnum;

import java.time.LocalDateTime;

public record TicketResponseDTO(
    String ticketId,

    String orderId,
    String eventId,
    String eventTitle,
    LocalDateTime eventDate,

    String venueName,
    String venueCity,
    String venueState,

    String userId,

    String sectorId,
    String sectorName,

    String reservationId,
    String ticketType,

    String qrCodeHash,
    TicketStatusEnum status,

    LocalDateTime createdAt,
    LocalDateTime usedAt
) {
}