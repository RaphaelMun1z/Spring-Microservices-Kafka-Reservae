package event_catalog_service.dtos.req;

import java.util.List;

public record SectorsTicketPriceRequestDTO(
    String eventId,
    List<String> sectorsId
) {
}