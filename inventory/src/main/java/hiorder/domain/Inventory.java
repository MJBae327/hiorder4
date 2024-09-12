package hiorder.domain;

import hiorder.InventoryApplication;
import hiorder.domain.OutOfStock;
import hiorder.domain.StockDecreased;
import hiorder.domain.StockCreated;
import hiorder.domain.StockDeleted;
import hiorder.domain.StockUpdated;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import java.util.Optional; 

@Entity
@Table(name = "Inventory_table")
@Data
//<<< DDD / Aggregate Root
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Integer quantity;

    @PostPersist
    public void onPostPersist() {
        StockCreated stockCreated = new StockCreated(this);
        stockCreated.publishAfterCommit();

        if (this.quantity <= 0) {
            OutOfStock outOfStock = new OutOfStock(this);
            outOfStock.publishAfterCommit();
        }
    }

    @PostUpdate
    public void onPostUpdate() {
        StockUpdated stockUpdated = new StockUpdated(this);
        stockUpdated.publishAfterCommit();

        if (this.quantity <= 0) {
            OutOfStock outOfStock = new OutOfStock(this);
            outOfStock.publishAfterCommit();
        }
    }

    @PreRemove
    public void onPreRemove() {
        StockDeleted stockDeleted = new StockDeleted(this);
        stockDeleted.publishAfterCommit();
    }

    public static InventoryRepository repository() {
        InventoryRepository inventoryRepository = InventoryApplication.applicationContext.getBean(
            InventoryRepository.class
        );
        return inventoryRepository;
    }

    //<<< Clean Arch / Port Method
    public static void decreaseStock(OrderCreated orderCreated, List<Long> inventoryIds, List<Integer> ingredientUnits) {
        for (int i = 0; i < inventoryIds.size(); i++) {
            Long inventoryId = inventoryIds.get(i);
            Integer ingredientUnit = ingredientUnits.get(i);
            
            // 해당 inventoryId에 해당하는 재고를 찾음
            Optional<Inventory> optionalInventory = repository().findById(inventoryId);
            
            if (!optionalInventory.isPresent()) {
                // 재고가 없는 경우 처리
                System.out.println("Error: Inventory with ID " + inventoryId + " not found.");
                // 여기서 에러 메시지를 반환하거나 이벤트 발행 가능
                return; // 혹은 계속 진행할지 여부에 따라 처리
            }
    
            Inventory inventory = optionalInventory.get();
            
            // 재고량이 부족한 경우
            if (inventory.getQuantity() < ingredientUnit * orderCreated.getQuantity()) {
                System.out.println("Error: Not enough stock for Inventory ID " + inventoryId + ". Available: " + inventory.getQuantity() + ", Required: " + (ingredientUnit * orderCreated.getQuantity()));
                // 재고 부족 메시지 처리 및 이벤트 발행
                return; // 부족한 경우 로직을 중단할지 계속할지 선택 가능
            }
    
            // 재고를 감소시킴
            inventory.setQuantity(inventory.getQuantity() - ingredientUnit * orderCreated.getQuantity());
    
            // 재고가 감소한 후 처리
            if (inventory.getQuantity() <= 0) {
                inventory.setQuantity(0);  // 재고가 음수로 내려가는 것을 방지
                OutOfStock outOfStock = new OutOfStock(inventory);
                outOfStock.publishAfterCommit();
            }
    
            // 변경된 재고를 저장
            repository().save(inventory);
    
            // 재고 감소 이벤트 발행
            StockDecreased stockDecreased = new StockDecreased(inventory);
            stockDecreased.publishAfterCommit();
        }
    }
    
    
    //>>> Clean Arch / Port Method
}
//>>> DDD / Aggregate Root
