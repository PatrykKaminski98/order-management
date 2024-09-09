package pl.ordermanagement.application.shared.datatype;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public abstract class AbstractStringValueObject {
    private final String value;

    protected AbstractStringValueObject(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Value cannot be blank");
        }
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }
}
