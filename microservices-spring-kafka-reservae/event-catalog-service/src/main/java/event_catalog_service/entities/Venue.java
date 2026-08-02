package event_catalog_service.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(
    name = "tb_venues",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_venues_name_city_state",
            columnNames = {
                "name",
                "city",
                "state"
            }
        )
    },
    indexes = {
        @Index(
            name = "idx_venues_city",
            columnList = "city"
        ),
        @Index(
            name = "idx_venues_state",
            columnList = "state"
        ),
        @Index(
            name = "idx_venues_city_state",
            columnList = "city, state"
        )
    }
)
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "O nome do local é obrigatório")
    @Size(
        min = 3,
        max = 150,
        message = "O nome deve ter entre 3 e 150 caracteres"
    )
    @Column(
        nullable = false,
        length = 150
    )
    private String name;

    @NotBlank(message = "A cidade é obrigatória")
    @Size(
        max = 100,
        message = "A cidade deve ter no máximo 100 caracteres"
    )
    @Column(
        nullable = false,
        length = 100
    )
    private String city;

    @NotBlank(message = "A sigla do estado é obrigatória")
    @Size(
        min = 2,
        max = 2,
        message = "O estado deve conter exatamente 2 caracteres"
    )
    @Column(
        nullable = false,
        length = 2
    )
    private String state;

    @NotNull
    @Positive(message = "A capacidade total deve ser maior que zero")
    @Column(
        name = "total_capacity",
        nullable = false
    )
    private Integer totalCapacity;

    @OneToMany(
        mappedBy = "venue",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Sector> sectors = new ArrayList<>();

    protected Venue() {
    }

    public Venue(
        String name,
        String city,
        String state,
        Integer totalCapacity
    ) {
        this.name = normalizeRequiredText(name);
        this.city = normalizeRequiredText(city);
        this.state = normalizeState(state);
        this.totalCapacity = totalCapacity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public Integer getTotalCapacity() {
        return totalCapacity;
    }

    public List<Sector> getSectors() {
        return Collections.unmodifiableList(sectors);
    }

    public void addSector(Sector sector) {
        if (sector == null) {
            throw new IllegalArgumentException(
                "O setor não pode ser nulo."
            );
        }

        if (sectors.contains(sector)) {
            return;
        }

        sector.setVenue(this);
        sectors.add(sector);
    }

    public void addMultipleSectors(List<Sector> sectors) {
        if (sectors == null || sectors.isEmpty()) {
            return;
        }

        sectors.forEach(this::addSector);
    }

    public void removeSector(Sector sector) {
        if (sector == null) {
            return;
        }

        boolean removed = sectors.remove(sector);

        if (removed) {
            sector.setVenue(null);
        }
    }

    public void updateLocation(
        String city,
        String state
    ) {
        if (city != null && !city.isBlank()) {
            this.city = city.trim();
        }

        if (state != null && !state.isBlank()) {
            this.state = normalizeState(state);
        }
    }

    public void updateTotalCapacity(Integer totalCapacity) {
        if (totalCapacity == null || totalCapacity <= 0) {
            throw new IllegalArgumentException(
                "A capacidade total deve ser maior que zero."
            );
        }

        this.totalCapacity = totalCapacity;
    }

    private static String normalizeRequiredText(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }

        return value.trim();
    }

    private static String normalizeState(String state) {
        if (state == null || state.isBlank()) {
            return state;
        }

        String normalizedState = state.trim().toUpperCase();

        if (normalizedState.length() != 2) {
            throw new IllegalArgumentException(
                "O estado deve possuir exatamente 2 caracteres."
            );
        }

        return normalizedState;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Venue venue)) {
            return false;
        }

        return id != null && id.equals(venue.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}