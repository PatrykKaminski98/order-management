package pl.ordermanagement.application.product.api.port.in;

import java.util.List;

import pl.ordermanagement.application.product.domain.model.Product;

public interface GetAllProductsQuery {
    List<Product> execute();
}
