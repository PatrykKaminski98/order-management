package pl.ordermanagement.application.order.api.port.out;

import java.util.List;

import pl.ordermanagement.application.order.domain.model.Order;
import pl.ordermanagement.application.shared.datatype.CustomerKey;

public interface FindOrdersByCustomerKeyPort {
    List<Order> find(CustomerKey customerKey);
}
