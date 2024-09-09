package pl.ordermanagement.application.order.api.port.out;

import java.util.Collection;
import java.util.List;

import pl.ordermanagement.application.order.domain.model.OrderItem;

public interface SaveOrderItemsPort {
    List<OrderItem> saveOrderItems(Collection<OrderItem> orderItems);
}
