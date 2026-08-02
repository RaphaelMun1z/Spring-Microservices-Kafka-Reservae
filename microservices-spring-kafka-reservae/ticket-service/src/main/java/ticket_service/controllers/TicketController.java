package ticket_service.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticket_service.controllers.contracts.TicketContract;
import ticket_service.dtos.res.TicketResponseDTO;
import ticket_service.services.TicketService;

import java.util.List;

@RestController
@RequestMapping("/ticket-service/api/tickets")
public class TicketController implements TicketContract {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Override
    @GetMapping("/v1/{id}")
    public ResponseEntity<TicketResponseDTO> getTicketById(String id) {
        return ResponseEntity.ok(
            ticketService.findById(id)
        );
    }

    @Override
    @GetMapping("/v1/user/{userId}")
    public ResponseEntity<List<TicketResponseDTO>> getTicketsByUser(String userId) {
        return ResponseEntity.ok(
            ticketService.findByUserId(userId)
        );
    }

    @Override
    @GetMapping("/v1/event/{eventId}")
    public ResponseEntity<Page<TicketResponseDTO>> getTicketsByEvent(
        String eventId,
        Pageable pageable
    ) {
        return ResponseEntity.ok(
            ticketService.findByEventId(
                eventId,
                pageable
            )
        );
    }

    @Override
    @PatchMapping("/v1/{id}/revoke")
    public ResponseEntity<Void> revokeTicket(String id) {
        ticketService.revokeTicket(id);

        return ResponseEntity.noContent().build();
    }
}