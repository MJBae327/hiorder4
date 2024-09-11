package hiorder.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import hiorder.config.kafka.KafkaProcessor;
import hiorder.domain.*;
import hiorder.external.MenuService;
import hiorder.external.MenuDto;
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
    public void wheneverOrderCreated_DecreaseStock(
        @Payload OrderCreated orderCreated
    ) {
        System.out.println("\n\n##### listener DecreaseStock : " + orderCreated + "\n\n");

        // Menu 정보 가져오기
        MenuDto menu = menuService.getMenu(Long.valueOf(orderCreated.getMenuId()));

        // Inventory.decreaseStock 호출, 재고 감소 처리
        Inventory.decreaseStock(orderCreated, menu.getInventoryId(), menu.getIngredientUnit());
    }
}
//>>> Clean Arch / Inbound Adaptor

