package ticket_service.proxy.eventCatalog.dto;

import java.math.BigDecimal;

public record EventSectorDetailsDTO(
    String sectorId,
    String sectorName,
    BigDecimal basePrice,
    BigDecimal halfPrice
) {
}