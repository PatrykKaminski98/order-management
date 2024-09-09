package pl.ordermanagement.adapter.out.jpa.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import pl.ordermanagement.application.shared.datatype.ProductIdentifier;

@Converter(autoApply = true)
public class ProductIdentifierConverter implements AttributeConverter<pl.ordermanagement.application.shared.datatype.ProductIdentifier, String> {
    @Override
    public String convertToDatabaseColumn(ProductIdentifier productIdentifier) {
        return productIdentifier != null ? productIdentifier.getValue() : null;
    }

    @Override
    public ProductIdentifier convertToEntityAttribute(String value) {
        return value != null ? new ProductIdentifier(value) : null;
    }
}
