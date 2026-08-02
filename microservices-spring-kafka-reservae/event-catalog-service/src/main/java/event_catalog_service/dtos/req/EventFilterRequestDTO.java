package event_catalog_service.dtos.req;

import event_catalog_service.entities.enums.EventStatusEnum;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record EventFilterRequestDTO(
    String search,
    String city,
    String state,
    EventStatusEnum status,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime startDate,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime endDate
) {
}