package ticket_service.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ticket_service.exceptions.models.BusinessException;
import ticket_service.exceptions.models.NotFoundException;

import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Configuration
public class FeignConfig {
    @Bean
    public ErrorDecoder errorDecoder(ObjectMapper objectMapper) {
        return new CustomErrorDecoder(objectMapper);
    }

    public static class CustomErrorDecoder implements ErrorDecoder {
        private final ErrorDecoder defaultErrorDecoder = new Default();
        private final ObjectMapper objectMapper;

        public CustomErrorDecoder(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public Exception decode(String methodKey, Response response) {
            String originalMessage = "Erro na comunicação com o serviço externo.";

            // 1. Extrai o corpo da resposta original enviada pelo outro microsserviço
            if (response.body() != null) {
                try (Reader reader = response.body().asReader(StandardCharsets.UTF_8)) {
                    String bodyString = Util.toString(reader);
                    JsonNode jsonNode = objectMapper.readTree(bodyString);

                    // 2. Busca o campo "message" no JSON original
                    if (jsonNode.has("message")) {
                        JsonNode messageNode = jsonNode.get("message");
                        // Trata caso a mensagem seja uma lista (array) ou string simples
                        originalMessage = messageNode.isArray() ? messageNode.get(0).asText() : messageNode.asText();
                    } else {
                        originalMessage = bodyString; // Fallback se não achar "message"
                    }
                } catch (Exception e) {
                    // Se falhar no parse, mantém a mensagem padrão
                }
            }

            // 3. Lança a exceção com a mensagem EXATA que veio do outro serviço
            return switch (response.status()) {
                case 404 -> new NotFoundException(originalMessage);
                case 400 -> new BusinessException(originalMessage);
                default -> defaultErrorDecoder.decode(
                    methodKey,
                    response
                );
            };
        }
    }
}
