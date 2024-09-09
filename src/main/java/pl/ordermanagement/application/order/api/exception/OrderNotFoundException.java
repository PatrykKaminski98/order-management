package pl.ordermanagement.application.order.api.exception;

import static java.lang.String.format;

import java.util.NoSuchElementException;

import pl.ordermanagement.application.shared.datatype.OrderKey;


public class OrderNotFoundException extends NoSuchElementException {
    private static final String DOMAIN_ENTITY_NOT_FOUND_EXCEPTION_MESSAGE = "Order with key %s has not been found";
    public OrderNotFoundException(OrderKey orderKey) {
        super(format(DOMAIN_ENTITY_NOT_FOUND_EXCEPTION_MESSAGE, orderKey.getValue()));}
}
