// OrderDto.java
package hiorder.external;

import lombok.Data;
import java.util.Date;

@Data
public class OrderDto {
    private Long id;
    private Long tableId;
    private Date orderTime;
    private Integer menuId;
    private Integer quantity;
}
