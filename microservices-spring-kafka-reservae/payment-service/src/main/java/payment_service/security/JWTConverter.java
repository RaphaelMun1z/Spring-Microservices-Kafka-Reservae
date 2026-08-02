package payment_service.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class JWTConverter
    implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<String> realmRoles =
            extractRealmRoles(jwt);

        List<SimpleGrantedAuthority> authorities =
            realmRoles.stream()
                .map(String::trim)
                .filter(role -> !role.isBlank())
                .map(String::toUpperCase)
                .map(role ->
                    new SimpleGrantedAuthority(
                        "ROLE_" + role
                    )
                )
                .toList();

        return new JwtAuthenticationToken(
            jwt,
            authorities,
            resolvePrincipalName(jwt)
        );
    }

    private Collection<String> extractRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess =
            jwt.getClaimAsMap("realm_access");

        if (realmAccess == null) {
            return List.of();
        }

        Object rolesClaim = realmAccess.get("roles");

        if (!(rolesClaim instanceof Collection<?> roles)) {
            return List.of();
        }

        return roles.stream()
            .filter(String.class::isInstance)
            .map(String.class::cast)
            .toList();
    }

    private String resolvePrincipalName(Jwt jwt) {
        String username =
            jwt.getClaimAsString("preferred_username");

        if (username != null && !username.isBlank()) {
            return username;
        }

        return jwt.getSubject();
    }
}