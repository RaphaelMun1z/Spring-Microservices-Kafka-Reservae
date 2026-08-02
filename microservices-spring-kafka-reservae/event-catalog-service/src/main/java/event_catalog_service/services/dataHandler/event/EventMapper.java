package event_catalog_service.services.dataHandler.event;

import event_catalog_service.dtos.query.EventDetailsProjection;
import event_catalog_service.dtos.query.EventSummaryProjection;
import event_catalog_service.dtos.res.EventDetailsResponseDTO;
import event_catalog_service.dtos.res.EventSectorDetailsDTO;
import event_catalog_service.dtos.res.EventSummaryResponseDTO;
import event_catalog_service.dtos.res.SectorPricingResponseDTO;
import event_catalog_service.entities.Event;
import event_catalog_service.entities.EventSectorPricing;
import event_catalog_service.entities.Sector;
import event_catalog_service.entities.Venue;
import event_catalog_service.entities.enums.EventStatusEnum;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventMapper {
    public SectorPricingResponseDTO toSectorPricingResponseDTO(
        Sector sector,
        EventSectorPricing esp
    ) {
        return new SectorPricingResponseDTO(
            sector.getId(),
            sector.getName(),
            esp.getBasePrice(),
            esp.getHalfPrice()
        );
    }

    public EventDetailsResponseDTO toEventDetailsResponseDTO(
        Event event,
        Venue venue,
        List<EventSectorDetailsDTO> sectorDetails
    ) {
        return new EventDetailsResponseDTO(
            event.getId(),
            event.getTitle(),
            event.getEventDate(),
            event.getStatus(),
            venue.getName(),
            venue.getCity(),
            venue.getState(),
            sectorDetails
        );
    }

    public EventDetailsResponseDTO toEventDetailsResponseDTO(
        EventDetailsProjection event,
        List<EventSectorDetailsDTO> sectorDetails
    ) {
        return new EventDetailsResponseDTO(
            event.getEventId(),
            event.getTitle(),
            event.getEventDate(),
            event.getStatus(),
            event.getVenueName(),
            event.getVenueCity(),
            event.getVenueState(),
            sectorDetails
        );
    }

    public EventSummaryResponseDTO toEventSummaryResponseDTO(
        EventSummaryProjection projection
    ) {
        return new EventSummaryResponseDTO(
            projection.getEventId(),
            projection.getTitle(),
            projection.getEventDate(),
            EventStatusEnum.valueOf(projection.getStatus()),
            projection.getVenueName(),
            projection.getVenueCity(),
            projection.getVenueState()
        );
    }
}
