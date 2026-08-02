package payment_service.proxy.order.dtos;

public enum OrderStatusEnum {
    PENDING,
    AWAITING_PAYMENT,
    RESERVATION_FAILED,
    CONFIRMED,
    PAYMENT_FAILED,
    CANCELLED
}