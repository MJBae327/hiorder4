package hiorder.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="order", url="${api.url.order}")
public interface OrderService {
    @GetMapping("/orders/{id}")
    OrderDto getOrder(@PathVariable("id") Long id);
}

