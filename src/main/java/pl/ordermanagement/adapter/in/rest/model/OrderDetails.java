package pl.ordermanagement.adapter.in.rest.model;

import java.math.BigDecimal;
import java.util.List;

import pl.ordermanagement.application.shared.datatype.CustomerKey;
import pl.ordermanagement.application.shared.datatype.OrderKey;
import pl.ordermanagement.application.shared.datatype.Status;

public record OrderDetails(
        OrderKey orderKey,
        Status status,
        CustomerKey customerKey,
        List<OrderItemDetails> orderItemDetails,
        BigDecimal totalPrice) {}
