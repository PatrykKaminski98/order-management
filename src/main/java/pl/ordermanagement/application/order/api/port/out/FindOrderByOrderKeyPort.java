package pl.ordermanagement.application.order.api.port.out;

import java.util.Optional;

import pl.ordermanagement.application.order.domain.model.Order;
import pl.ordermanagement.application.shared.datatype.OrderKey;

public interface FindOrderByOrderKeyPort {
    Optional<Order> findByOrderKey(OrderKey orderKey);

}
