package pl.ordermanagement.adapter.out.jpa.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import pl.ordermanagement.application.shared.datatype.OrderKey;

@Converter(autoApply = true)
public class OrderKeyConverter implements AttributeConverter<OrderKey, String> {
    @Override
    public String convertToDatabaseColumn(OrderKey orderKey) {
        return orderKey != null ? orderKey.getValue() : null;
    }

    @Override
    public OrderKey convertToEntityAttribute(String value) {
        return value != null ? new OrderKey(value) : null;
    }
}
