package pl.ordermanagement.application.product.domain;

import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ordermanagement.application.product.api.ConfirmProductsAvailabilityApi;
import pl.ordermanagement.application.product.api.exception.ProductNotFoundException;
import pl.ordermanagement.application.product.api.port.out.FindProductsByProductIdentifiersPort;
import pl.ordermanagement.application.product.domain.model.Product;
import pl.ordermanagement.application.shared.datatype.ProductIdentifier;

@Service
@RequiredArgsConstructor
class ConfirmProductsAvailabilityService implements ConfirmProductsAvailabilityApi {
    private static final String OUT_OF_STOCK_EXCEPTION_MESSAGE = "The indicated quantity for product identifier: %s is out of stock. Ordered: %s. Available: %s";
    private final FindProductsByProductIdentifiersPort findProductsByProductIdentifiersPort;


    @Override
    public void confirmProductsAvailability(List<OrderItem> orderItems) {
        Set<ProductIdentifier> productIdentifiers =
                orderItems.stream().map(OrderItem::productIdentifier).collect(Collectors.toSet());
        Map<ProductIdentifier, Integer> productIdentifierToAvailableQuantityMap =
                findProductsByProductIdentifiersPort.findProducts(productIdentifiers).stream()
                        .collect(toMap(Product::getProductIdentifier, Product::getQuantityAvailable));
        for (OrderItem orderItem : orderItems) {
            if(productIdentifierToAvailableQuantityMap.containsKey(orderItem.productIdentifier())){
                Integer availableQuantity = productIdentifierToAvailableQuantityMap.get(orderItem.productIdentifier());
                if (availableQuantity < orderItem.quantity()){
                    throw new IllegalArgumentException(format(OUT_OF_STOCK_EXCEPTION_MESSAGE, orderItem.productIdentifier().getValue(), orderItem.quantity(), availableQuantity));
                }
            } else {
                throw new ProductNotFoundException(orderItem.productIdentifier());

            }
        }
    }
}
