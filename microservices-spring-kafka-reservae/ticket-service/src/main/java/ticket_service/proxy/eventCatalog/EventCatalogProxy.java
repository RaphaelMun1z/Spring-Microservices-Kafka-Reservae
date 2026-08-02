package ticket_service.proxy.eventCatalog;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ticket_service.proxy.eventCatalog.dto.EventDetailsResponseDTO;

@FeignClient(name = "event-catalog-service")
public interface EventCatalogProxy {

    @GetMapping("/event-catalog-service/api/events/v1/{eventId}")
    EventDetailsResponseDTO findEventById(@PathVariable String eventId);
}