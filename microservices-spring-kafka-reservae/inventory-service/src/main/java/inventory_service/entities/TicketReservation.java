package inventory_service.entities;

import inventory_service.entities.enums.ReservationStatusEnum;
import inventory_service.exceptions.models.BusinessException;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("ticket_reservation")
public class TicketReservation {

    @Id
    private String reservationId;

    @Indexed
    private String eventId;

    @Indexed
    private String sectorId;

    @Indexed
    private String userId;

    @Indexed
    private String orderId;

    private int quantity;

    @Indexed
    private ReservationStatusEnum reservationStatus;

    @TimeToLive
    private Long timeToLive;

    public TicketReservation() {
    }

    public TicketReservation(
        String eventId,
        String sectorId,
        String userId,
        String orderId,
        int quantity,
        long timeToLiveSeconds
    ) {
        validateQuantity(quantity);
        validateTimeToLive(timeToLiveSeconds);

        this.reservationId = buildReservationId(orderId, userId, eventId, sectorId);
        this.eventId = eventId;
        this.sectorId = sectorId;
        this.userId = userId;
        this.orderId = orderId;
        this.quantity = quantity;
        this.reservationStatus = ReservationStatusEnum.RESERVED;
        this.timeToLive = timeToLiveSeconds;
    }

    public static String buildReservationId(
        String orderId,
        String userId,
        String eventId,
        String sectorId
    ) {
        return "order:%s:user:%s:event:%s:sector:%s"
            .formatted(orderId, userId, eventId, sectorId);
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getSectorId() {
        return sectorId;
    }

    public String getUserId() {
        return userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public ReservationStatusEnum getReservationStatus() {
        return reservationStatus;
    }

    public Long getTimeToLive() {
        return timeToLive;
    }

    public void confirm() {
        if (!ReservationStatusEnum.RESERVED.equals(this.reservationStatus)) {
            throw new BusinessException("A reserva não está em estado válido para confirmação.");
        }

        this.reservationStatus = ReservationStatusEnum.CONFIRMED;
        this.timeToLive = -1L;
    }

    public void cancel() {
        if (!ReservationStatusEnum.RESERVED.equals(this.reservationStatus)) {
            throw new BusinessException("A reserva não está em estado válido para cancelamento.");
        }

        this.reservationStatus = ReservationStatusEnum.CANCELLED;
        this.timeToLive = -1L;
    }

    public void expire() {
        if (!ReservationStatusEnum.RESERVED.equals(this.reservationStatus)) {
            throw new BusinessException("A reserva não está em estado válido para expiração.");
        }

        this.reservationStatus = ReservationStatusEnum.EXPIRED;
        this.timeToLive = -1L;
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new BusinessException("A quantidade de ingressos deve ser maior que zero.");
        }
    }

    private void validateTimeToLive(long timeToLiveSeconds) {
        if (timeToLiveSeconds <= 0) {
            throw new BusinessException("O tempo de expiração da reserva deve ser maior que zero.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketReservation ticketReservation)) return false;
        return reservationId != null && reservationId.equals(ticketReservation.reservationId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}