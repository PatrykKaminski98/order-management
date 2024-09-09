package pl.ordermanagement.adapter.in.rest.model;

import java.util.List;

import pl.ordermanagement.application.shared.datatype.CustomerKey;
import pl.ordermanagement.application.shared.datatype.OrderKey;
import pl.ordermanagement.application.shared.datatype.Status;


public record Order(OrderKey orderKey, CustomerKey customerKey, Status status, List<OrderItem> orderItems) {
}
