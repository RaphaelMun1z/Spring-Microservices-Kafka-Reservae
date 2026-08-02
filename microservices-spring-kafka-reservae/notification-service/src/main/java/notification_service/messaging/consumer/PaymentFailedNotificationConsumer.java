package notification_service.messaging.consumer;

import notification_service.messaging.event.PaymentFailedNotificationRequestedEvent;
import notification_service.services.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentFailedNotificationConsumer {

    private final NotificationService notificationService;

    public PaymentFailedNotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(
        topics = "${reservae.config.kafka.topics.notificacao-pagamento-falhou}",
        containerFactory = "paymentFailedNotificationKafkaListenerContainerFactory"
    )
    public void consume(PaymentFailedNotificationRequestedEvent event) {
        notificationService.sendPaymentFailedEmail(event);
    }
}