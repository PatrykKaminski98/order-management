package pl.ordermanagement.application.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ordermanagement.application.order.api.port.in.usecase.CreateOrderUseCase;
import pl.ordermanagement.application.order.api.port.out.CreateOrderPort;
import pl.ordermanagement.application.order.domain.model.Order;
import pl.ordermanagement.application.shared.datatype.CustomerKey;
import pl.ordermanagement.application.shared.datatype.ProductIdentifier;
import pl.ordermanagement.application.shared.datatype.Status;

@ExtendWith(MockitoExtension.class)
class CreateOrderServiceTest {
    private static final CustomerKey CUSTOMER_KEY = new CustomerKey("KAMIN12");
    private static final int QUANTITY = 5;
    private static final ProductIdentifier PRODUCT_IDENTIFIER = new ProductIdentifier(UUID.randomUUID().toString());
    @Mock
    private CreateOrderPort createOrderPort;
    @Mock
    private OrderItemsValidator orderItemsValidator;
    @InjectMocks
    private CreateOrderService createOrderService;

    @Test
    void shouldCreateOrder() {
        // given
        CreateOrderUseCase.OrderItem orderItem = new CreateOrderUseCase.OrderItem(PRODUCT_IDENTIFIER, QUANTITY);
        CreateOrderUseCase.Command command = new CreateOrderUseCase.Command(CUSTOMER_KEY, List.of(orderItem));
        when(createOrderPort.create(any(Order.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        // when
        Order order = createOrderService.apply(command);
        // then
        assertThat(order.getOrderKey()).isNotNull();
        assertThat(order.getCustomerKey()).isEqualTo(CUSTOMER_KEY);
        assertThat(order.getStatus()).isEqualTo(Status.CREATED);
        assertThat(order.getOrderItems()).hasSize(1);
        assertThat(order.getOrderItems().getFirst().getProductIdentifier()).isEqualTo(PRODUCT_IDENTIFIER);
        assertThat(order.getOrderItems().getFirst().getQuantity()).isEqualTo(QUANTITY);

        verify(orderItemsValidator).validate(order.getOrderItems());
        verify(createOrderPort).create(order);
    }

    @Test
    void shouldNotCreateOrderWhenValidationFails() {
        // given
        CreateOrderUseCase.OrderItem orderItem = new CreateOrderUseCase.OrderItem(PRODUCT_IDENTIFIER, QUANTITY);
        CreateOrderUseCase.Command command = new CreateOrderUseCase.Command(CUSTOMER_KEY, List.of(orderItem));
        doThrow(RuntimeException.class).when(orderItemsValidator).validate(anyList());
        // when
        assertThrows(RuntimeException.class, () -> createOrderService.apply(command));
        // then
        verifyNoInteractions(createOrderPort);
    }
}
