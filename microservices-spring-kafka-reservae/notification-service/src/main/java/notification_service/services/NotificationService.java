package notification_service.services;

import notification_service.dtos.req.EmailRecipientRequestDTO;
import notification_service.dtos.req.SendManualNotificationRequestDTO;
import notification_service.messaging.event.PaymentConfirmedNotificationRequestedEvent;
import notification_service.messaging.event.PaymentFailedNotificationRequestedEvent;
import notification_service.messaging.event.PaymentPendingNotificationRequestedEvent;
import notification_service.messaging.event.TicketGeneratedEvent;
import notification_service.templates.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private static final Logger logger =
        LoggerFactory.getLogger(NotificationService.class);

    private static final String MOCK_CUSTOMER_EMAIL =
        "raphaelmunizvarela@gmail.com";

    private static final String MOCK_CUSTOMER_NAME =
        "Nome do comprador";

    private final BrevoEmailSenderService emailSenderService;
    private final EmailFormattingService formattingService;
    private final QrCodeImageService qrCodeImageService;
    private final PaymentPendingEmailTemplate paymentPendingEmailTemplate;
    private final PaymentConfirmedEmailTemplate paymentConfirmedEmailTemplate;
    private final PaymentFailedEmailTemplate paymentFailedEmailTemplate;
    private final TicketsGeneratedEmailTemplate ticketsGeneratedEmailTemplate;
    private final TicketAvailableEmailTemplate ticketAvailableEmailTemplate;
    private final EventChangedEmailTemplate eventChangedEmailTemplate;
    private final EventCanceledEmailTemplate eventCanceledEmailTemplate;
    private final EventReminderEmailTemplate eventReminderEmailTemplate;
    private final ReviewRequestEmailTemplate reviewRequestEmailTemplate;
    private final ManualNotificationEmailTemplate manualNotificationEmailTemplate;

    public NotificationService(
        BrevoEmailSenderService emailSenderService,
        EmailFormattingService formattingService,
        QrCodeImageService qrCodeImageService,
        PaymentPendingEmailTemplate paymentPendingEmailTemplate,
        PaymentConfirmedEmailTemplate paymentConfirmedEmailTemplate,
        PaymentFailedEmailTemplate paymentFailedEmailTemplate,
        TicketsGeneratedEmailTemplate ticketsGeneratedEmailTemplate,
        TicketAvailableEmailTemplate ticketAvailableEmailTemplate,
        EventChangedEmailTemplate eventChangedEmailTemplate,
        EventCanceledEmailTemplate eventCanceledEmailTemplate,
        EventReminderEmailTemplate eventReminderEmailTemplate,
        ReviewRequestEmailTemplate reviewRequestEmailTemplate,
        ManualNotificationEmailTemplate manualNotificationEmailTemplate
    ) {
        this.emailSenderService = emailSenderService;
        this.formattingService = formattingService;
        this.qrCodeImageService = qrCodeImageService;
        this.paymentPendingEmailTemplate = paymentPendingEmailTemplate;
        this.paymentConfirmedEmailTemplate = paymentConfirmedEmailTemplate;
        this.paymentFailedEmailTemplate = paymentFailedEmailTemplate;
        this.ticketsGeneratedEmailTemplate = ticketsGeneratedEmailTemplate;
        this.ticketAvailableEmailTemplate = ticketAvailableEmailTemplate;
        this.eventChangedEmailTemplate = eventChangedEmailTemplate;
        this.eventCanceledEmailTemplate = eventCanceledEmailTemplate;
        this.eventReminderEmailTemplate = eventReminderEmailTemplate;
        this.reviewRequestEmailTemplate = reviewRequestEmailTemplate;
        this.manualNotificationEmailTemplate = manualNotificationEmailTemplate;
    }

    public void sendPaymentPendingEmail(
        PaymentPendingNotificationRequestedEvent event
    ) {
        List<EmailItem> items = event.items()
            .stream()
            .map(item -> new EmailItem(
                item.imageUrl(),
                event.eventName(),
                item.sectorName(),
                item.quantity(),
                formattingService.formatMoney(item.unitPrice()),
                formattingService.formatMoney(item.subtotal())
            ))
            .toList();

        EmailTemplate template = paymentPendingEmailTemplate.build(
            event.customerName(),
            event.orderId(),
            formattingService.formatDateTime(event.paymentExpiresAt()),
            formattingService.formatMoney(event.totalAmount()),
            items,
            event.paymentUrl(),
            event.orderUrl(),
            emailSenderService.getSenderName()
        );

        emailSenderService.send(
            event.customerEmail(),
            event.customerName(),
            template
        );

        logger.info(
            "Payment pending email sent to customer {} for order {}.",
            event.customerEmail(),
            event.orderId()
        );
    }

    public void sendPaymentConfirmedEmail(
        PaymentConfirmedNotificationRequestedEvent event
    ) {
        EmailTemplate template = paymentConfirmedEmailTemplate.build(
            event.customerName(),
            event.orderId(),
            event.eventName(),
            formattingService.formatDateTime(LocalDateTime.parse(event.eventDate())),
            formattingService.formatMoney(event.totalAmount()),
            emailSenderService.getSenderName()
        );

        emailSenderService.send(
            event.customerEmail(),
            event.customerName(),
            template
        );

        logger.info(
            "Payment confirmed email sent to customer {} for order {}.",
            event.customerEmail(),
            event.orderId()
        );
    }

    public void sendPaymentFailedEmail(
        PaymentFailedNotificationRequestedEvent event
    ) {
        EmailTemplate template = paymentFailedEmailTemplate.build(
            event.customerName(),
            event.orderId(),
            event.eventName(),
            formattingService.safeText(event.reason()),
            event.paymentUrl(),
            emailSenderService.getSenderName()
        );

        emailSenderService.send(
            event.customerEmail(),
            event.customerName(),
            template
        );

        logger.info(
            "Payment failed email sent to customer {} for order {}.",
            event.customerEmail(),
            event.orderId()
        );
    }

    public void sendTicketsGeneratedEmail(
        TicketGeneratedEvent event
    ) {
        List<EmailAttachment> attachments = event.items()
            .stream()
            .map(item -> new EmailAttachment(
                "ticket-" + item.ticketId() + ".png",
                qrCodeImageService.generateQrCodeBase64(
                    item.qrCodeHash()
                )
            ))
            .toList();

        EmailTemplate template = ticketsGeneratedEmailTemplate.build(
            event,
            emailSenderService.getSenderName()
        );

        emailSenderService.send(
            MOCK_CUSTOMER_EMAIL,
            MOCK_CUSTOMER_NAME,
            template,
            attachments
        );

        logger.info(
            "Tickets generated email containing {} attachment(s) sent for order {}.",
            event.items().size(),
            event.orderId()
        );
    }

    public void sendTicketAvailableEmail(
        String recipientEmail,
        String recipientName,
        String orderId,
        String eventName,
        String sectorName,
        String quantity
    ) {
        EmailTemplate template = ticketAvailableEmailTemplate.build(
            recipientName,
            orderId,
            eventName,
            sectorName,
            quantity,
            emailSenderService.getSenderName()
        );

        emailSenderService.send(
            recipientEmail,
            recipientName,
            template
        );

        logger.info(
            "Ticket available email sent to {} for order {}.",
            recipientEmail,
            orderId
        );
    }

    public void sendEventChangedEmail(
        String recipientEmail,
        String recipientName,
        String eventName,
        String previousInformation,
        String newInformation,
        String changeDescription
    ) {
        EmailTemplate template = eventChangedEmailTemplate.build(
            recipientName,
            eventName,
            previousInformation,
            newInformation,
            changeDescription,
            emailSenderService.getSenderName()
        );

        emailSenderService.send(
            recipientEmail,
            recipientName,
            template
        );

        logger.info(
            "Event changed email sent to {} for event {}.",
            recipientEmail,
            eventName
        );
    }

    public void sendEventCanceledEmail(
        String recipientEmail,
        String recipientName,
        String eventName,
        String orderId,
        String refundInstructions
    ) {
        EmailTemplate template = eventCanceledEmailTemplate.build(
            recipientName,
            eventName,
            orderId,
            refundInstructions,
            emailSenderService.getSenderName()
        );

        emailSenderService.send(
            recipientEmail,
            recipientName,
            template
        );

        logger.info(
            "Event canceled email sent to {} for event {}.",
            recipientEmail,
            eventName
        );
    }

    public void sendEventReminderEmail(
        String recipientEmail,
        String recipientName,
        String eventName,
        String date,
        String time,
        String location,
        String sector
    ) {
        EmailTemplate template = eventReminderEmailTemplate.build(
            recipientName,
            eventName,
            date,
            time,
            location,
            sector,
            emailSenderService.getSenderName()
        );

        emailSenderService.send(
            recipientEmail,
            recipientName,
            template
        );

        logger.info(
            "Event reminder email sent to {} for event {}.",
            recipientEmail,
            eventName
        );
    }

    public void sendReviewRequestEmail(
        String recipientEmail,
        String recipientName,
        String eventName,
        String orderId
    ) {
        EmailTemplate template = reviewRequestEmailTemplate.build(
            recipientName,
            eventName,
            orderId,
            emailSenderService.getSenderName()
        );

        emailSenderService.send(
            recipientEmail,
            recipientName,
            template
        );

        logger.info(
            "Review request email sent to {} for event {}.",
            recipientEmail,
            eventName
        );
    }

    public void sendManualNotificationEmail(
        SendManualNotificationRequestDTO request
    ) {
        for (EmailRecipientRequestDTO recipient : request.recipients()) {
            EmailTemplate template = manualNotificationEmailTemplate.build(
                recipient.name(),
                request.title(),
                request.message(),
                emailSenderService.getSenderName()
            );

            emailSenderService.send(
                recipient.email(),
                recipient.name(),
                template
            );

            logger.info(
                "Manual notification email sent to {}.",
                recipient.email()
            );
        }
    }
}