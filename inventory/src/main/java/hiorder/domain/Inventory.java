package hiorder.domain;

import hiorder.InventoryApplication;
import hiorder.external.OrderDto;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import java.util.Optional;

@Entity
@Table(name = "Inventory_table")
@Data
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Integer quantity;

    public static InventoryRepository repository() {
        InventoryRepository inventoryRepository = InventoryApplication.applicationContext.getBean(
            InventoryRepository.class
        );
        return inventoryRepository;
    }

    //<<< Clean Arch / Port Method
    public static void decreaseStock(OrderDto orderDto, List<Long> inventoryIds, List<Integer> ingredientUnits) {
        for (int i = 0; i < inventoryIds.size(); i++) {
            Long inventoryId = inventoryIds.get(i);
            Integer ingredientUnit = ingredientUnits.get(i);
            
            // 해당 inventoryId에 해당하는 재고를 찾음
            Optional<Inventory> optionalInventory = repository().findById(inventoryId);
            
            if (!optionalInventory.isPresent()) {
                // 재고가 없는 경우 처리
                System.out.println("Error: Inventory with ID " + inventoryId + " not found.");
                return;
            }

            Inventory inventory = optionalInventory.get();
            
            // 재고량이 부족한 경우
            if (inventory.getQuantity() < ingredientUnit * orderDto.getQuantity()) {
                System.out.println("Error: Not enough stock for Inventory ID " + inventoryId + ". Available: " + inventory.getQuantity() + ", Required: " + (ingredientUnit * orderDto.getQuantity()));
                return;
            }

            // 재고를 감소시킴
            inventory.setQuantity(inventory.getQuantity() - ingredientUnit * orderDto.getQuantity());

            // 재고가 감소한 후 처리
            if (inventory.getQuantity() <= 0) {
                inventory.setQuantity(0);  // 재고가 음수로 내려가는 것을 방지
                // OutOfStock 이벤트 발행
            }

            // 변경된 재고를 저장
            repository().save(inventory);

            // StockDecreased 이벤트 발행
        }
    }
    //>>> Clean Arch / Port Method
}
