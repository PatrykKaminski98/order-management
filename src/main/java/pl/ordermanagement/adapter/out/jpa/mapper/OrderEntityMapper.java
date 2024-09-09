package pl.ordermanagement.adapter.out.jpa.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.ordermanagement.adapter.out.jpa.entity.OrderItemEntity;
import pl.ordermanagement.adapter.out.jpa.entity.PurchaseOrderEntity;
import pl.ordermanagement.application.order.domain.model.Order;
import pl.ordermanagement.application.order.domain.model.OrderItem;
import pl.ordermanagement.configuration.MapperConfiguration;

@Mapper(config = MapperConfiguration.class)
public interface OrderEntityMapper {
    Order map (PurchaseOrderEntity purchaseOrderEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "created", ignore = true)
    void mapExisting(Order order, @MappingTarget PurchaseOrderEntity existingPurchaseOrderEntity);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "created", ignore = true)
    PurchaseOrderEntity mapToNewEntity(Order order);

    List<OrderItemEntity> map(Collection<OrderItem> orderItems);

}
