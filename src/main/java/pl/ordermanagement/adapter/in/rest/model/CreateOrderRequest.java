package pl.ordermanagement.adapter.in.rest.model;

import java.util.List;

import pl.ordermanagement.application.shared.datatype.CustomerKey;

public record CreateOrderRequest(CustomerKey customerKey, List<OrderItem> orderItems) {
}
