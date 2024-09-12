package hiorder.domain;

import hiorder.domain.*;
import hiorder.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class OrderCreated2 extends AbstractEvent {

    private Long id;
    private Long tableId;
    private Date orderTime;
    private Integer menuId;
    private Integer quantity;
}
