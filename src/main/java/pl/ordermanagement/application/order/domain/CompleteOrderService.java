package pl.ordermanagement.application.order.domain;


import static java.lang.String.format;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.ordermanagement.application.order.api.exception.InvalidOrderStatusException;
import pl.ordermanagement.application.order.api.exception.OrderNotFoundException;
import pl.ordermanagement.application.order.api.port.in.usecase.CompleteOrderUseCase;
import pl.ordermanagement.application.order.api.port.out.FindOrderByOrderKeyPort;
import pl.ordermanagement.application.order.api.port.out.UpdateOrderPort;
import pl.ordermanagement.application.order.domain.model.Order;
import pl.ordermanagement.application.shared.datatype.OrderKey;
import pl.ordermanagement.application.shared.datatype.Status;

@Component
@RequiredArgsConstructor
class CompleteOrderService implements CompleteOrderUseCase {
    private static final String INCORRECT_ORDER_STATUS_EXCEPTION_MESSAGE = "Processed order is in %s state. It cannot be completed. OrderKey: %s";
    private final FindOrderByOrderKeyPort findOrderByOrderKeyPort;
    private final UpdateOrderPort updateOrderPort;

    @Override
    @Transactional
    public void apply(@NonNull OrderKey orderKey) {
        Order order = findOrderByOrderKeyPort.findByOrderKey(orderKey).orElseThrow(() -> new OrderNotFoundException(orderKey));
        validateOperation(order);
        order.setStatus(Status.COMPLETED);
        updateOrderPort.update(order);
    }

    private void validateOperation(Order orderToBeCompleted) {
        if(orderToBeCompleted.getStatus().equals(Status.CANCELLED) || orderToBeCompleted.getStatus().equals(Status.COMPLETED))
        {
            throw new InvalidOrderStatusException(format(INCORRECT_ORDER_STATUS_EXCEPTION_MESSAGE, orderToBeCompleted.getStatus(), orderToBeCompleted.getOrderKey().getValue()));
        }
    }
}
