package user_profile_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "User Profile Microservice API",
        version = "v1.2.0",
        description = """
            API responsável pelo gerenciamento dos perfis dos usuários
            da plataforma Reservae.
            
            Permite consultar e atualizar dados complementares do usuário
            autenticado, mantendo a identidade vinculada ao Keycloak.
            """
    )
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(
                new Components()
                    .addSecuritySchemes(
                        "bearerAuth",
                        new SecurityScheme()
                            .name("bearerAuth")
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                    )
            )
            .info(
                new io.swagger.v3.oas.models.info.Info()
                    .title("User Profile Microservice API")
                    .version("v1.2.0")
                    .description("""
                        API responsável pelo gerenciamento dos perfis dos
                        usuários da plataforma Reservae.
                        """)
                    .license(
                        new License()
                            .name("Apache 2.0")
                            .url(
                                "https://github.com/RaphaelMun1z/ReservaeProject"
                            )
                    )
            );
    }
}