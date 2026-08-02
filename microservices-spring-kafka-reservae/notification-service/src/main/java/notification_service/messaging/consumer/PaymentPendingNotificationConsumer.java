package notification_service.messaging.consumer;

import notification_service.messaging.event.PaymentPendingNotificationRequestedEvent;
import notification_service.services.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentPendingNotificationConsumer {
    private final NotificationService notificationService;

    public PaymentPendingNotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(
        topics = "${reservae.config.kafka.topics.notificacao-pagamento-pendente}",
        containerFactory = "paymentPendingNotificationKafkaListenerContainerFactory"
    )
    public void consume(PaymentPendingNotificationRequestedEvent event) {
        notificationService.sendPaymentPendingEmail(event);
    }
}