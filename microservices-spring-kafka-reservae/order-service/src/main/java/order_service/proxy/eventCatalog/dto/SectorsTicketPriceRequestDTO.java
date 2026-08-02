package order_service.proxy.eventCatalog.dto;

import java.util.List;

public record SectorsTicketPriceRequestDTO(
    String eventId,
    List<String> sectorsId
) {
}