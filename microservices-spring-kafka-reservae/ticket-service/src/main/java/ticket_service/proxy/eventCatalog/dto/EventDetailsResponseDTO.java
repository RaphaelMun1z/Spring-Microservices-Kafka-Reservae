package ticket_service.proxy.eventCatalog.dto;

import java.time.LocalDateTime;
import java.util.List;

public record EventDetailsResponseDTO(
    String eventId,
    String title,
    LocalDateTime eventDate,
    String status,
    String venueName,
    String venueCity,
    String venueState,
    List<EventSectorDetailsDTO> sectorsDetails
) {
}