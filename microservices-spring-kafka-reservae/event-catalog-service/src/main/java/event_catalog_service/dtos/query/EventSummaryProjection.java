package event_catalog_service.dtos.query;

import java.time.LocalDateTime;

public interface EventSummaryProjection {

    String getEventId();

    String getTitle();

    LocalDateTime getEventDate();

    String getStatus();

    String getVenueName();

    String getVenueCity();

    String getVenueState();
}