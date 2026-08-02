package event_catalog_service.controllers.contracts;

import event_catalog_service.dtos.req.CreateEventRequestDTO;
import event_catalog_service.dtos.req.EventFilterRequestDTO;
import event_catalog_service.dtos.req.SectorPricingRequestDTO;
import event_catalog_service.dtos.req.SectorsTicketPriceRequestDTO;
import event_catalog_service.dtos.res.EventDetailsResponseDTO;
import event_catalog_service.dtos.res.EventSectorPriceResponseDTO;
import event_catalog_service.dtos.res.EventSummaryResponseDTO;
import event_catalog_service.dtos.res.SectorPricingResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Event Catalog Endpoint", description = "Gerenciamento do catálogo de eventos e configuração de setores/preços")
public interface EventCatalogContract {
    @Operation(
        summary = "Buscar eventos",
        description = """
            Retorna uma lista paginada de eventos.
            
            Todos os filtros são opcionais. A pesquisa textual é realizada
            sobre o título do evento.
            """
    )
    ResponseEntity<Page<EventSummaryResponseDTO>> findEvents(
        @ParameterObject EventFilterRequestDTO filter,
        @ParameterObject Pageable pageable
    );

    @Operation(summary = "Buscar os detalhes completos de um evento através do seu ID")
    ResponseEntity<EventDetailsResponseDTO> findEventById(
        @Parameter(description = "ID do evento")
        String id
    );

    @Operation(summary = "Registrar um novo evento no catálogo")
    ResponseEntity<EventDetailsResponseDTO> createEvent(
        CreateEventRequestDTO dto
    );

    @Operation(summary = "Adicionar um novo setor (e sua precificação) a um evento existente")
    ResponseEntity<SectorPricingResponseDTO> addSectorToAnEvent(
        @Parameter(description = "ID do evento")
        String id,

        SectorPricingRequestDTO dto
    );

    @Operation(summary = "Remover um setor específico de um evento")
    ResponseEntity<Void> removeSectorFromAnEvent(
        @Parameter(description = "ID do evento")
        String id,

        @Parameter(description = "ID do setor")
        String secId
    );

    @Operation(
        summary = "Consultar os preços dos setores de um evento"
    )
    ResponseEntity<List<EventSectorPriceResponseDTO>> consultTicketsPrice(@Valid @RequestBody SectorsTicketPriceRequestDTO sectorsTicketPriceRequestDTO);
}