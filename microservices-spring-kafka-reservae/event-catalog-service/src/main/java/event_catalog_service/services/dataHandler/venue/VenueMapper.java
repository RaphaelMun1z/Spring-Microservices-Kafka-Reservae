package event_catalog_service.services.dataHandler.venue;

import event_catalog_service.dtos.res.SectorResponseDTO;
import event_catalog_service.dtos.res.VenueResponseDTO;
import event_catalog_service.entities.Sector;
import event_catalog_service.entities.Venue;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VenueMapper {
    public SectorResponseDTO toSectorResponseDTO(Sector sector) {
        return new SectorResponseDTO(
            sector.getId(),
            sector.getName(),
            sector.getCapacity()
        );
    }

    public List<SectorResponseDTO> toSectorResponseDTOList(List<Sector> sectors) {
        return sectors.stream()
            .map(this::toSectorResponseDTO)
            .toList();
    }

    public VenueResponseDTO toVenueResponseDTO(Venue venue) {
        return new VenueResponseDTO(
            venue.getId(),
            venue.getName(),
            venue.getCity(),
            venue.getState(),
            venue.getTotalCapacity(),
            toSectorResponseDTOList(venue.getSectors())
        );
    }

    public List<VenueResponseDTO> toVenueResponseDTOList(List<Venue> venues) {
        return venues.stream()
            .map(this::toVenueResponseDTO)
            .toList();
    }
}
