package pl.ordermanagement.adapter.out.jpa.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import pl.ordermanagement.application.shared.datatype.CustomerKey;

@Converter(autoApply = true)
public class CustomerKeyConverter implements AttributeConverter<CustomerKey, String> {
    @Override
    public String convertToDatabaseColumn(CustomerKey customerKey) {
        return customerKey != null ? customerKey.getValue() : null;
    }

    @Override
    public CustomerKey convertToEntityAttribute(String value) {
        return value != null ? new CustomerKey(value) : null;
    }
}
