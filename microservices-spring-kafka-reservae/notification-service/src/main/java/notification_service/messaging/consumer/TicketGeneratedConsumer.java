package notification_service.messaging.consumer;

import notification_service.messaging.event.TicketGeneratedEvent;
import notification_service.services.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TicketGeneratedConsumer {
    private static final Logger logger = LoggerFactory.getLogger(TicketGeneratedConsumer.class);

    private final NotificationService notificationService;

    public TicketGeneratedConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(
        topics = "${reservae.config.kafka.topics.ingresso-gerado}",
        containerFactory = "ticketGeneratedKafkaListenerContainerFactory"
    )
    public void consume(TicketGeneratedEvent event) {
        logger.info(
            "Ingressos gerados recebidos para envio por e-mail. Pedido: {}. Quantidade: {}.",
            event.orderId(),
            event.items().size()
        );

        notificationService.sendTicketsGeneratedEmail(event);
    }
}