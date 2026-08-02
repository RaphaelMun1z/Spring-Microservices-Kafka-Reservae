package order_service.proxy.event_catalog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class EventCatalogFeignConfig {
    @Bean
    public ErrorDecoder eventCatalogErrorDecoder(ObjectMapper objectMapper) {
        return new EventCatalogFeignErrorDecoder(objectMapper);
    }
}