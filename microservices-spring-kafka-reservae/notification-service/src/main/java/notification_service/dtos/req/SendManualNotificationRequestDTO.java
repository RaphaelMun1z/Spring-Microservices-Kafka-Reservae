package notification_service.dtos.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SendManualNotificationRequestDTO(

    @NotEmpty(message = "At least one recipient is required.")
    @Size(
        max = 100,
        message = "A notification can have at most 100 recipients."
    )
    List<@Valid EmailRecipientRequestDTO> recipients,

    @NotBlank(message = "Notification title is required.")
    @Size(
        max = 150,
        message = "Notification title must have at most 150 characters."
    )
    String title,

    @NotBlank(message = "Notification message is required.")
    @Size(
        max = 5000,
        message = "Notification message must have at most 5000 characters."
    )
    String message
) {
}