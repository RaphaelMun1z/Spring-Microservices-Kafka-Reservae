package inventory_service.proxy.order;

import inventory_service.proxy.order.config.OrderFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "order-service",
    configuration = OrderFeignConfig.class
)
public interface OrderProxy {
    @GetMapping("/order-service/api/orders/validate/v1/{orderId}/exists")
    void validateOrder(@PathVariable String orderId);
}
