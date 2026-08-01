package order_service.controllers;

import jakarta.validation.Valid;
import order_service.controllers.contracts.OrderContract;
import order_service.dtos.req.CheckoutRequestDTO;
import order_service.dtos.req.UpdateOrderStatusRequestDTO;
import order_service.dtos.res.OrderResponseDTO;
import order_service.dtos.res.OrderSummaryResponseDTO;
import order_service.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/order-service/api/orders")
public class OrderController implements OrderContract {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    @PostMapping("/v1/checkout")
    public ResponseEntity<OrderSummaryResponseDTO> checkout(
        @Valid @RequestBody CheckoutRequestDTO dto
    ) {
        OrderSummaryResponseDTO response = orderService.processCheckout(dto);

        URI location = URI.create(
            "/order-service/api/orders/v1/" + response.orderId()
        );

        return ResponseEntity
            .accepted()
            .location(location)
            .body(response);
    }

    @Override
    @GetMapping("/v1/{orderId}")
    public ResponseEntity<OrderResponseDTO> findOrderById(
        @PathVariable String orderId
    ) {
        return ResponseEntity.ok(
            orderService.findOrderById(orderId)
        );
    }

    @Override
    @GetMapping("/v1/event/{eventId}/orders")
    public ResponseEntity<List<OrderSummaryResponseDTO>> findOrdersByEventId(
        @PathVariable String eventId
    ) {
        return ResponseEntity.ok(
            orderService.findOrdersByEventId(eventId)
        );
    }

    @Override
    @GetMapping("/v1/reservation/{reservationId}/order")
    public ResponseEntity<OrderSummaryResponseDTO> findOrderByReservationId(
        @PathVariable String reservationId
    ) {
        return ResponseEntity.ok(
            orderService.findOrderByReservationId(reservationId)
        );
    }

    @Override
    @PatchMapping("/v1/{orderId}/status")
    public ResponseEntity<OrderSummaryResponseDTO> updateOrderStatus(
        @PathVariable String orderId,
        @Valid @RequestBody UpdateOrderStatusRequestDTO request
    ) {
        return ResponseEntity.ok(
            orderService.updateOrderStatus(
                orderId,
                request.status()
            )
        );
    }

    @Override
    @GetMapping("/v1/user/{userId}/orders")
    public ResponseEntity<List<OrderResponseDTO>> findOrdersByUserId(
        @PathVariable String userId
    ) {
        return ResponseEntity.ok(
            orderService.findOrdersByUserId(userId)
        );
    }
}