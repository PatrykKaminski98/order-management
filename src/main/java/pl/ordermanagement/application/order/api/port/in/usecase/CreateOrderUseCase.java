package pl.ordermanagement.application.order.api.port.in.usecase;


import java.util.List;

import pl.ordermanagement.application.order.domain.model.Order;
import pl.ordermanagement.application.shared.datatype.CustomerKey;
import pl.ordermanagement.application.shared.datatype.ProductIdentifier;

public interface CreateOrderUseCase {
    Order apply(Command command);

    record Command(CustomerKey customerKey, List<OrderItem> orderItems) {}

    record OrderItem(ProductIdentifier productIdentifier, int quantity) {}

}
