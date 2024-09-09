package pl.ordermanagement.application.order.domain;

import static java.lang.String.format;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ordermanagement.application.order.api.exception.InvalidOrderStatusException;
import pl.ordermanagement.application.order.api.exception.OrderNotFoundException;
import pl.ordermanagement.application.order.api.port.in.usecase.CancelOrderUseCase;
import pl.ordermanagement.application.order.api.port.out.FindOrderByOrderKeyPort;
import pl.ordermanagement.application.order.api.port.out.UpdateOrderPort;
import pl.ordermanagement.application.order.domain.model.Order;
import pl.ordermanagement.application.shared.datatype.OrderKey;
import pl.ordermanagement.application.shared.datatype.Status;

@Service
@RequiredArgsConstructor
class CancelOrderService implements CancelOrderUseCase {
    private static final String INCORRECT_ORDER_STATUS_EXCEPTION_MESSAGE = "Processed order is in %s state. It cannot be cancelled. OrderKey: %s";
    private final FindOrderByOrderKeyPort findOrderByOrderKeyPort;
    private final UpdateOrderPort updateOrderPort;

    @Override
    @Transactional
    public void apply(@NonNull OrderKey orderKey) {
        Order order = findOrderByOrderKeyPort.findByOrderKey(orderKey).orElseThrow(() -> new OrderNotFoundException(orderKey));
        validateOperation(order);
        order.setStatus(Status.CANCELLED);
        updateOrderPort.update(order);
    }

    private void validateOperation(Order orderToBeCancelled) {
        if(orderToBeCancelled.getStatus().equals(Status.COMPLETED))
        {
            throw new InvalidOrderStatusException(format(INCORRECT_ORDER_STATUS_EXCEPTION_MESSAGE, orderToBeCancelled.getStatus(), orderToBeCancelled.getOrderKey().getValue()));
        }
    }
}
