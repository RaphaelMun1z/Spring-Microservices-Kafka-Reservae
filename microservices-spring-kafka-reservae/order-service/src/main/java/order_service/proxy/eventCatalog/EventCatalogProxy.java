package order_service.proxy.eventCatalog;

import order_service.proxy.eventCatalog.config.EventCatalogFeignConfig;
import order_service.proxy.eventCatalog.dto.EventDetailsResponseDTO;
import order_service.proxy.eventCatalog.dto.EventSectorPriceResponseDTO;
import order_service.proxy.eventCatalog.dto.SectorsTicketPriceRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
    name = "event-catalog-service",
    configuration = EventCatalogFeignConfig.class
)
public interface EventCatalogProxy {
    @GetMapping("/event-catalog-service/api/events/v1/{id}")
    EventDetailsResponseDTO findEventById(@PathVariable String id);

    @PostMapping("/event-catalog-service/api/events/v1/tickets/price")
    List<EventSectorPriceResponseDTO> consultTicketPrices(
        @RequestBody SectorsTicketPriceRequestDTO request
    );
}
