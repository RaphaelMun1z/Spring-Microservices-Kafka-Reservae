package notification_service.controllers;

import jakarta.validation.Valid;
import notification_service.controllers.contracts.NotificationContract;
import notification_service.dtos.req.SendManualNotificationRequestDTO;
import notification_service.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification-service/api/notifications")
public class NotificationController implements NotificationContract {
    private final NotificationService notificationService;

    public NotificationController(
        NotificationService notificationService
    ) {
        this.notificationService = notificationService;
    }

    @Override
    @PostMapping("/v1/manual")
    public ResponseEntity<Void> sendManualNotification(
        @Valid @RequestBody SendManualNotificationRequestDTO request
    ) {
        notificationService.sendManualNotificationEmail(request);

        return ResponseEntity.noContent().build();
    }
}