package hiorder.domain;

import hiorder.domain.*;
import hiorder.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class StockCreated extends AbstractEvent {

    private Long id;
    private String name;
    private Integer quantity;

    public StockCreated(Inventory aggregate) {
        super(aggregate);
    }

    public StockCreated() {
        super();
    }
}
//>>> DDD / Domain Event
