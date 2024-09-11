package hiorder.domain;

import hiorder.domain.*;
import hiorder.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class OutOfStock extends AbstractEvent {

    private Long id;
}
