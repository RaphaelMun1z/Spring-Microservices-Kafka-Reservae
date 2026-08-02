package ticket_service.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticket_service.dtos.req.ValidateAccessRequestDTO;
import ticket_service.dtos.res.AccessValidationResponseDTO;
import ticket_service.entities.AccessLog;
import ticket_service.entities.Ticket;
import ticket_service.entities.enums.AccessStatusEnum;
import ticket_service.repositories.AccessLogRepository;
import ticket_service.repositories.TicketRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AccessService {
    private final TicketRepository ticketRepository;
    private final AccessLogRepository accessLogRepository;

    public AccessService(
        TicketRepository ticketRepository,
        AccessLogRepository accessLogRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.accessLogRepository = accessLogRepository;
    }

    @Transactional
    public AccessValidationResponseDTO validateTicket(ValidateAccessRequestDTO dto) {
        Optional<Ticket> ticketOpt = ticketRepository.findByQrCodeHash(dto.qrCodeHash());

        if (ticketOpt.isEmpty()) {
            return new AccessValidationResponseDTO(
                false,
                AccessStatusEnum.DENIED_INVALID,
                "QR Code inválido ou não encontrado.",
                null,
                null
            );
        }

        Ticket ticket = ticketOpt.get();

        boolean isAllowed = false;
        AccessStatusEnum result;
        String message;

        switch (ticket.getStatus()) {
            case VALID -> {
                ticket.useTicket();
                result = AccessStatusEnum.GRANTED;
                isAllowed = true;
                message = "Acesso liberado.";
            }
            case USED -> {
                result = AccessStatusEnum.DENIED_USED;
                message = "Ingresso já utilizado.";
            }
            case REVOKED -> {
                result = AccessStatusEnum.DENIED_REVOKED;
                message = "Ingresso cancelado ou revogado.";
            }
            default -> {
                result = AccessStatusEnum.DENIED_INVALID;
                message = "Status do ingresso inválido.";
            }
        }

        AccessLog log = new AccessLog(
            ticket.getId(),
            ticket.getEventId(),
            dto.gateId(),
            LocalDateTime.now(),
            result
        );

        accessLogRepository.save(log);

        return new AccessValidationResponseDTO(
            isAllowed,
            result,
            message,
            ticket.getSectorId(),
            ticket.getId()
        );
    }

    public Page<AccessLog> findLogs(
        String eventId,
        String gateId,
        AccessStatusEnum result,
        Pageable pageable
    ) {
        return accessLogRepository.findLogsWithFilters(
            eventId,
            gateId,
            result,
            pageable
        );
    }
}