package pl.ordermanagement.application.order.domain.model;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import pl.ordermanagement.application.shared.datatype.AbstractAggregate;
import pl.ordermanagement.application.shared.datatype.CustomerKey;
import pl.ordermanagement.application.shared.datatype.OrderKey;
import pl.ordermanagement.application.shared.datatype.Status;

@Getter
@Setter
@AllArgsConstructor
public class Order extends AbstractAggregate {
    @NonNull
    private OrderKey orderKey;
    @NonNull
    private CustomerKey customerKey;
    @NonNull
    Status status;
    private List<OrderItem> orderItems;

}
