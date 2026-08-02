package order_service.services;

import order_service.dtos.res.OrderResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {
    private final OrderService orderService;

    public ValidationService(OrderService orderService) {
        this.orderService = orderService;
    }

    public OrderResponseDTO validateAndGetOrder(String orderId) {
        return orderService.findOrderById(orderId);
    }
}
