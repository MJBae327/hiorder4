package hiorder.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import hiorder.config.kafka.KafkaProcessor;
import hiorder.domain.Inventory;
import hiorder.domain.InventoryRepository;
import hiorder.domain.OrderCreated;
import hiorder.external.MenuDto;
import hiorder.external.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    MenuService menuService;  // MenuService를 주입 받아서 메뉴 정보를 가져옴

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='OrderCreated'"
    )
    public void wheneverOrderCreated_DecreaseStock(@Payload OrderCreated orderCreated) {
        System.out.println("\n\n##### listener DecreaseStock : " + orderCreated + "\n\n");

        // OrderCreated 이벤트에서 받은 menuId를 사용하여 Menu 정보를 가져옴
        MenuDto menu = menuService.getMenu(Long.valueOf(orderCreated.getMenuId()));

        if (menu != null) {
            // Inventory의 재고를 감소시키는 메서드 호출
            Inventory.decreaseStock(orderCreated, menu.getInventoryId(), menu.getIngredientUnit());
        } else {
            System.out.println("Menu information could not be retrieved for MenuId: " + orderCreated.getMenuId());
        }
    }
}
//>>> Clean Arch / Inbound Adaptor

