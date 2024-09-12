package hiorder.infra;

import hiorder.config.kafka.KafkaProcessor;
import hiorder.domain.Inventory;
import hiorder.external.MenuDto;
import hiorder.external.MenuService;
import hiorder.external.OrderDto;
import hiorder.external.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PolicyHandler {

    @Autowired
    MenuService menuService;

    @Autowired
    OrderService orderService;  // OrderService 주입

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='OrderCreated'"
    )
    public void wheneverOrderCreated_DecreaseStock(@Payload String eventString) {
        // OrderCreated 이벤트가 발생했을 때, OrderDto를 OrderService를 통해 가져옴
        OrderDto orderDto = orderService.getOrder(Long.valueOf(eventString));

        if (orderDto != null) {
            System.out.println("\n\n##### listener DecreaseStock : " + orderDto + "\n\n");

            // OrderDto에서 메뉴 정보를 가져옴
            MenuDto menu = menuService.getMenu(Long.valueOf(orderDto.getMenuId()));

            if (menu != null) {
                List<Long> inventoryIds = menu.getInventoryId();
                List<Integer> ingredientUnits = menu.getIngredientUnit();

                // Inventory의 재고를 감소시키는 메서드 호출
                if (inventoryIds != null && ingredientUnits != null) {
                    Inventory.decreaseStock(orderDto, inventoryIds, ingredientUnits);
                } else {
                    System.out.println("Error: Menu does not have valid inventory or ingredient data.");
                }
            } else {
                System.out.println("Error: Menu information could not be retrieved for MenuId: " + orderDto.getMenuId());
            }
        } else {
            System.out.println("Error: Order information could not be retrieved for eventString: " + eventString);
        }
    }
}


