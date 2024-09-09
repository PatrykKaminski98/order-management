package pl.ordermanagement.application.product.api.port.out;

import java.util.List;

import pl.ordermanagement.application.product.domain.model.Product;

public interface FindAllProductsPort {
    List<Product> findAllProducts();

}
