package user_profile_service.services;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user_profile_service.dtos.req.UpdateMyProfileRequestDTO;
import user_profile_service.dtos.res.UserProfileResponseDTO;
import user_profile_service.entities.UserProfile;
import user_profile_service.repositories.UserProfileRepository;
import user_profile_service.services.dataHandler.UserProfileMapper;

@Service
public class UserProfileService {

    private final UserProfileRepository repository;
    private final UserProfileMapper mapper;

    public UserProfileService(
        UserProfileRepository repository,
        UserProfileMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public UserProfileResponseDTO syncAndGetProfile(Jwt jwt) {
        String userId = extractUserId(jwt);
        String email = extractEmail(jwt);
        String initialFullName = extractFullName(jwt);

        UserProfile profile = repository
            .findById(userId)
            .map(existingProfile -> {
                validateEmailOwnership(
                    email,
                    userId
                );

                existingProfile.synchronizeEmail(email);

                return existingProfile;
            })
            .orElseGet(() -> createProfile(
                userId,
                initialFullName,
                email
            ));

        return mapper.toResponseDTO(profile);
    }

    @Transactional
    public UserProfileResponseDTO updateMyProfile(
        Jwt jwt,
        UpdateMyProfileRequestDTO request
    ) {
        String userId = extractUserId(jwt);

        /*
         * Garante que o perfil seja criado caso este seja
         * o primeiro acesso do usuário ao serviço.
         */
        syncAndGetProfile(jwt);

        UserProfile profile = repository
            .findById(userId)
            .orElseThrow(() ->
                new IllegalStateException(
                    "Perfil do usuário não encontrado."
                )
            );

        String normalizedDocument =
            normalizeDocument(request.document());

        validateDocumentOwnership(
            normalizedDocument,
            userId
        );

        profile.updateProfile(
            request.fullName(),
            normalizedDocument
        );

        return mapper.toResponseDTO(profile);
    }

    private UserProfile createProfile(
        String userId,
        String fullName,
        String email
    ) {
        validateEmailOwnership(
            email,
            userId
        );

        UserProfile profile = new UserProfile(
            userId,
            fullName,
            email,
            null
        );

        return repository.save(profile);
    }

    private void validateEmailOwnership(
        String email,
        String userId
    ) {
        boolean emailAlreadyInUse =
            repository.existsByEmailIgnoreCaseAndIdNot(
                email,
                userId
            );

        if (emailAlreadyInUse) {
            throw new IllegalStateException(
                "O e-mail já está associado a outro usuário."
            );
        }
    }

    private void validateDocumentOwnership(
        String document,
        String userId
    ) {
        if (document == null) {
            return;
        }

        boolean documentAlreadyInUse =
            repository.existsByDocumentAndIdNot(
                document,
                userId
            );

        if (documentAlreadyInUse) {
            throw new IllegalStateException(
                "O CPF já está associado a outro usuário."
            );
        }
    }

    private String extractUserId(Jwt jwt) {
        String subject = jwt.getSubject();

        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException(
                "Token sem identificador de usuário."
            );
        }

        return subject;
    }

    private String extractEmail(Jwt jwt) {
        String email = jwt.getClaimAsString("email");

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                "Token sem claim de e-mail. Verifique os scopes do cliente no Keycloak."
            );
        }

        return email.trim().toLowerCase();
    }

    private String extractFullName(Jwt jwt) {
        String name = jwt.getClaimAsString("name");

        if (name != null && !name.isBlank()) {
            return name.trim();
        }

        String givenName =
            jwt.getClaimAsString("given_name");

        String familyName =
            jwt.getClaimAsString("family_name");

        String fullName = String.join(
            " ",
            normalizeNullable(givenName),
            normalizeNullable(familyName)
        ).trim();

        if (!fullName.isBlank()) {
            return fullName;
        }

        String username =
            jwt.getClaimAsString("preferred_username");

        if (username != null && !username.isBlank()) {
            return username.trim();
        }

        return "Usuário Reservae";
    }

    private String normalizeDocument(String document) {
        if (document == null || document.isBlank()) {
            return null;
        }

        String normalized =
            document.replaceAll("\\D", "");

        if (normalized.length() != 11) {
            throw new IllegalArgumentException(
                "O CPF deve possuir exatamente 11 dígitos."
            );
        }

        return normalized;
    }

    private String normalizeNullable(String value) {
        return value == null ? "" : value.trim();
    }
}