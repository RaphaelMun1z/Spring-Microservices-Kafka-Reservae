package user_profile_service.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Locale;

@Entity
@Table(
    name = "tb_user_profiles",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_user_profiles_email",
            columnNames = "email"
        ),
        @UniqueConstraint(
            name = "uk_user_profiles_document",
            columnNames = "document"
        )
    }
)
public class UserProfile {

    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    @NotBlank(message = "O nome completo é obrigatório")
    @Size(
        min = 3,
        max = 150,
        message = "O nome completo deve ter entre 3 e 150 caracteres"
    )
    @Column(
        name = "full_name",
        nullable = false,
        length = 150
    )
    private String fullName;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "O e-mail informado é inválido")
    @Size(max = 254)
    @Column(
        nullable = false,
        length = 254
    )
    private String email;

    @Size(
        min = 11,
        max = 11,
        message = "O CPF deve possuir 11 dígitos"
    )
    @Column(length = 11)
    private String document;

    protected UserProfile() {
    }

    public UserProfile(
        String id,
        String fullName,
        String email,
        String document
    ) {
        this.id = requireText(
            id,
            "O ID do usuário é obrigatório."
        );

        this.fullName = normalizeFullName(fullName);
        this.email = normalizeEmail(email);
        this.document = normalizeDocument(document);
    }

    /**
     * O e-mail pertence à identidade administrada pelo Keycloak.
     * Portanto, ele pode ser sincronizado sempre que o usuário acessa.
     */
    public void synchronizeEmail(String email) {
        this.email = normalizeEmail(email);
    }

    /**
     * Nome e CPF pertencem ao perfil de negócio do Reservae.
     */
    public void updateProfile(
        String fullName,
        String document
    ) {
        this.fullName = normalizeFullName(fullName);
        this.document = normalizeDocument(document);
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getDocument() {
        return document;
    }

    private static String normalizeFullName(String fullName) {
        return requireText(
            fullName,
            "O nome completo é obrigatório."
        ).trim();
    }

    private static String normalizeEmail(String email) {
        return requireText(
            email,
            "O e-mail é obrigatório."
        )
            .trim()
            .toLowerCase(Locale.ROOT);
    }

    private static String normalizeDocument(String document) {
        if (document == null || document.isBlank()) {
            return null;
        }

        String normalizedDocument =
            document.replaceAll("\\D", "");

        if (normalizedDocument.length() != 11) {
            throw new IllegalArgumentException(
                "O CPF deve possuir exatamente 11 dígitos."
            );
        }

        return normalizedDocument;
    }

    private static String requireText(
        String value,
        String message
    ) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof UserProfile userProfile)) {
            return false;
        }

        return id != null && id.equals(userProfile.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}