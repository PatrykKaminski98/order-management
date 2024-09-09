package pl.ordermanagement.application.order.api.exception;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

class InvalidOrderStatusExceptionTest {
    private static final String EXCEPTION_MESSAGE = "Invalid order status";
    @Test
    void shouldCreateInvalidOrderStatusException() {
        // when
        InvalidOrderStatusException invalidOrderStatusException = new InvalidOrderStatusException(EXCEPTION_MESSAGE);
        // then
        assertThat(invalidOrderStatusException.getMessage()).isEqualTo(EXCEPTION_MESSAGE);
    }
}
