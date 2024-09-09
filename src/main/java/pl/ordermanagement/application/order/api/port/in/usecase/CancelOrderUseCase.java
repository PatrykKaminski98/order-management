package pl.ordermanagement.application.order.api.port.in.usecase;

import lombok.NonNull;
import pl.ordermanagement.application.shared.datatype.OrderKey;

public interface CancelOrderUseCase {
    void apply(@NonNull OrderKey orderKey);
}