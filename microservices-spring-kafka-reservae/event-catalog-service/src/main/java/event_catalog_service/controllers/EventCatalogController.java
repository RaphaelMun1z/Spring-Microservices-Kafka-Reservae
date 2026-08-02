package event_catalog_service.controllers;

import event_catalog_service.controllers.contracts.EventCatalogContract;
import event_catalog_service.dtos.req.CreateEventRequestDTO;
import event_catalog_service.dtos.req.EventFilterRequestDTO;
import event_catalog_service.dtos.req.SectorPricingRequestDTO;
import event_catalog_service.dtos.req.SectorsTicketPriceRequestDTO;
import event_catalog_service.dtos.res.EventDetailsResponseDTO;
import event_catalog_service.dtos.res.EventSectorPriceResponseDTO;
import event_catalog_service.dtos.res.EventSummaryResponseDTO;
import event_catalog_service.dtos.res.SectorPricingResponseDTO;
import event_catalog_service.services.EventCatalogService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event-catalog-service/api/events")
public class EventCatalogController implements EventCatalogContract {
    private final EventCatalogService eventCatalogService;

    public EventCatalogController(EventCatalogService eventCatalogService) {
        this.eventCatalogService = eventCatalogService;
    }

    @Override
    @GetMapping("/v1")
    public ResponseEntity<Page<EventSummaryResponseDTO>> findEvents(
        @ParameterObject @ModelAttribute EventFilterRequestDTO filter,
        @ParameterObject @PageableDefault(size = 12) Pageable pageable
    ) {
        return ResponseEntity.ok(
            eventCatalogService.findEventsFiltered(
                filter,
                pageable
            )
        );
    }

    @Override
    @GetMapping("/v1/{id}")
    public ResponseEntity<EventDetailsResponseDTO> findEventById(@PathVariable String id) {
        return ResponseEntity.ok().body(eventCatalogService.findEventById(id));
    }

    @Override
    @PostMapping("/v1")
    public ResponseEntity<EventDetailsResponseDTO> createEvent(@Valid @RequestBody CreateEventRequestDTO dto) {
        return ResponseEntity.ok().body(eventCatalogService.registerEvent(dto));
    }

    @Override
    @PostMapping("/v1/{id}/add-sector")
    public ResponseEntity<SectorPricingResponseDTO> addSectorToAnEvent(
        @PathVariable String id,
        @Valid @RequestBody SectorPricingRequestDTO dto
    ) {
        return ResponseEntity.ok().body(eventCatalogService.addSectorToAnEvent(
            id,
            dto
        ));
    }

    @Override
    @DeleteMapping("/v1/{id}/remove-sector/{secId}")
    public ResponseEntity<Void> removeSectorFromAnEvent(
        @PathVariable String id,
        @PathVariable String secId
    ) {
        eventCatalogService.removeSectorFromAnEvent(
            id,
            secId
        );
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/v1/tickets/price")
    public ResponseEntity<List<EventSectorPriceResponseDTO>> consultTicketsPrice(
        @Valid @RequestBody SectorsTicketPriceRequestDTO sectorsTicketPriceRequestDTO
    ) {
        return ResponseEntity.ok(eventCatalogService.consultTicketsPrice(sectorsTicketPriceRequestDTO));
    }
}