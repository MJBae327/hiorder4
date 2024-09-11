package hiorder.external;

import java.util.Date;
import lombok.Data;

@Data
public class Menu {

    private Long id;
    private String name;
    private Integer price;
    private Object inventoryId;
    private Object ingredientUnit;
}
