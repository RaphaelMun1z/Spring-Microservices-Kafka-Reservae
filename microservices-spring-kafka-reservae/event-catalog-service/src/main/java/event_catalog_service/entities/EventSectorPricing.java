package event_catalog_service.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Entity
@Table(
    name = "tb_event_sector_pricings",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_event_sector_pricings_event_sector",
            columnNames = {
                "event_id",
                "sector_id"
            }
        )
    },
    indexes = {
        @Index(
            name = "idx_event_sector_pricings_event_id",
            columnList = "event_id"
        ),
        @Index(
            name = "idx_event_sector_pricings_sector_id",
            columnList = "sector_id"
        ),
        @Index(
            name = "idx_event_sector_pricings_event_available",
            columnList = "event_id, is_available"
        )
    }
)
public class EventSectorPricing {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "event_id",
        nullable = false
    )
    private Event event;

    @NotNull
    @Column(
        name = "sector_id",
        nullable = false
    )
    private String sectorId;

    @NotNull
    @PositiveOrZero(message = "O preço base não pode ser negativo")
    @Column(
        name = "base_price",
        nullable = false,
        precision = 10,
        scale = 2
    )
    private BigDecimal basePrice;

    @NotNull
    @PositiveOrZero(
        message = "O preço da meia-entrada não pode ser negativo"
    )
    @Column(
        name = "half_price",
        nullable = false,
        precision = 10,
        scale = 2
    )
    private BigDecimal halfPrice;

    @NotNull
    @Column(
        name = "is_available",
        nullable = false
    )
    private Boolean available;

    protected EventSectorPricing() {
    }

    public EventSectorPricing(
        Event event,
        String sectorId,
        BigDecimal basePrice,
        BigDecimal halfPrice
    ) {
        this.event = event;
        this.sectorId = sectorId;
        this.basePrice = basePrice;
        this.halfPrice = halfPrice;
        this.available = true;
    }

    public String getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    void setEvent(Event event) {
        this.event = event;
    }

    public String getSectorId() {
        return sectorId;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public BigDecimal getHalfPrice() {
        return halfPrice;
    }

    public boolean isAvailable() {
        return Boolean.TRUE.equals(available);
    }

    public void updatePrices(
        BigDecimal basePrice,
        BigDecimal halfPrice
    ) {
        validatePrice(basePrice, "O preço base");
        validatePrice(halfPrice, "O preço da meia-entrada");

        this.basePrice = basePrice;
        this.halfPrice = halfPrice;
    }

    public void suspendSales() {
        this.available = false;
    }

    public void resumeSales() {
        this.available = true;
    }

    private void validatePrice(
        BigDecimal price,
        String fieldName
    ) {
        if (price == null) {
            throw new IllegalArgumentException(
                fieldName + " é obrigatório."
            );
        }

        if (price.signum() < 0) {
            throw new IllegalArgumentException(
                fieldName + " não pode ser negativo."
            );
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof EventSectorPricing pricing)) {
            return false;
        }

        return id != null && id.equals(pricing.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}