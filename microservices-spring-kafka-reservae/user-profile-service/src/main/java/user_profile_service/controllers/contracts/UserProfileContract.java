package user_profile_service.controllers.contracts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import user_profile_service.dtos.req.UpdateMyProfileRequestDTO;
import user_profile_service.dtos.res.UserProfileResponseDTO;

@RequestMapping("/user-profile-service/api/profiles")
@SecurityRequirement(name = "bearerAuth")
public interface UserProfileContract {

    @Operation(
        summary = "Consultar meu perfil",
        description = """
            Consulta o perfil do usuário autenticado.
            
            Caso seja o primeiro acesso, o perfil é criado
            automaticamente usando as informações do JWT.
            """
    )
    ResponseEntity<UserProfileResponseDTO> getMyProfile(
        @AuthenticationPrincipal Jwt jwt
    );

    @Operation(
        summary = "Atualizar meu perfil",
        description = """
            Atualiza o nome e o CPF do usuário autenticado.
            
            O ID e o e-mail continuam sendo administrados
            pelo Keycloak.
            """
    )
    ResponseEntity<UserProfileResponseDTO> updateMyProfile(
        @AuthenticationPrincipal Jwt jwt,
        @Valid @RequestBody UpdateMyProfileRequestDTO request
    );
}