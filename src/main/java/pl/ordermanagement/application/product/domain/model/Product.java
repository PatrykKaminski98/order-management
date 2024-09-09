package pl.ordermanagement.application.product.domain.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import pl.ordermanagement.application.shared.datatype.AbstractAggregate;
import pl.ordermanagement.application.shared.datatype.ProductIdentifier;

@Getter
@Setter
@AllArgsConstructor
public class Product extends AbstractAggregate{
    @NonNull
    private ProductIdentifier productIdentifier;
    private String name;
    private String producer;
    @NonNull
    private BigDecimal price;
    @NonNull
    private int quantityAvailable;
}
