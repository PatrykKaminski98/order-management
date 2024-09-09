package pl.ordermanagement.application.order.domain;

import static java.lang.String.format;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ordermanagement.application.order.api.exception.OrderNotFoundException;
import pl.ordermanagement.application.order.api.port.in.query.GetOrderDetailsQuery;
import pl.ordermanagement.application.order.api.port.out.FindOrderByOrderKeyPort;
import pl.ordermanagement.application.order.domain.model.Order;
import pl.ordermanagement.application.order.domain.model.OrderItem;
import pl.ordermanagement.application.product.api.GetProductDetailsApi;
import pl.ordermanagement.application.shared.datatype.OrderKey;
import pl.ordermanagement.application.shared.datatype.ProductIdentifier;

@Service
@RequiredArgsConstructor
class GetOrderDetailsService implements GetOrderDetailsQuery {
    private static final String PRODUCT_DOES_NOT_EXIST_EXCEPTION_MESSAGE = "Product with identifier: %s does not exist.";
    private final FindOrderByOrderKeyPort findOrderByOrderKeyPort;
    private final GetProductDetailsApi getProductDetailsApi;

    @Override
    @Transactional(readOnly = true)
    public QueryResult execute(@NonNull OrderKey orderKey) {
        Order order = findOrderByOrderKeyPort.findByOrderKey(orderKey).orElseThrow(() -> new OrderNotFoundException(orderKey));
        List<OrderItemDetails> orderItemDetails = createOrderItemDetailsList(order.getOrderItems());
        BigDecimal totalPrice = calculateTotalPrice(orderItemDetails);
        return new QueryResult(order.getOrderKey(), order.getCustomerKey(),order.getStatus(), orderItemDetails, totalPrice);
    }


    private List<OrderItemDetails> createOrderItemDetailsList(List<OrderItem> orderItems) {
        Map<ProductIdentifier, GetProductDetailsApi.ProductDetails> productDetailsMap = fetchProductDetails(orderItems);
        ArrayList<OrderItemDetails> result = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            if(productDetailsMap.containsKey(orderItem.getProductIdentifier())) {
                GetProductDetailsApi.ProductDetails productDetails = productDetailsMap.get(orderItem.getProductIdentifier());
                OrderItemDetails orderItemDetails = new OrderItemDetails(productDetails.name(), productDetails.producer(), productDetails.price(), orderItem.getQuantity());
                result.add(orderItemDetails);
            } else{
                throw new IllegalStateException(format(PRODUCT_DOES_NOT_EXIST_EXCEPTION_MESSAGE, orderItem.getProductIdentifier().getValue()));
            }
        }
        return result;
    }

    private Map<ProductIdentifier, GetProductDetailsApi.ProductDetails> fetchProductDetails(List<OrderItem> orderItems) {
        Set<ProductIdentifier> productIdentifiers = getProductIdentifiers(orderItems);
        return getProductDetailsApi.getProductDetailsMap(productIdentifiers);
    }

    private BigDecimal calculateTotalPrice(List<OrderItemDetails> orderItemDetails) {
        return orderItemDetails.stream()
                .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Set<ProductIdentifier> getProductIdentifiers(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::getProductIdentifier)
                .collect(Collectors.toSet());
    }
}
