package user_profile_service.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import user_profile_service.controllers.contracts.UserProfileContract;
import user_profile_service.dtos.req.UpdateMyProfileRequestDTO;
import user_profile_service.dtos.res.UserProfileResponseDTO;
import user_profile_service.services.UserProfileService;

@RestController
public class UserProfileController implements UserProfileContract {

    private final UserProfileService userProfileService;

    public UserProfileController(
        UserProfileService userProfileService
    ) {
        this.userProfileService = userProfileService;
    }

    @Override
    @GetMapping("/v1/me")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<UserProfileResponseDTO> getMyProfile(
        @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(
            userProfileService.syncAndGetProfile(jwt)
        );
    }

    @Override
    @PatchMapping("/v1/me")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<UserProfileResponseDTO> updateMyProfile(
        @AuthenticationPrincipal Jwt jwt,
        @Valid @RequestBody UpdateMyProfileRequestDTO request
    ) {
        return ResponseEntity.ok(
            userProfileService.updateMyProfile(jwt, request)
        );
    }
}