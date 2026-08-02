package payment_service.proxy.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import payment_service.proxy.order.config.OrderFeignConfig;
import payment_service.proxy.order.dtos.OrderResponseDTO;

@FeignClient(
    name = "order-service",
    configuration = OrderFeignConfig.class
)
public interface OrderProxy {
    @GetMapping("/order-service/api/orders/validate/v1/{orderId}/exists")
    OrderResponseDTO validateAndGetOrder(@PathVariable String orderId);
}
