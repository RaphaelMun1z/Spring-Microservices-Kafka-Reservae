package ticket_service.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ticket_service.entities.AccessLog;
import ticket_service.entities.enums.AccessStatusEnum;

public interface AccessLogRepository extends JpaRepository<AccessLog, String> {
    @Query("SELECT a " +
        "FROM AccessLog a " +
        "WHERE " +
        "(:gateId IS NULL OR a.gateId = :gateId) " +
        "AND " + "(:result IS NULL OR a.result = :result)")
    Page<AccessLog> findLogsWithFilters(
        @Param("eventId") String eventId,
        @Param("gateId") String gateId,
        @Param("result") AccessStatusEnum result,
        Pageable pageable
    );
}
