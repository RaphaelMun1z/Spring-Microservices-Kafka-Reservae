package payment_service.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/actuator/health",
                    "/actuator/info"
                )
                .permitAll()

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