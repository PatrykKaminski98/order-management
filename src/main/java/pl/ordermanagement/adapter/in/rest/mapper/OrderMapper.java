package pl.ordermanagement.adapter.in.rest.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;

import pl.ordermanagement.adapter.in.rest.model.CreateOrderRequest;
import pl.ordermanagement.adapter.in.rest.model.OrderDetails;
import pl.ordermanagement.adapter.in.rest.model.OrderItem;
import pl.ordermanagement.adapter.in.rest.model.OrderItemDetails;
import pl.ordermanagement.application.order.api.port.in.usecase.CreateOrderUseCase;
import pl.ordermanagement.application.order.api.port.in.query.GetOrderDetailsQuery;
import pl.ordermanagement.application.order.domain.model.Order;
import pl.ordermanagement.configuration.MapperConfiguration;

@Mapper(config = MapperConfiguration.class)
public interface OrderMapper {
    Order map(pl.ordermanagement.adapter.in.rest.model.Order order);

    CreateOrderUseCase.Command map (CreateOrderRequest request);

    pl.ordermanagement.adapter.in.rest.model.Order map(Order order);

    OrderDetails map(GetOrderDetailsQuery.QueryResult queryResult);

    List<OrderItemDetails> map(Collection<GetOrderDetailsQuery.OrderItemDetails> orderItemDetails);

    default OrderItem map(pl.ordermanagement.application.order.domain.model.OrderItem orderItem){
        return new OrderItem(orderItem.getProductIdentifier(), orderItem.getQuantity());
    }
}
