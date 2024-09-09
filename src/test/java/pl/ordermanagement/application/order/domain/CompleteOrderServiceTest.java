package pl.ordermanagement.application.order.domain;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ordermanagement.application.order.api.exception.InvalidOrderStatusException;
import pl.ordermanagement.application.order.api.exception.OrderNotFoundException;
import pl.ordermanagement.application.order.api.port.out.FindOrderByOrderKeyPort;
import pl.ordermanagement.application.order.api.port.out.UpdateOrderPort;
import pl.ordermanagement.application.order.domain.model.Order;
import pl.ordermanagement.application.shared.datatype.CustomerKey;
import pl.ordermanagement.application.shared.datatype.OrderKey;
import pl.ordermanagement.application.shared.datatype.Status;

@ExtendWith(MockitoExtension.class)
class CompleteOrderServiceTest {
    private static final String INCORRECT_ORDER_STATUS_EXCEPTION_MESSAGE = "Processed order is in %s state. It cannot be completed. OrderKey: %s";
    private static final OrderKey ORDER_KEY = new OrderKey(UUID.randomUUID().toString());
    private static final CustomerKey CUSTOMER_KEY = new CustomerKey("KAMIN12");
    @Mock
    private FindOrderByOrderKeyPort findOrderByOrderKeyPort;
    @Mock
    private UpdateOrderPort updateOrderPort;
    @InjectMocks
    private CompleteOrderService completeOrderService;
    @Captor
    private ArgumentCaptor<Order> orderArgumentCaptor;

    @Test
    void shouldCompleteOrder() {
        // given
        Order orderToBeCompleted = new Order(ORDER_KEY, CUSTOMER_KEY, Status.CONFIRMED, emptyList());
        when(findOrderByOrderKeyPort.findByOrderKey(ORDER_KEY)).thenReturn(Optional.of(orderToBeCompleted));
        // when
        completeOrderService.apply(ORDER_KEY);
        // then
        verify(updateOrderPort).update(orderArgumentCaptor.capture());
        Order order = orderArgumentCaptor.getValue();
        assertThat(order.getStatus()).isEqualTo(Status.COMPLETED);
    }

    @Test
    void shouldThrowExceptionWhenOrderToBeCompletedWasNotFound() {
        // given
        when(findOrderByOrderKeyPort.findByOrderKey(ORDER_KEY)).thenReturn(Optional.empty());
        // when + then
        assertThrows(OrderNotFoundException.class, () -> completeOrderService.apply(ORDER_KEY));
        verifyNoInteractions(updateOrderPort);
    }

    @Test
    void shouldThrowExceptionWhenOrderIsInCancelledState() {
        // given
        Order orderToBeCompleted = new Order(ORDER_KEY, CUSTOMER_KEY, Status.CANCELLED, emptyList());
        when(findOrderByOrderKeyPort.findByOrderKey(ORDER_KEY)).thenReturn(Optional.of(orderToBeCompleted));
        // when + then
        InvalidOrderStatusException invalidOrderStatusException = assertThrows(InvalidOrderStatusException.class, () -> completeOrderService.apply(ORDER_KEY));
        assertThat(invalidOrderStatusException.getMessage()).isEqualTo(String.format(INCORRECT_ORDER_STATUS_EXCEPTION_MESSAGE, orderToBeCompleted.getStatus(), orderToBeCompleted.getOrderKey().getValue()));
        verifyNoInteractions(updateOrderPort);
    }

    @Test
    void shouldThrowExceptionWhenOrderIsAlreadyInCompletedState() {
        // given
        Order orderToBeCompleted = new Order(ORDER_KEY, CUSTOMER_KEY, Status.COMPLETED, emptyList());
        when(findOrderByOrderKeyPort.findByOrderKey(ORDER_KEY)).thenReturn(Optional.of(orderToBeCompleted));
        // when + then
        InvalidOrderStatusException invalidOrderStatusException = assertThrows(InvalidOrderStatusException.class, () -> completeOrderService.apply(ORDER_KEY));
        assertThat(invalidOrderStatusException.getMessage()).isEqualTo(String.format(INCORRECT_ORDER_STATUS_EXCEPTION_MESSAGE, orderToBeCompleted.getStatus(), orderToBeCompleted.getOrderKey().getValue()));
        verifyNoInteractions(updateOrderPort);
    }

    @Test
    void shouldThrowExceptionWhenOrderKeyIsNull() {
        // when + then
        assertThrows(NullPointerException.class, () -> completeOrderService.apply(null));
        verifyNoInteractions(updateOrderPort);
    }

}