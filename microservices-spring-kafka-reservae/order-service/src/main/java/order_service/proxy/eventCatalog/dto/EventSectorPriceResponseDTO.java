package order_service.proxy.eventCatalog.dto;

import java.math.BigDecimal;

public record EventSectorPriceResponseDTO(
    String eventId,
    String sectorId,
    BigDecimal basePrice,
    BigDecimal halfPrice
) {
}
