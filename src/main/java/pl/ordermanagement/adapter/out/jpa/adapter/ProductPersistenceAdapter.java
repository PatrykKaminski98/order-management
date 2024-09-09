package pl.ordermanagement.adapter.out.jpa.adapter;

import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.ordermanagement.adapter.out.jpa.entity.ProductEntity;
import pl.ordermanagement.adapter.out.jpa.mapper.ProductEntityMapper;
import pl.ordermanagement.adapter.out.jpa.repository.ProductRepository;
import pl.ordermanagement.application.product.api.port.out.FindAllProductsPort;
import pl.ordermanagement.application.product.api.port.out.FindProductsByProductIdentifiersPort;
import pl.ordermanagement.application.product.domain.model.Product;
import pl.ordermanagement.application.shared.datatype.ProductIdentifier;

@Component
@RequiredArgsConstructor
class ProductPersistenceAdapter implements FindProductsByProductIdentifiersPort, FindAllProductsPort {
    private final ProductRepository repository;
    private final ProductEntityMapper mapper;

    @Override
    public List<Product> findAllProducts() {
        return mapper.map(repository.findAll());
    }

    @Override
    public List<Product> findProducts(Set<ProductIdentifier> productIdentifiers) {
        List<ProductEntity> productDetailsEntities = repository.findByProductIdentifierIn(productIdentifiers);
        return mapper.map(productDetailsEntities);
    }
}
