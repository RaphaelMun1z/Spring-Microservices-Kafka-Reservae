package order_service.proxy.eventCatalog.dto;

import java.math.BigDecimal;

public record EventSectorDetailsDTO(
    String eventId,
    String sectorId,
    String sectorName,
    BigDecimal sectorBasePrice,
    BigDecimal sectorHalfPrice,
    Integer totalCapacity
) {
}
