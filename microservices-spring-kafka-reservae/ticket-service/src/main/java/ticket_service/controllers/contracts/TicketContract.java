package ticket_service.controllers.contracts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import ticket_service.dtos.res.TicketResponseDTO;

import java.util.List;

@Tag(
    name = "Ticket Endpoint",
    description = "Consulta, acompanhamento e revogação de ingressos gerados"
)
public interface TicketContract {

    @Operation(summary = "Buscar os detalhes de um ingresso específico pelo ID")
    ResponseEntity<TicketResponseDTO> getTicketById(
        @PathVariable String id
    );

    @Operation(summary = "Listar todos os ingressos pertencentes a um usuário")
    ResponseEntity<List<TicketResponseDTO>> getTicketsByUser(
        @PathVariable String userId
    );

    @Operation(summary = "Listar todos os ingressos de um evento com paginação")
    ResponseEntity<Page<TicketResponseDTO>> getTicketsByEvent(
        @PathVariable String eventId,
        Pageable pageable
    );

    @Operation(summary = "Revogar um ingresso específico")
    ResponseEntity<Void> revokeTicket(
        @PathVariable String id
    );
}