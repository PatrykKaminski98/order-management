package pl.ordermanagement.application.order.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import pl.ordermanagement.application.shared.datatype.AbstractAggregate;
import pl.ordermanagement.application.shared.datatype.ProductIdentifier;

@Getter
@Setter
@AllArgsConstructor
public  class OrderItem extends AbstractAggregate {
    @NonNull
    private ProductIdentifier productIdentifier;
    @NonNull
    private Integer quantity;
}
