package pl.ordermanagement.adapter.in.rest.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import pl.ordermanagement.adapter.in.rest.model.Product;
import pl.ordermanagement.configuration.MapperConfiguration;

@Mapper(config = MapperConfiguration.class)
public interface ProductMapper {
    List<Product> map(Collection<pl.ordermanagement.application.product.domain.model.Product> products);
}
