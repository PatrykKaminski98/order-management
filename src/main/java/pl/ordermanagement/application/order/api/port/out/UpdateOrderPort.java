package pl.ordermanagement.application.order.api.port.out;

import pl.ordermanagement.application.order.domain.model.Order;

public interface UpdateOrderPort {
    Order update(Order order);
}
