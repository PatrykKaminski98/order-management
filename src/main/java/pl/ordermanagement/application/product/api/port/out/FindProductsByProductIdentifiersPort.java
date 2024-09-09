package pl.ordermanagement.application.product.api.port.out;

import java.util.List;
import java.util.Set;

import pl.ordermanagement.application.product.domain.model.Product;
import pl.ordermanagement.application.shared.datatype.ProductIdentifier;

public interface FindProductsByProductIdentifiersPort {
    List<Product> findProducts(Set<ProductIdentifier> productIdentifiers);


}
