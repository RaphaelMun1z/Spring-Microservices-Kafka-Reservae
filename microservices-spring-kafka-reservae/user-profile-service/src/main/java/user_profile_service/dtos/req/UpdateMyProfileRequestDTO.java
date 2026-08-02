package user_profile_service.dtos.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateMyProfileRequestDTO(
    @NotBlank(message = "O nome completo é obrigatório")
    @Size(
        min = 3,
        max = 150,
        message = "O nome completo deve ter entre 3 e 150 caracteres"
    )
    String fullName,

    @Pattern(
        regexp = "^$|\\d{11}$",
        message = "O CPF deve possuir exatamente 11 dígitos"
    )
    String document
) {
}