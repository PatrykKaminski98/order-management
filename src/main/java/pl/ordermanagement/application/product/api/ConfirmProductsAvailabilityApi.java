package pl.ordermanagement.application.product.api;

import java.util.List;

import pl.ordermanagement.application.shared.datatype.ProductIdentifier;

public interface ConfirmProductsAvailabilityApi {
    void confirmProductsAvailability(List<OrderItem> orderItems);

    record OrderItem(ProductIdentifier productIdentifier, int quantity){}
}
