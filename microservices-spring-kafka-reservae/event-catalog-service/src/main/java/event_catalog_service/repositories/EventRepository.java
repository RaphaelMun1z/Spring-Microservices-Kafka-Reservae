package event_catalog_service.repositories;

import event_catalog_service.dtos.query.EventDetailsProjection;
import event_catalog_service.dtos.query.EventSummaryProjection;
import event_catalog_service.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, String> {
    @Query(value = """
        SELECT
            e.id AS eventId,
            e.title AS title,
            e.event_date AS eventDate,
            e.status AS status,
        
            v.name AS venueName,
            v.city AS venueCity,
            v.state AS venueState
        
        FROM tb_events e
        
        INNER JOIN tb_venues v
            ON v.id = e.venue_id
        
        WHERE e.id = :eventId
        """, nativeQuery = true)
    Optional<EventDetailsProjection> findEventDetailsByEventId(
        @Param("eventId") String eventId
    );

    @Query(
        value = """
            SELECT
                e.id AS eventId,
                e.title AS title,
                e.event_date AS eventDate,
                CAST(e.status AS TEXT) AS status,
            
                v.name AS venueName,
                v.city AS venueCity,
                v.state AS venueState
            
            FROM tb_events e
            
            INNER JOIN tb_venues v
                ON v.id = e.venue_id
            
            WHERE
                (
                    :search IS NULL
                    OR LOWER(e.title) LIKE LOWER(CONCAT('%', :search, '%'))
                )
            
                AND (
                    :city IS NULL
                    OR LOWER(v.city) = LOWER(:city)
                )
            
                AND (
                    :state IS NULL
                    OR UPPER(v.state) = UPPER(:state)
                )
            
                AND (
                    :status IS NULL
                    OR CAST(e.status AS TEXT) = :status
                )
            
                AND (
                    CAST(:startDate AS timestamp) IS NULL
                    OR e.event_date >= CAST(:startDate AS timestamp)
                )
            
                AND (
                    CAST(:endDate AS timestamp) IS NULL
                    OR e.event_date <= CAST(:endDate AS timestamp)
                )
            """,
        countQuery = """
            SELECT COUNT(e.id)
            
            FROM tb_events e
            
            INNER JOIN tb_venues v
                ON v.id = e.venue_id
            
            WHERE
                (
                    :search IS NULL
                    OR LOWER(e.title) LIKE LOWER(CONCAT('%', :search, '%'))
                )
            
                AND (
                    :city IS NULL
                    OR LOWER(v.city) = LOWER(:city)
                )
            
                AND (
                    :state IS NULL
                    OR UPPER(v.state) = UPPER(:state)
                )
            
                AND (
                    :status IS NULL
                    OR CAST(e.status AS TEXT) = :status
                )
            
                AND (
                    CAST(:startDate AS timestamp) IS NULL
                    OR e.event_date >= CAST(:startDate AS timestamp)
                )
            
                AND (
                    CAST(:endDate AS timestamp) IS NULL
                    OR e.event_date <= CAST(:endDate AS timestamp)
                )
            """,
        nativeQuery = true
    )
    Page<EventSummaryProjection> findEventsWithFilters(
        @Param("search") String search,
        @Param("city") String city,
        @Param("state") String state,
        @Param("status") String status,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable
    );
}
