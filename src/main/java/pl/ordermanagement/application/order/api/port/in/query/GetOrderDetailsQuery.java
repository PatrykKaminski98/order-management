package pl.ordermanagement.application.order.api.port.in.query;


import java.math.BigDecimal;
import java.util.List;

import lombok.NonNull;
import pl.ordermanagement.application.shared.datatype.CustomerKey;
import pl.ordermanagement.application.shared.datatype.OrderKey;
import pl.ordermanagement.application.shared.datatype.Status;

public interface GetOrderDetailsQuery {
    QueryResult execute(@NonNull OrderKey orderKey);

    record QueryResult(
            OrderKey orderKey,
            CustomerKey customerKey,
            Status status,
            List<OrderItemDetails> orderItemDetails,
            BigDecimal totalPrice
    ){}
    record OrderItemDetails(
            String productName,
            String producer,
            BigDecimal price,
            int quantity
    ){}


}
