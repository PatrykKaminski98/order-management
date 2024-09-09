package pl.ordermanagement.application.product.domain;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ordermanagement.application.product.api.port.out.FindAllProductsPort;
import pl.ordermanagement.application.product.domain.model.Product;

@Service
@RequiredArgsConstructor
class GetAllProductsQuery implements pl.ordermanagement.application.product.api.port.in.GetAllProductsQuery {
    private final FindAllProductsPort findAllProductsPort;

    @Override
    public List<Product> execute() {
        return findAllProductsPort.findAllProducts();
    }
}
