package pl.ordermanagement.adapter.in.rest.model;

import java.math.BigDecimal;

import pl.ordermanagement.application.shared.datatype.ProductIdentifier;

public record Product(ProductIdentifier productIdentifier, String name, String producer, BigDecimal price, Integer quantityAvailable) {}
