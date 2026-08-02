package event_catalog_service.dtos.res;

import java.math.BigDecimal;

public record EventSectorPriceResponseDTO(
    String eventId,
    String sectorId,
    BigDecimal basePrice,
    BigDecimal halfPrice
) {
}
