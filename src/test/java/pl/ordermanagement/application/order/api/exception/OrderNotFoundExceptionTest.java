package pl.ordermanagement.application.order.api.exception;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import pl.ordermanagement.application.shared.datatype.OrderKey;

class OrderNotFoundExceptionTest {

    private static final OrderKey ORDER_KEY = new OrderKey(UUID.randomUUID().toString());

    @Test
    void shouldCreateExceptionWithCorrectMessage() {
        // when
        OrderNotFoundException exception = new OrderNotFoundException(ORDER_KEY);
        // then
        assertThat(exception.getMessage()).isEqualTo("Order with key " + ORDER_KEY.getValue() + " has not been found");
    }
}
