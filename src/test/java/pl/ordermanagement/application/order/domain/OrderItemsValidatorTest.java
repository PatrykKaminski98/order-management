package pl.ordermanagement.application.order.domain;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ordermanagement.application.order.domain.model.OrderItem;
import pl.ordermanagement.application.product.api.ConfirmProductsAvailabilityApi;
import pl.ordermanagement.application.shared.datatype.ProductIdentifier;

@ExtendWith(MockitoExtension.class)
class OrderItemsValidatorTest {
    private static final String EMPTY_ORDER_ITEMS_EXCEPTION_MESSAGE = "Order item list cannot be empty";
    private static final String NEGATIVE_QUANTITY_EXCEPTION_MESSAGE = "The quantity in an order item cannot be negative.";
    @Mock
    private ConfirmProductsAvailabilityApi confirmProductsAvailabilityApi;
    @InjectMocks
    private OrderItemsValidator orderItemsValidator;

    @Test
    void shouldNotThrowExceptionWhenOrderItemsPassValidation() {
        // given
        List<OrderItem> orderItems = List.of(
                new OrderItem(new ProductIdentifier(UUID.randomUUID().toString()), 5),
                new OrderItem(new ProductIdentifier(UUID.randomUUID().toString()), 2));
        // when + then
        assertDoesNotThrow(() -> orderItemsValidator.validate(orderItems));
        verify(confirmProductsAvailabilityApi).confirmProductsAvailability(anyList());
    }

    @Test
    void shouldThrowExceptionWhenOrderItemListIsEmpty() {
        // given
        List<OrderItem> orderItems = emptyList();
        // when + then
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> orderItemsValidator.validate(orderItems));
        assertThat(illegalArgumentException.getMessage()).isEqualTo(EMPTY_ORDER_ITEMS_EXCEPTION_MESSAGE);
        verify(confirmProductsAvailabilityApi, never()).confirmProductsAvailability(anyList());
    }

    @Test
    void shouldThrowExceptionWhenCollectionContainsItemWithNegativeQuantity() {
        // given
        List<OrderItem> orderItems = List.of(
                new OrderItem(new ProductIdentifier(UUID.randomUUID().toString()), 5),
                new OrderItem(new ProductIdentifier(UUID.randomUUID().toString()), -4));

        // when + then
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> orderItemsValidator.validate(orderItems));
        assertThat(illegalArgumentException.getMessage()).isEqualTo(NEGATIVE_QUANTITY_EXCEPTION_MESSAGE);
        verify(confirmProductsAvailabilityApi, never()).confirmProductsAvailability(anyList());
    }
}
