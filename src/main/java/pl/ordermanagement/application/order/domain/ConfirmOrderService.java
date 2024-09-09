package pl.ordermanagement.application.order.domain;

import static java.lang.String.format;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ordermanagement.application.order.api.exception.InvalidOrderStatusException;
import pl.ordermanagement.application.order.api.exception.OrderNotFoundException;
import pl.ordermanagement.application.order.api.port.in.usecase.ConfirmOrderUseCase;
import pl.ordermanagement.application.order.api.port.out.FindOrderByOrderKeyPort;
import pl.ordermanagement.application.order.api.port.out.UpdateOrderPort;
import pl.ordermanagement.application.order.domain.model.Order;
import pl.ordermanagement.application.shared.datatype.OrderKey;
import pl.ordermanagement.application.shared.datatype.Status;

@Service
@RequiredArgsConstructor
class ConfirmOrderService implements ConfirmOrderUseCase {
    private static final String INCORRECT_ORDER_STATUS_EXCEPTION_MESSAGE = "Processed order is in %s state. Confirmation cannot be done.- OrderKey: %s";
    private final FindOrderByOrderKeyPort findOrderByOrderKeyPort;
    private final UpdateOrderPort updateOrderPort;

    @Override
    @Transactional
    public void apply(@NonNull OrderKey orderKey) {
        Order order = findOrderByOrderKeyPort.findByOrderKey(orderKey).orElseThrow(() -> new OrderNotFoundException(orderKey));
        validateOperation(order);
        order.setStatus(Status.CONFIRMED);
        updateOrderPort.update(order);
    }

    private void validateOperation(Order orderToBeConfirmed) {
        if(orderToBeConfirmed.getStatus().equals(Status.CANCELLED) || orderToBeConfirmed.getStatus().equals(Status.COMPLETED))
        {
            throw new InvalidOrderStatusException(format(INCORRECT_ORDER_STATUS_EXCEPTION_MESSAGE, orderToBeConfirmed.getStatus(),orderToBeConfirmed.getOrderKey().getValue()));
        }
    }
}
