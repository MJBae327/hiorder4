package hiorder.external;

import lombok.Data;
import java.util.List;

@Data
public class MenuDto {

    private Long id;
    private String name;
    private Integer price;
    private List<Long> inventoryId; // 재고 ID 리스트
    private List<Integer> ingredientUnit; // 재료 단위 리스트
}
