package notification_service.templates;

public record EmailTemplate(
    String subject,
    String html
) {
}