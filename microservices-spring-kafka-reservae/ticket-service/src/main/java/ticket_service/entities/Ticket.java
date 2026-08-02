package ticket_service.entities;

import jakarta.persistence.*;
import ticket_service.entities.enums.TicketStatusEnum;
import ticket_service.exceptions.models.BusinessException;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "event_id", nullable = false)
    private String eventId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "sector_id", nullable = false)
    private String sectorId;

    @Column(name = "reservation_id", nullable = false)
    private String reservationId;

    @Column(name = "ticket_type", nullable = false)
    private String ticketType;

    @Column(name = "qr_code_hash", nullable = false, unique = true)
    private String qrCodeHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TicketStatusEnum status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    protected Ticket() {
    }

    public Ticket(
        String orderId,
        String eventId,
        String userId,
        String sectorId,
        String reservationId,
        String ticketType,
        String qrCodeHash,
        TicketStatusEnum status
    ) {
        this.orderId = orderId;
        this.eventId = eventId;
        this.userId = userId;
        this.sectorId = sectorId;
        this.reservationId = reservationId;
        this.ticketType = ticketType;
        this.qrCodeHash = qrCodeHash;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getUserId() {
        return userId;
    }

    public String getSectorId() {
        return sectorId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getTicketType() {
        return ticketType;
    }

    public String getQrCodeHash() {
        return qrCodeHash;
    }

    public TicketStatusEnum getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }

    public void revokeTicket() {
        if (TicketStatusEnum.USED.equals(this.status)) {
            throw new BusinessException("Ingresso já utilizado não pode ser revogado.");
        }

        if (TicketStatusEnum.REVOKED.equals(this.status)) {
            throw new BusinessException("Ingresso já está revogado.");
        }

        this.status = TicketStatusEnum.REVOKED;
    }

    public void useTicket() {
        if (!TicketStatusEnum.VALID.equals(this.status)) {
            throw new IllegalStateException("Ingresso não está válido para uso.");
        }

        this.status = TicketStatusEnum.USED;
        this.usedAt = LocalDateTime.now();
    }

    public void validateTicket() {
        this.status = TicketStatusEnum.VALID;
        this.usedAt = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket ticket)) return false;
        return id != null && id.equals(ticket.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}