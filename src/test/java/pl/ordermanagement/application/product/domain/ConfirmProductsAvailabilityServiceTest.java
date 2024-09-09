package pl.ordermanagement.application.product.domain;

import static java.lang.String.format;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ordermanagement.application.product.api.ConfirmProductsAvailabilityApi;
import pl.ordermanagement.application.product.api.exception.ProductNotFoundException;
import pl.ordermanagement.application.product.api.port.out.FindProductsByProductIdentifiersPort;
import pl.ordermanagement.application.product.domain.model.Product;
import pl.ordermanagement.application.shared.datatype.ProductIdentifier;

@ExtendWith(MockitoExtension.class)
class ConfirmProductsAvailabilityServiceTest {
    private static final String OUT_OF_STOCK_EXCEPTION_MESSAGE = "The indicated quantity for product identifier: %s is out of stock. Ordered: %s. Available: %s";
    private static final ProductIdentifier PRODUCT_IDENTIFIER_1 = new ProductIdentifier("123");
    private static final ProductIdentifier PRODUCT_IDENTIFIER_2 = new ProductIdentifier("321");
    private static final String PRODUCT_1 = "product1";
    private static final String PRODUCT_2 = "product2";
    private static final String PRODUCER_1 = "producer1";
    private static final String PRODUCER_2 = "producer2";
    @Mock
    private FindProductsByProductIdentifiersPort findProductsByProductIdentifiersPort;
    @InjectMocks
    private ConfirmProductsAvailabilityService confirmProductsAvailabilityService;

    @Test
    void shouldNotThrowExceptionWhenOrderItemsAreValid() {
        // given
        List<ConfirmProductsAvailabilityApi.OrderItem> orderItems = List.of(
                new ConfirmProductsAvailabilityApi.OrderItem(PRODUCT_IDENTIFIER_1, 5),
                new ConfirmProductsAvailabilityApi.OrderItem(PRODUCT_IDENTIFIER_2, 15));
        List<Product> products = List.of(
                new Product(PRODUCT_IDENTIFIER_1, PRODUCT_1, PRODUCER_1, BigDecimal.TEN, 20),
                new Product(PRODUCT_IDENTIFIER_2, PRODUCT_2, PRODUCER_2, BigDecimal.TWO, 17));
        when(findProductsByProductIdentifiersPort.findProducts(Set.of(PRODUCT_IDENTIFIER_1, PRODUCT_IDENTIFIER_2))).thenReturn(products);
        // when + then
        assertDoesNotThrow(() -> confirmProductsAvailabilityService.confirmProductsAvailability(orderItems));
    }

    @Test
    void shouldThrowExceptionWhenOrderedQuantityIsGreaterThanAvailableQuantity() {
        // given
        List<ConfirmProductsAvailabilityApi.OrderItem> orderItems = List.of(
                new ConfirmProductsAvailabilityApi.OrderItem(PRODUCT_IDENTIFIER_1, 15),
                new ConfirmProductsAvailabilityApi.OrderItem(PRODUCT_IDENTIFIER_2, 15));
        List<Product> products = List.of(
                new Product(PRODUCT_IDENTIFIER_1, PRODUCT_1, PRODUCER_1, BigDecimal.TEN, 12),
                new Product(PRODUCT_IDENTIFIER_2, PRODUCT_2, PRODUCER_2, BigDecimal.TWO, 17));
        when(findProductsByProductIdentifiersPort.findProducts(Set.of(PRODUCT_IDENTIFIER_1, PRODUCT_IDENTIFIER_2))).thenReturn(products);
        // when + then
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> confirmProductsAvailabilityService.confirmProductsAvailability(orderItems));
        assertThat(illegalArgumentException.getMessage()).isEqualTo(format(OUT_OF_STOCK_EXCEPTION_MESSAGE, PRODUCT_IDENTIFIER_1.getValue(), 15, 12));
    }

    @Test
    void shouldThrowExceptionWhenOrderItemsListContainsNonExistingProduct() {
        // given
        List<ConfirmProductsAvailabilityApi.OrderItem> orderItems = List.of(
                new ConfirmProductsAvailabilityApi.OrderItem(PRODUCT_IDENTIFIER_1, 15),
                new ConfirmProductsAvailabilityApi.OrderItem(PRODUCT_IDENTIFIER_2, 15));
        List<Product> products = List.of(
                new Product(PRODUCT_IDENTIFIER_1, PRODUCT_1, PRODUCER_1, BigDecimal.TEN, 20));
        when(findProductsByProductIdentifiersPort.findProducts(Set.of(PRODUCT_IDENTIFIER_1, PRODUCT_IDENTIFIER_2))).thenReturn(products);
        // when + then
        ProductNotFoundException productNotFoundException = assertThrows(ProductNotFoundException.class, () -> confirmProductsAvailabilityService.confirmProductsAvailability(orderItems));
    }
}
