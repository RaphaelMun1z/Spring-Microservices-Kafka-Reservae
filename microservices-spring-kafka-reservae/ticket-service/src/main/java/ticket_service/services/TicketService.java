package ticket_service.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticket_service.dtos.res.TicketResponseDTO;
import ticket_service.entities.Ticket;
import ticket_service.entities.enums.TicketStatusEnum;
import ticket_service.exceptions.models.NotFoundException;
import ticket_service.messaging.event.OrderConfirmedEvent;
import ticket_service.messaging.event.OrderConfirmedItemEvent;
import ticket_service.messaging.event.TicketGeneratedEvent;
import ticket_service.messaging.event.TicketGeneratedItemEvent;
import ticket_service.messaging.publisher.TicketGeneratedPublisher;
import ticket_service.proxy.eventCatalog.EventCatalogProxy;
import ticket_service.proxy.eventCatalog.dto.EventDetailsResponseDTO;
import ticket_service.proxy.eventCatalog.dto.EventSectorDetailsDTO;
import ticket_service.repositories.TicketRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final QrCodeService qrCodeService;
    private final TicketGeneratedPublisher ticketGeneratedPublisher;
    private final EventCatalogProxy eventCatalogProxy;

    public TicketService(
        TicketRepository ticketRepository,
        QrCodeService qrCodeService,
        TicketGeneratedPublisher ticketGeneratedPublisher,
        EventCatalogProxy eventCatalogProxy
    ) {
        this.ticketRepository = ticketRepository;
        this.qrCodeService = qrCodeService;
        this.ticketGeneratedPublisher = ticketGeneratedPublisher;
        this.eventCatalogProxy = eventCatalogProxy;
    }

    @Transactional
    public List<Ticket> generateFromConfirmedOrder(OrderConfirmedEvent event) {
        if (event.items() == null || event.items().isEmpty()) {
            throw new IllegalArgumentException("O pedido confirmado não possui itens para gerar ingressos.");
        }

        List<Ticket> ticketsToGenerate = new ArrayList<>();

        for (OrderConfirmedItemEvent item : event.items()) {
            for (int index = 0; index < item.quantity(); index++) {
                String qrCodeHash = qrCodeService.generateQrCodeHash(
                    event.orderId(),
                    item.orderItemId(),
                    item.reservationId(),
                    event.userId(),
                    event.eventId(),
                    item.sectorId()
                );

                Ticket ticket = new Ticket(
                    event.orderId(),
                    event.eventId(),
                    event.userId(),
                    item.sectorId(),
                    item.reservationId(),
                    item.ticketType(),
                    qrCodeHash,
                    TicketStatusEnum.VALID
                );

                ticketsToGenerate.add(ticket);
            }
        }

        List<Ticket> generatedTickets = ticketRepository.saveAll(ticketsToGenerate);

        ticketGeneratedPublisher.publish(
            toTicketGeneratedEvent(
                event,
                generatedTickets
            )
        );

        return generatedTickets;
    }

    public TicketResponseDTO findById(String id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new NotFoundException("Ticket não encontrado."));
        EventDetailsResponseDTO eventDetails = eventCatalogProxy.findEventById(ticket.getEventId());
        return toTicketResponseDTO(ticket, eventDetails);
    }

    public List<TicketResponseDTO> findByUserId(String userId) {
        Map<String, EventDetailsResponseDTO> eventsById = new HashMap<>();

        return ticketRepository.findByUserId(userId)
            .stream()
            .map(ticket -> {
                EventDetailsResponseDTO eventDetails = eventsById.computeIfAbsent(
                    ticket.getEventId(),
                    eventCatalogProxy::findEventById
                );

                return toTicketResponseDTO(ticket, eventDetails);
            })
            .toList();
    }

    public Page<TicketResponseDTO> findByEventId(String eventId, Pageable pageable) {
        EventDetailsResponseDTO eventDetails = eventCatalogProxy.findEventById(eventId);

        return ticketRepository.findByEventId(eventId, pageable).map(ticket -> toTicketResponseDTO(ticket, eventDetails));
    }

    @Transactional
    public void revokeTicket(String id) {
        Ticket ticket = ticketRepository.findById(id)
            .orElseThrow(
                () -> new NotFoundException("Ticket não encontrado.")
            );

        ticket.revokeTicket();
    }

    private TicketGeneratedEvent toTicketGeneratedEvent(
        OrderConfirmedEvent orderConfirmedEvent,
        List<Ticket> tickets
    ) {
        List<TicketGeneratedItemEvent> generatedItems = tickets.stream()
            .map(ticket -> new TicketGeneratedItemEvent(
                ticket.getId(),
                ticket.getSectorId(),
                ticket.getTicketType(),
                ticket.getQrCodeHash()
            ))
            .toList();

        return new TicketGeneratedEvent(
            UUID.randomUUID().toString(),
            orderConfirmedEvent.orderId(),
            orderConfirmedEvent.eventId(),
            orderConfirmedEvent.userId(),
            generatedItems,
            LocalDateTime.now()
        );
    }

    private String resolveSectorName(
        String sectorId,
        EventDetailsResponseDTO eventDetails
    ) {
        if (eventDetails.sectorsDetails() == null) {
            return "Setor não informado";
        }

        return eventDetails.sectorsDetails()
            .stream()
            .filter(sector -> sector.sectorId().equals(sectorId))
            .map(EventSectorDetailsDTO::sectorName)
            .findFirst()
            .orElse("Setor não informado");
    }

    private TicketResponseDTO toTicketResponseDTO(
        Ticket ticket,
        EventDetailsResponseDTO eventDetails
    ) {
        return new TicketResponseDTO(
            ticket.getId(),

            ticket.getOrderId(),
            ticket.getEventId(),
            eventDetails.title(),
            eventDetails.eventDate(),

            eventDetails.venueName(),
            eventDetails.venueCity(),
            eventDetails.venueState(),

            ticket.getUserId(),

            ticket.getSectorId(),
            resolveSectorName(ticket.getSectorId(), eventDetails),

            ticket.getReservationId(),
            ticket.getTicketType(),

            ticket.getQrCodeHash(),
            ticket.getStatus(),

            ticket.getCreatedAt(),
            ticket.getUsedAt()
        );
    }
}