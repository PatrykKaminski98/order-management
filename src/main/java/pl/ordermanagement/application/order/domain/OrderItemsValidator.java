package pl.ordermanagement.application.order.domain;

import java.util.List;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ordermanagement.application.order.domain.model.OrderItem;
import pl.ordermanagement.application.product.api.ConfirmProductsAvailabilityApi;

@Service
@RequiredArgsConstructor
class OrderItemsValidator {
    private static final String EMPTY_ORDER_ITEMS_EXCEPTION_MESSAGE = "Order item list cannot be empty";
    private static final String NEGATIVE_QUANTITY_EXCEPTION_MESSAGE = "The quantity in an order item cannot be negative.";
    private final ConfirmProductsAvailabilityApi confirmProductsAvailabilityApi;
    void validate(List<OrderItem> orderItems){
        if(orderItems.isEmpty()){
            throw new IllegalArgumentException(EMPTY_ORDER_ITEMS_EXCEPTION_MESSAGE);
        }
        boolean hasNegativeQuantity = orderItems.stream().map(OrderItem::getQuantity).anyMatch(quantity -> quantity < 0);
        if(hasNegativeQuantity){
            throw new IllegalArgumentException(NEGATIVE_QUANTITY_EXCEPTION_MESSAGE);
        }
        List<ConfirmProductsAvailabilityApi.OrderItem> list = orderItems.stream().map(this::mapToOrderItem).toList();
        confirmProductsAvailabilityApi.confirmProductsAvailability(list);
    }

    private ConfirmProductsAvailabilityApi.OrderItem mapToOrderItem(OrderItem orderItem){
        return new ConfirmProductsAvailabilityApi.OrderItem(orderItem.getProductIdentifier(), orderItem.getQuantity());
    }
}
