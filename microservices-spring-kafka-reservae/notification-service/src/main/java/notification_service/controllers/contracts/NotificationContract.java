package notification_service.controllers.contracts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import notification_service.dtos.req.SendManualNotificationRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(
    name = "Notification Endpoint",
    description = "Endpoints for manually sending administrative notifications."
)
public interface NotificationContract {

    @Operation(
        summary = "Send a manual notification",
        description = """
            Sends an administrative email to one or more recipients.
            Each recipient receives an individual and personalized email.
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Notifications sent successfully."
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request."
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error while sending notifications."
        )
    })
    ResponseEntity<Void> sendManualNotification(
        @Valid @RequestBody SendManualNotificationRequestDTO request
    );
}