package order_service.proxy.eventCatalog.dto;

import java.time.LocalDateTime;
import java.util.List;

public record EventDetailsResponseDTO(
    String eventId,
    String title,
    LocalDateTime eventDate,
    EventStatusEnum status,
    String venueName,
    String venueCity,
    String venueState,
    List<EventSectorDetailsDTO> sectorsDetails
) {
}
