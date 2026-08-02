package order_service.proxy.event_catalog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import order_service.exceptions.models.BusinessException;
import order_service.exceptions.models.ExternalServiceException;
import order_service.exceptions.models.NotFoundException;
import order_service.proxy.event_catalog.dtos.EventCatalogErrorResponseDTO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EventCatalogFeignErrorDecoder implements ErrorDecoder {
    private final ObjectMapper objectMapper;
    private final ErrorDecoder defaultErrorDecoder = new Default();

    public EventCatalogFeignErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        String message = extractMessage(response);

        return switch (response.status()) {
            case 400, 409, 422 -> new BusinessException(message);
            case 404 -> new NotFoundException(message);
            case 500, 502, 503, 504 -> new ExternalServiceException(
                "O serviço de catálogo de eventos está indisponível no momento."
            );
            default -> defaultErrorDecoder.decode(methodKey, response);
        };
    }

    private String extractMessage(Response response) {
        if (response.body() == null) {
            return "O serviço de catálogo de eventos recusou a operação.";
        }

        try {
            String body = Util.toString(response.body().asReader(StandardCharsets.UTF_8));

            if (body == null || body.isBlank()) {
                return "O serviço de catálogo de eventos recusou a operação.";
            }

            EventCatalogErrorResponseDTO errorResponse = objectMapper.readValue(
                body,
                EventCatalogErrorResponseDTO.class
            );

            List<String> messages = errorResponse.message();

            if (messages == null || messages.isEmpty()) {
                return "O serviço de catálogo de eventos recusou a operação.";
            }

            return String.join(" ", messages);
        } catch (IOException ex) {
            return "O serviço de catálogo de eventos recusou a operação.";
        }
    }
}