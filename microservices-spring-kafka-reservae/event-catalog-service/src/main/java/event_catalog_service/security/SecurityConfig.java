package event_catalog_service.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        NimbusJwtDecoder jwtDecoder
    ) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth
                // LIBERAÇÃO TEMPORÁRIA — remover depois
                .requestMatchers("/**").permitAll()

                // Swagger e endpoints básicos do Actuator
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/actuator/health",
                    "/actuator/info"
                )
                .permitAll()

                // Listagem pública de eventos
                .requestMatchers(
                    HttpMethod.GET,
                    "/event-catalog-service/api/events/v1"
                )
                .permitAll()

                // Detalhes públicos de um evento
                .requestMatchers(
                    HttpMethod.GET,
                    "/event-catalog-service/api/events/v1/{id}"
                )
                .permitAll()

                // Consulta pública de preços dos setores
                .requestMatchers(
                    HttpMethod.POST,
                    "/event-catalog-service/api/events/v1/{eventId}/sectors/prices"
                )
                .permitAll()

                // Criação de evento
                .requestMatchers(
                    HttpMethod.POST,
                    "/event-catalog-service/api/events/v1"
                )
                .hasRole("ADMIN")

                // Adição de setor ao evento
                .requestMatchers(
                    HttpMethod.POST,
                    "/event-catalog-service/api/events/v1/{id}/add-sector"
                )
                .hasRole("ADMIN")

                // Remoção de setor do evento
                .requestMatchers(
                    HttpMethod.DELETE,
                    "/event-catalog-service/api/events/v1/{id}/remove-sector/{secId}"
                )
                .hasRole("ADMIN")

                // Qualquer outra rota exige autenticação
                .anyRequest()
                .authenticated()
            )

            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt ->
                    jwt
                        .decoder(jwtDecoder)
                        .jwtAuthenticationConverter(
                            new JWTConverter()
                        )
                )
            )

            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public NimbusJwtDecoder jwtDecoder(
        @Value(
            "${spring.security.oauth2.resourceserver.jwt.issuer-uri}"
        )
        String issuerUri,

        @Value(
            "${reservae.security.required-audience}"
        )
        String requiredAudience
    ) {
        NimbusJwtDecoder decoder =
            JwtDecoders.fromIssuerLocation(issuerUri);

        OAuth2TokenValidator<Jwt> issuerValidator =
            JwtValidators.createDefaultWithIssuer(
                issuerUri
            );

        OAuth2TokenValidator<Jwt> audienceValidator =
            new AudienceValidator(
                requiredAudience
            );

        decoder.setJwtValidator(
            new DelegatingOAuth2TokenValidator<>(
                issuerValidator,
                audienceValidator
            )
        );

        return decoder;
    }
}