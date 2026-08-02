package event_catalog_service.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(
    name = "tb_sectors",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_sectors_venue_name",
            columnNames = {
                "venue_id",
                "name"
            }
        )
    },
    indexes = {
        @Index(
            name = "idx_sectors_venue_id",
            columnList = "venue_id"
        ),
        @Index(
            name = "idx_sectors_name",
            columnList = "name"
        )
    }
)
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "venue_id",
        nullable = false
    )
    private Venue venue;

    @NotBlank(message = "O nome do setor é obrigatório")
    @Size(
        min = 2,
        max = 100,
        message = "O nome deve ter entre 2 e 100 caracteres"
    )
    @Column(
        nullable = false,
        length = 100
    )
    private String name;

    @NotNull
    @Positive(message = "A capacidade do setor deve ser maior que zero")
    @Column(nullable = false)
    private Integer capacity;

    protected Sector() {
    }

    public Sector(
        Venue venue,
        String name,
        Integer capacity
    ) {
        this.venue = venue;
        this.name = normalizeName(name);
        this.capacity = capacity;
    }

    public String getId() {
        return id;
    }

    public Venue getVenue() {
        return venue;
    }

    void setVenue(Venue venue) {
        this.venue = venue;
    }

    public String getName() {
        return name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void updateName(String name) {
        this.name = normalizeName(name);
    }

    public void updateCapacity(Integer newCapacity) {
        if (newCapacity == null || newCapacity <= 0) {
            throw new IllegalArgumentException(
                "A nova capacidade deve ser maior que zero."
            );
        }

        this.capacity = newCapacity;
    }

    private static String normalizeName(String name) {
        if (name == null || name.isBlank()) {
            return name;
        }

        return name.trim();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Sector sector)) {
            return false;
        }

        return id != null && id.equals(sector.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}