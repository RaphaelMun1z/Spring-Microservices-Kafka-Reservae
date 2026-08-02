package ticket_service.controllers.contracts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ticket_service.dtos.req.ValidateAccessRequestDTO;
import ticket_service.dtos.res.AccessValidationResponseDTO;
import ticket_service.entities.AccessLog;
import ticket_service.entities.enums.AccessStatusEnum;

@Tag(
    name = "Access Validation Endpoint",
    description = "Validação de ingressos nas catracas e consulta de histórico de acessos"
)
public interface AccessContract {

    @Operation(summary = "Validar a tentativa de acesso de um ingresso em uma catraca")
    ResponseEntity<AccessValidationResponseDTO> validateAccess(
        @RequestBody @Valid ValidateAccessRequestDTO dto
    );

    @Operation(summary = "Consultar histórico de tentativas de acesso com filtros e paginação")
    ResponseEntity<Page<AccessLog>> getAccessLogs(
        @RequestParam(required = false) String eventId,
        @RequestParam(required = false) String gateId,
        @RequestParam(required = false) AccessStatusEnum result,
        Pageable pageable
    );
}