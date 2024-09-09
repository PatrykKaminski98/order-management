package pl.ordermanagement.application.product.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ordermanagement.application.product.api.port.out.FindAllProductsPort;
import pl.ordermanagement.application.product.domain.model.Product;
import pl.ordermanagement.application.shared.datatype.ProductIdentifier;

@ExtendWith(MockitoExtension.class)
class GetAllProductsServiceTest {
    private static final Product PRODUCT_1 = new Product(new ProductIdentifier("DT2352"), "name1", "producer1", BigDecimal.TEN, 15);
    private static final Product PRODUCT_2 = new Product(new ProductIdentifier("PA4232"), "name2", "producer2", BigDecimal.TWO, 150);
    @Mock
    private FindAllProductsPort findAllProductsPort;
    @InjectMocks
    private GetAllProductsService getAllProductsService;

    @Test
    void shouldReturnAllProductsList() {
        // given
        List<Product> products = List.of(PRODUCT_1, PRODUCT_2);
        when(findAllProductsPort.findAllProducts()).thenReturn(products);
        // when
        List<Product> results = getAllProductsService.execute();
        // then
        assertThat(results).isEqualTo(products);
    }
}
