package pl.ordermanagement.adapter.in.rest.model;

import java.math.BigDecimal;

public record OrderItemDetails(
        String productName,
        String producer,
        BigDecimal price,
        int quantity
) {
}
