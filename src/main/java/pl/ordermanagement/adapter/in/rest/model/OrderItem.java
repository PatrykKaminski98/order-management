package pl.ordermanagement.adapter.in.rest.model;

import pl.ordermanagement.application.shared.datatype.ProductIdentifier;

public record OrderItem (ProductIdentifier productIdentifier, int quantity){}
