package inventory_service.proxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ErrorResponseDTO(
    String localDateTime,
    List<String> message,
    String details
) {
}