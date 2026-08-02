package event_catalog_service.dtos.res;

import event_catalog_service.entities.enums.EventStatusEnum;

import java.time.LocalDateTime;

public record EventSummaryResponseDTO(
    String eventId,
    String title,
    LocalDateTime eventDate,
    EventStatusEnum status,
    String venueName,
    String venueCity,
    String venueState
) {
}