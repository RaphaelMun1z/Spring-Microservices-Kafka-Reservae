package notification_service.templates;

public record EmailItem(
    String imageUrl,
    String eventName,
    String sectorName,
    Integer quantity,
    String unitPrice,
    String subtotal
) {
}