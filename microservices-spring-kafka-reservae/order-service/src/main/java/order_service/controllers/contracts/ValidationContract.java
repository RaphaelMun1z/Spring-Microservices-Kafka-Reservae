package order_service.controllers.contracts;

import order_service.dtos.res.OrderResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface ValidationContract {
    ResponseEntity<OrderResponseDTO> validateAndGetOrder(@PathVariable String orderId);
}
