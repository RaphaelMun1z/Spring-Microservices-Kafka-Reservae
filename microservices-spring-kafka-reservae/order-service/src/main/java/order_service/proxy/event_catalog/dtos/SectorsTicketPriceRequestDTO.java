package order_service.proxy.event_catalog.dtos;

import java.util.List;

public record SectorsTicketPriceRequestDTO(
    String eventId,
    List<String> sectorsId
) {
}