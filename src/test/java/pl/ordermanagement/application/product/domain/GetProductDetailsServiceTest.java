package pl.ordermanagement.application.product.domain;


import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ordermanagement.application.product.api.GetProductDetailsApi;
import pl.ordermanagement.application.product.api.port.out.FindProductsByProductIdentifiersPort;
import pl.ordermanagement.application.product.domain.model.Product;
import pl.ordermanagement.application.shared.datatype.ProductIdentifier;

@ExtendWith(MockitoExtension.class)
class GetProductDetailsServiceTest {
    private static final ProductIdentifier PRODUCT_IDENTIFIER_1 = new ProductIdentifier(UUID.randomUUID().toString());
    private static final ProductIdentifier PRODUCT_IDENTIFIER_2 = new ProductIdentifier(UUID.randomUUID().toString());
    private static final String PRODUCT_1 = "product1";
    private static final String PRODUCT_2 = "product2";
    private static final String PRODUCER_1 = "producer1";
    private static final String PRODUCER_2 = "producer2";
    @Mock
    private FindProductsByProductIdentifiersPort findProductsByProductIdentifiersPort;
    @InjectMocks
    private GetProductDetailsService getProductDetailsService;

    @Test
    void shouldReturnProductDetailsMap() {
        // given
        Set<ProductIdentifier> productIdentifiers = Set.of(PRODUCT_IDENTIFIER_1, PRODUCT_IDENTIFIER_2);
        Product product1 = new Product(PRODUCT_IDENTIFIER_1, PRODUCT_1, PRODUCER_1, BigDecimal.TEN, 45);
        Product product2 = new Product(PRODUCT_IDENTIFIER_2, PRODUCT_2, PRODUCER_2, BigDecimal.TWO, 3);
        List<Product> products = List.of(product1, product2);
        when(findProductsByProductIdentifiersPort.findProducts(productIdentifiers)).thenReturn(products);
        // when
        Map<ProductIdentifier, GetProductDetailsApi.ProductDetails> productDetailsMap = getProductDetailsService.getProductDetailsMap(productIdentifiers);
        assertThat(productDetailsMap.values()).anySatisfy(productDetails -> assertProductDetails(productDetails, product1));
        assertThat(productDetailsMap.values()).anySatisfy(productDetails -> assertProductDetails(productDetails, product2));
    }

    @Test
    void shouldReturnEmptyMapWhenThereIsNoProductsForGivenIdentifiers() {
        // given
        Set<ProductIdentifier> productIdentifiers = Set.of(PRODUCT_IDENTIFIER_1, PRODUCT_IDENTIFIER_2);
        when(findProductsByProductIdentifiersPort.findProducts(productIdentifiers)).thenReturn(emptyList());
        // when
        Map<ProductIdentifier, GetProductDetailsApi.ProductDetails> productDetailsMap = getProductDetailsService.getProductDetailsMap(productIdentifiers);
        assertThat(productDetailsMap).isEmpty();
    }

    private void  assertProductDetails(GetProductDetailsApi.ProductDetails productDetails, Product product) {
        assertThat(productDetails.name()).isEqualTo(product.getName());
        assertThat(productDetails.producer()).isEqualTo(product.getProducer());
        assertThat(productDetails.price()).isEqualTo(product.getPrice());
    }
}
