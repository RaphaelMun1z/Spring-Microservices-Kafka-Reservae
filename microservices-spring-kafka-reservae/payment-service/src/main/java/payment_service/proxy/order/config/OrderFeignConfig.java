package payment_service.proxy.order.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class OrderFeignConfig {
    @Bean
    public ErrorDecoder orderErrorDecoder(ObjectMapper objectMapper) {
        return new OrderFeignErrorDecoder(objectMapper);
    }
}