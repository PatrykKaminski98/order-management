package pl.ordermanagement.application.order.domain;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ordermanagement.application.order.api.port.in.usecase.CreateOrderUseCase;
import pl.ordermanagement.application.order.api.port.out.CreateOrderPort;
import pl.ordermanagement.application.order.domain.model.Order;
import pl.ordermanagement.application.shared.datatype.OrderKey;
import pl.ordermanagement.application.shared.datatype.Status;

@Service
@RequiredArgsConstructor
class CreateOrderService implements CreateOrderUseCase {
    private final CreateOrderPort createOrderPort;
    private final OrderItemsValidator orderItemsValidator;

    @Override
    @Transactional
    public Order apply(Command command) {
        List<pl.ordermanagement.application.order.domain.model.OrderItem> orderItems = command.orderItems().stream()
                .map(this::mapOrderItem)
                .toList();
        orderItemsValidator.validate(orderItems);
        Order order = new Order(
                generateOrderKey(),
                command.customerKey(),
                Status.CREATED,
                orderItems);
        return createOrderPort.create(order);
    }

    private OrderKey generateOrderKey(){
        return new OrderKey(UUID.randomUUID().toString());
    }

    private pl.ordermanagement.application.order.domain.model.OrderItem mapOrderItem(OrderItem orderItem){
        return new pl.ordermanagement.application.order.domain.model.OrderItem(orderItem.productIdentifier(), orderItem.quantity());
    }
}
