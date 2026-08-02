package order_service.proxy.event_catalog.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EventCatalogErrorResponseDTO(
    String localDateTime,
    List<String> message,
    String details
) {
}