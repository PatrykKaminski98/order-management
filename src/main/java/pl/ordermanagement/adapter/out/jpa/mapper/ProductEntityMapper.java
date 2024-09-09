package pl.ordermanagement.adapter.out.jpa.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import pl.ordermanagement.adapter.out.jpa.entity.ProductEntity;
import pl.ordermanagement.application.product.domain.model.Product;
import pl.ordermanagement.configuration.MapperConfiguration;

@Mapper(config = MapperConfiguration.class)
public interface ProductEntityMapper {
    List<Product> map(Collection<ProductEntity> productEntityCollection);
}
