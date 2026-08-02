package order_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;

@OpenAPIDefinition(info =
@Info(title = "Order Microservice API",
    version = "v0.0.1",
    description = "Orquestra a criação do carrinho de compras e gerencia o estado transacional dos pedidos."))
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components())
            .info(new io.swagger.v3.oas.models.info.Info()
                .title("Order Microservice API")
                .version("v1.2.0")
                .license(
                    new License()
                        .name("Apache 2.0")
                        .url("https://github.com/RaphaelMun1z/ReservaeProject")
                )
            );
    }
}
