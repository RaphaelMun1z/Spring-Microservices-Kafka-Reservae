package order_service.proxy.eventCatalog.dto;

import java.math.BigDecimal;

public record SectorPricingResponseDTO(
    String sectorId,
    String sectorName,
    BigDecimal basePrice,
    BigDecimal halfPrice
) {
}
