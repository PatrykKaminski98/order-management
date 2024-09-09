package pl.ordermanagement.application.order.domain;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ordermanagement.application.order.api.exception.OrderNotFoundException;
import pl.ordermanagement.application.order.api.port.in.query.GetOrderDetailsQuery;
import pl.ordermanagement.application.order.api.port.out.FindOrderByOrderKeyPort;
import pl.ordermanagement.application.order.domain.model.Order;
import pl.ordermanagement.application.order.domain.model.OrderItem;
import pl.ordermanagement.application.product.api.GetProductDetailsApi;
import pl.ordermanagement.application.shared.datatype.CustomerKey;
import pl.ordermanagement.application.shared.datatype.OrderKey;
import pl.ordermanagement.application.shared.datatype.ProductIdentifier;
import pl.ordermanagement.application.shared.datatype.Status;

@ExtendWith(MockitoExtension.class)
class GetOrderDetailsServiceTest {

    private static final String PRODUCT_DOES_NOT_EXIST_EXCEPTION_MESSAGE = "Product with identifier: %s does not exist.";
    private static final OrderKey ORDER_KEY = new OrderKey(UUID.randomUUID().toString());
    private static final CustomerKey CUSTOMER_KEY = new CustomerKey("KAMIN12");
    private static final Status STATUS = Status.CONFIRMED;
    private static final int QUANTITY = 5;
    private static final ProductIdentifier PRODUCT_IDENTIFIER = new ProductIdentifier(UUID.randomUUID().toString());
    private static final BigDecimal PRICE = BigDecimal.TEN;
    private static final String PRODUCER = "producer";
    private static final String PRODUCT_NAME = "productName";
    @Mock
    private FindOrderByOrderKeyPort findOrderByOrderKeyPort;
    @Mock
    private GetProductDetailsApi getProductDetailsApi;
    @InjectMocks
    private GetOrderDetailsService getOrderDetailsService;

    @Test
    void shouldCreateAndReturnQueryResult() {
        // given
        OrderItem orderItem = new OrderItem(PRODUCT_IDENTIFIER, QUANTITY);
        Order order = new Order(ORDER_KEY, CUSTOMER_KEY, STATUS, List.of(orderItem));
        when(findOrderByOrderKeyPort.findByOrderKey(ORDER_KEY)).thenReturn(Optional.of(order));
        when(getProductDetailsApi.getProductDetailsMap(Set.of(PRODUCT_IDENTIFIER)))
                .thenReturn(Map.of(PRODUCT_IDENTIFIER,
                        new GetProductDetailsApi.ProductDetails(PRODUCT_NAME, PRODUCER, PRICE)));
        // when
        GetOrderDetailsQuery.QueryResult queryResult = getOrderDetailsService.execute(ORDER_KEY);
        // then
        assertThat(queryResult.orderKey()).isEqualTo(ORDER_KEY);
        assertThat(queryResult.customerKey()).isEqualTo(CUSTOMER_KEY);
        assertThat(queryResult.status()).isEqualTo(STATUS);
        assertThat(queryResult.orderItemDetails()).hasSize(1);
        GetOrderDetailsQuery.OrderItemDetails orderItemDetails = queryResult.orderItemDetails().getFirst();
        assertThat(orderItemDetails.productName()).isEqualTo(PRODUCT_NAME);
        assertThat(orderItemDetails.producer()).isEqualTo(PRODUCER);
        assertThat(orderItemDetails.price()).isEqualTo(PRICE);
        assertThat(orderItemDetails.quantity()).isEqualTo(QUANTITY);
        assertThat(queryResult.totalPrice()).isEqualTo(BigDecimal.valueOf(50L));
    }

    @Test
    void shouldThrowExceptionWhenOrderConsistOfNonExistingProduct() {
        OrderItem orderItem = new OrderItem(PRODUCT_IDENTIFIER, QUANTITY);
        ProductIdentifier invalidProductIdentifier = new ProductIdentifier(UUID.randomUUID().toString());
        OrderItem orderItemWithInvalidProduct = new OrderItem(invalidProductIdentifier, QUANTITY);
        Order order = new Order(ORDER_KEY, CUSTOMER_KEY, STATUS, List.of(orderItem, orderItemWithInvalidProduct));
        when(findOrderByOrderKeyPort.findByOrderKey(ORDER_KEY)).thenReturn(Optional.of(order));
        when(getProductDetailsApi.getProductDetailsMap(Set.of(PRODUCT_IDENTIFIER, invalidProductIdentifier)))
                .thenReturn(Map.of(PRODUCT_IDENTIFIER,
                        new GetProductDetailsApi.ProductDetails(PRODUCT_NAME, PRODUCER, PRICE)));
        // when + then
        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () -> getOrderDetailsService.execute(ORDER_KEY));
        assertThat(illegalStateException.getMessage()).isEqualTo(format(PRODUCT_DOES_NOT_EXIST_EXCEPTION_MESSAGE, invalidProductIdentifier.getValue()));
    }

    @Test
    void shouldThrowExceptionWhenOrderIsNotFound() {
        // given
        when(findOrderByOrderKeyPort.findByOrderKey(ORDER_KEY)).thenReturn(Optional.empty());
        // when + then
        assertThrows(OrderNotFoundException.class, () -> getOrderDetailsService.execute(ORDER_KEY));
    }

    @Test
    void shouldThrowExceptionWhenOrderKeyIsNull() {
        // when + then
        assertThrows(NullPointerException.class, () -> getOrderDetailsService.execute(null));
        verifyNoInteractions(findOrderByOrderKeyPort, getProductDetailsApi);
    }


}
