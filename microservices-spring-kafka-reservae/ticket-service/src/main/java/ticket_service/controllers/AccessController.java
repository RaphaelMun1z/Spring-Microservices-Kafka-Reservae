package ticket_service.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticket_service.controllers.contracts.AccessContract;
import ticket_service.dtos.req.ValidateAccessRequestDTO;
import ticket_service.dtos.res.AccessValidationResponseDTO;
import ticket_service.entities.AccessLog;
import ticket_service.entities.enums.AccessStatusEnum;
import ticket_service.services.AccessService;

@RestController
@RequestMapping("/ticket-service/api/tickets/access")
public class AccessController implements AccessContract {

    private final AccessService accessService;

    public AccessController(AccessService accessService) {
        this.accessService = accessService;
    }

    @Override
    @PostMapping("/v1/validate")
    public ResponseEntity<AccessValidationResponseDTO> validateAccess(
        @Valid @RequestBody ValidateAccessRequestDTO dto
    ) {
        return ResponseEntity.ok(
            accessService.validateTicket(dto)
        );
    }

    @Override
    @GetMapping("/v1/logs")
    public ResponseEntity<Page<AccessLog>> getAccessLogs(
        @RequestParam(required = false) String eventId,
        @RequestParam(required = false) String gateId,
        @RequestParam(required = false) AccessStatusEnum result,
        Pageable pageable
    ) {
        return ResponseEntity.ok(
            accessService.findLogs(
                eventId,
                gateId,
                result,
                pageable
            )
        );
    }
}