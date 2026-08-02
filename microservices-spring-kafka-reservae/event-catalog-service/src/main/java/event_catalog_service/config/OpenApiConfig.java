package event_catalog_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;

@OpenAPIDefinition(info =
@Info(title = "Event Catalog Microservice API",
    version = "v0.0.1",
    description = "Responsável pelo ciclo de vida dos eventos esportivos, times e estádios. Serve como a origem dos dados estruturais."))
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components())
            .info(new io.swagger.v3.oas.models.info.Info()
                .title("Event Catalog Microservice API")
                .version("v1.2.0")
                .license(
                    new License()
                        .name("Apache 2.0")
                        .url("https://github.com/RaphaelMun1z/ReservaeProject")
                )
            );
    }
}
