package inventory_service.services;

import inventory_service.dtos.res.EventSectorInventoryResponseDTO;
import inventory_service.dtos.res.ReservationStatusResponseDTO;
import inventory_service.dtos.res.TicketReservationResponseDTO;
import inventory_service.entities.EventSectorInventory;
import inventory_service.entities.TicketReservation;
import inventory_service.entities.enums.ReservationStatusEnum;
import inventory_service.environment.InstanceInformationService;
import inventory_service.exceptions.models.BusinessException;
import inventory_service.exceptions.models.NotFoundException;
import inventory_service.proxy.eventCatalog.EventCatalogProxy;
import inventory_service.proxy.order.OrderProxy;
import inventory_service.repositories.EventSectorInventoryRepository;
import inventory_service.repositories.TicketReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InventoryManagementService {
    private final Logger logger = LoggerFactory.getLogger(InventoryManagementService.class);

    private final long reservationTtlMinutes;

    private final InstanceInformationService informationService;
    private final EventSectorInventoryRepository eventSectorInventoryRepository;
    private final TicketReservationRepository ticketReservationRepository;
    private final EventCatalogProxy eventCatalogProxy;
    private final OrderProxy orderProxy;

    public InventoryManagementService(
        InstanceInformationService informationService,
        EventSectorInventoryRepository eventSectorInventoryRepository,
        TicketReservationRepository ticketReservationRepository,
        EventCatalogProxy eventCatalogProxy,
        OrderProxy orderProxy,
        @Value("${reservae.config.reservation.ttl-minutes}") long reservationTtlMinutes
    ) {
        this.informationService = informationService;
        this.eventSectorInventoryRepository = eventSectorInventoryRepository;
        this.ticketReservationRepository = ticketReservationRepository;
        this.eventCatalogProxy = eventCatalogProxy;
        this.orderProxy = orderProxy;
        this.reservationTtlMinutes = reservationTtlMinutes;
    }

    @Transactional
    public EventSectorInventoryResponseDTO createEventSectorInventory(
        String eventId,
        String sectorId,
        int capacity
    ) {
        // Validar existência de Evento/Setor
        eventCatalogProxy.validateEventSector(
            eventId,
            sectorId
        );

        if (capacity <= 0) {
            throw new BusinessException("A capacidade do setor deve ser maior que zero.");
        }

        String eventCatalogServicePort = eventCatalogProxy.validateSectorCapacity(
            eventId,
            sectorId,
            capacity
        );

        String inventoryId = EventSectorInventory.buildId(eventId, sectorId);

        if (eventSectorInventoryRepository.existsById(inventoryId)) {
            throw new BusinessException("O inventário deste evento/setor já existe.");
        }

        EventSectorInventory inventory = new EventSectorInventory(
            eventId,
            sectorId,
            capacity
        );

        EventSectorInventory savedInventory = eventSectorInventoryRepository.save(inventory);

        logger.info(
            "Inventário criado para eventId={}, sectorId={}, capacity={}. {}",
            eventId,
            sectorId,
            capacity,
            eventCatalogServicePort
        );

        return toInventoryResponse(savedInventory);
    }

    @Transactional
    public TicketReservationResponseDTO reserveTickets(
        String orderId,
        String userId,
        String eventId,
        String sectorId,
        int quantity
    ) {
        validateReservationRequest(
            orderId,
            userId,
            eventId,
            sectorId,
            quantity
        );

        String eventCatalogServicePort = eventCatalogProxy.validateEventSector(
            eventId,
            sectorId
        );

        String inventoryId = EventSectorInventory.buildId(eventId, sectorId);

        EventSectorInventory inventory = eventSectorInventoryRepository.findById(inventoryId)
            .orElseThrow(() -> new NotFoundException("Inventário do setor não encontrado."));

        inventory.reserve(quantity);

        TicketReservation reservation = new TicketReservation(
            eventId,
            sectorId,
            userId,
            orderId,
            quantity,
            Duration.ofMinutes(reservationTtlMinutes).toSeconds()
        );

        eventSectorInventoryRepository.save(inventory);
        TicketReservation savedReservation = ticketReservationRepository.save(reservation);

        logger.info(
            "Reserva criada. Pedido: {}. Usuário: {}. Evento: {}. Setor: {}. Quantidade: {}. Reserva: {}. {}",
            orderId,
            userId,
            eventId,
            sectorId,
            quantity,
            savedReservation.getReservationId(),
            eventCatalogServicePort
        );

        return toReservationResponse(savedReservation);
    }

    @Transactional
    public void confirmReservationSale(String reservationId) {
        TicketReservation reservation = findReservationEntityById(reservationId);

        if (!ReservationStatusEnum.RESERVED.equals(reservation.getReservationStatus())) {
            throw new BusinessException("A reserva não está pendente para confirmação.");
        }

        EventSectorInventory inventory = findInventoryEntityByEventAndSector(
            reservation.getEventId(),
            reservation.getSectorId()
        );

        inventory.confirmSale(reservation.getQuantity());
        reservation.confirm();

        eventSectorInventoryRepository.save(inventory);
        ticketReservationRepository.save(reservation);

        logger.info(
            "Reserva confirmada como venda. Reserva: {}. Pedido: {}. Quantidade: {}.",
            reservation.getReservationId(),
            reservation.getOrderId(),
            reservation.getQuantity()
        );
    }

    @Transactional
    public void confirmOrderReservations(String orderId) {
        List<TicketReservation> reservations = ticketReservationRepository.findByOrderId(orderId);

        if (reservations.isEmpty()) {
            throw new NotFoundException("Nenhuma reserva encontrada para o pedido.");
        }

        for (TicketReservation reservation : reservations) {
            if (ReservationStatusEnum.RESERVED.equals(reservation.getReservationStatus())) {
                confirmReservationSale(reservation.getReservationId());
            }
        }
    }

    @Transactional
    public void releaseReservation(String reservationId) {
        TicketReservation reservation = findReservationEntityById(reservationId);

        if (!ReservationStatusEnum.RESERVED.equals(reservation.getReservationStatus())) {
            logger.info(
                "Reserva {} não será liberada porque está com status {}.",
                reservation.getReservationId(),
                reservation.getReservationStatus()
            );
            return;
        }

        EventSectorInventory inventory = findInventoryEntityByEventAndSector(
            reservation.getEventId(),
            reservation.getSectorId()
        );

        inventory.releaseReservation(reservation.getQuantity());
        reservation.cancel();

        eventSectorInventoryRepository.save(inventory);
        ticketReservationRepository.save(reservation);

        logger.info(
            "Reserva liberada. Reserva: {}. Pedido: {}. Quantidade: {}.",
            reservation.getReservationId(),
            reservation.getOrderId(),
            reservation.getQuantity()
        );
    }

    @Transactional
    public void releaseOrderReservations(String orderId) {
        List<TicketReservation> reservations = ticketReservationRepository.findByOrderId(orderId);

        if (reservations.isEmpty()) {
            throw new NotFoundException("Nenhuma reserva encontrada para o pedido.");
        }

        for (TicketReservation reservation : reservations) {
            if (ReservationStatusEnum.RESERVED.equals(reservation.getReservationStatus())) {
                releaseReservation(reservation.getReservationId());
            }
        }
    }

    public ReservationStatusResponseDTO consultReservationStatus(String reservationId) {
        TicketReservation reservation = findReservationEntityById(reservationId);

        return new ReservationStatusResponseDTO(
            reservation.getReservationId(),
            reservation.getReservationStatus(),
            calculateExpirationDate(reservation),
            getEnvironment()
        );
    }

    public List<ReservationStatusResponseDTO> findUserReservations(String userId) {
        return ticketReservationRepository.findByUserId(userId)
            .stream()
            .map(reservation -> new ReservationStatusResponseDTO(
                reservation.getReservationId(),
                reservation.getReservationStatus(),
                calculateExpirationDate(reservation),
                getEnvironment()
            ))
            .toList();
    }

    public List<TicketReservationResponseDTO> findOrderReservations(String orderId) {
        return ticketReservationRepository.findByOrderId(orderId)
            .stream()
            .map(this::toReservationResponse)
            .toList();
    }

    public TicketReservationResponseDTO findReservationById(String reservationId) {
        TicketReservation reservation = findReservationEntityById(reservationId);

        return toReservationResponse(reservation);
    }

    public EventSectorInventoryResponseDTO findInventoryByEventAndSector(
        String eventId,
        String sectorId
    ) {
        EventSectorInventory inventory = findInventoryEntityByEventAndSector(
            eventId,
            sectorId
        );

        return toInventoryResponse(inventory);
    }

    private TicketReservation findReservationEntityById(String reservationId) {
        return ticketReservationRepository.findById(reservationId)
            .orElseThrow(() -> new NotFoundException("Reserva não encontrada."));
    }

    private EventSectorInventory findInventoryEntityByEventAndSector(
        String eventId,
        String sectorId
    ) {
        String inventoryId = EventSectorInventory.buildId(eventId, sectorId);

        return eventSectorInventoryRepository.findById(inventoryId)
            .orElseThrow(() -> new NotFoundException("Inventário do setor não encontrado."));
    }

    private TicketReservationResponseDTO toReservationResponse(TicketReservation reservation) {
        return new TicketReservationResponseDTO(
            reservation.getReservationId(),
            reservation.getOrderId(),
            reservation.getUserId(),
            reservation.getEventId(),
            reservation.getSectorId(),
            reservation.getQuantity(),
            reservation.getReservationStatus(),
            getEnvironment()
        );
    }

    private EventSectorInventoryResponseDTO toInventoryResponse(EventSectorInventory inventory) {
        return new EventSectorInventoryResponseDTO(
            inventory.getId(),
            inventory.getEventId(),
            inventory.getSectorId(),
            inventory.getCapacity(),
            inventory.getReservedQuantity(),
            inventory.getSoldQuantity(),
            inventory.getAvailableTicketsAmount(),
            getEnvironment()
        );
    }

    private LocalDateTime calculateExpirationDate(TicketReservation reservation) {
        if (!ReservationStatusEnum.RESERVED.equals(reservation.getReservationStatus())) {
            return null;
        }

        Long timeToLive = reservation.getTimeToLive();

        if (timeToLive == null || timeToLive < 0) {
            return null;
        }

        return LocalDateTime.now().plusSeconds(timeToLive);
    }

    private String getEnvironment() {
        return "PORT [INVENTORY]: " + informationService.retrieveServerPort();
    }

    private void validateReservationRequest(
        String orderId,
        String userId,
        String eventId,
        String sectorId,
        int quantity
    ) {
        if (orderId == null || orderId.isBlank()) {
            throw new BusinessException("O ID do pedido é obrigatório.");
        }

        // Validar se Pedido existe
        orderProxy.validateOrder(orderId);

        if (userId == null || userId.isBlank()) {
            throw new BusinessException("O ID do usuário é obrigatório.");
        }

        if (eventId == null || eventId.isBlank()) {
            throw new BusinessException("O ID do evento é obrigatório.");
        }

        if (sectorId == null || sectorId.isBlank()) {
            throw new BusinessException("O ID do setor é obrigatório.");
        }

        if (quantity <= 0) {
            throw new BusinessException("A quantidade de ingressos deve ser maior que zero.");
        }
    }
}