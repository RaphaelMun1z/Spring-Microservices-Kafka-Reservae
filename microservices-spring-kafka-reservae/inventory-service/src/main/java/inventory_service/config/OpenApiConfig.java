package inventory_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;

@OpenAPIDefinition(info =
@Info(title = "Inventory Microservice API",
    version = "v0.0.1",
    description = "Camada reativa de alta concorrência encarregada de controlar o estado em tempo real dos ingressos."))
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components())
            .info(new io.swagger.v3.oas.models.info.Info()
                .title("Inventory Microservice API")
                .version("v1.2.0")
                .license(
                    new License()
                        .name("Apache 2.0")
                        .url("https://github.com/RaphaelMun1z/ReservaeProject")
                )
            );
    }
}
