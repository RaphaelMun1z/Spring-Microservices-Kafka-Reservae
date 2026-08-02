package event_catalog_service.dtos.res;

public record SectorResponseDTO(
    String sectorId,
    String sectorName,
    Integer sectorCapacity
) {
}
