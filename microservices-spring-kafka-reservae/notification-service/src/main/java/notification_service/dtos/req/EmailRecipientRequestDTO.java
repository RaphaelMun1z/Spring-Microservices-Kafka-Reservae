package notification_service.dtos.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmailRecipientRequestDTO(

    @NotBlank(message = "Recipient name is required.")
    @Size(
        max = 120,
        message = "Recipient name must have at most 120 characters."
    )
    String name,

    @NotBlank(message = "Recipient email is required.")
    @Email(message = "Recipient email must be valid.")
    @Size(
        max = 254,
        message = "Recipient email must have at most 254 characters."
    )
    String email
) {
}