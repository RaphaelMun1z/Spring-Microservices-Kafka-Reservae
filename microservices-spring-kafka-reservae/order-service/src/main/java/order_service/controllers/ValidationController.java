package order_service.controllers;

import order_service.controllers.contracts.ValidationContract;
import order_service.dtos.res.OrderResponseDTO;
import order_service.services.ValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order-service/api/orders/validate")
public class ValidationController implements ValidationContract {
    private final ValidationService validationService;

    public ValidationController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @Override
    @GetMapping("/v1/{orderId}/exists")
    public ResponseEntity<OrderResponseDTO> validateAndGetOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(validationService.validateAndGetOrder(orderId));
    }
}
