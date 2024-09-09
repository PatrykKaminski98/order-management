package pl.ordermanagement.application.product.domain;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ordermanagement.application.product.api.GetProductDetailsApi;
import pl.ordermanagement.application.product.api.port.out.FindProductsByProductIdentifiersPort;
import pl.ordermanagement.application.product.domain.model.Product;
import pl.ordermanagement.application.shared.datatype.ProductIdentifier;

@Service
@RequiredArgsConstructor
class GetProductDetailsService implements GetProductDetailsApi {
    private final FindProductsByProductIdentifiersPort findProductsByProductIdentifiersPort;

    @Override
    public Map<ProductIdentifier, ProductDetails> getProductDetailsMap(Set<ProductIdentifier> productIdentifiers) {
        return findProductsByProductIdentifiersPort.findProducts(productIdentifiers).stream()
                .collect(Collectors.toMap(
                        Product::getProductIdentifier,
                        this::mapToProductDetails
                ));
    }

    private ProductDetails mapToProductDetails(Product product){
        return new ProductDetails(product.getName(), product.getProducer(), product.getPrice());
    }
}
