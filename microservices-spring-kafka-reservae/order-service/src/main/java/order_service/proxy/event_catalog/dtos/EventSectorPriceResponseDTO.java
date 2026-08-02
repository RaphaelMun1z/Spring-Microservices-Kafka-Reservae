package order_service.proxy.event_catalog.dtos;

import java.math.BigDecimal;

public record EventSectorPriceResponseDTO(
    String eventId,
    String sectorId,
    BigDecimal basePrice,
    BigDecimal halfPrice
) {
}
