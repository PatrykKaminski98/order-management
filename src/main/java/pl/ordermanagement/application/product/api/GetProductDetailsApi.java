package pl.ordermanagement.application.product.api;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import pl.ordermanagement.application.shared.datatype.ProductIdentifier;

public interface GetProductDetailsApi {
    Map<ProductIdentifier, ProductDetails> getProductDetailsMap(Set<ProductIdentifier> productIdentifiers);

    record ProductDetails(
            String name,
            String producer,
            BigDecimal price) {
    }
}

