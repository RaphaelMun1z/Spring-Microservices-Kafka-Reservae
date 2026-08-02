package notification_service.templates;

public record EmailLayoutData(
    String preheader,
    String headerContext,
    String title,
    String subtitle,
    String heroImageClasspath,
    String heroImageAlt,
    String bodyHtml,
    String primaryButtonLabel,
    String primaryButtonUrl,
    String footerNote,
    String senderName
) {
}