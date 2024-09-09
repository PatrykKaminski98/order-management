package pl.ordermanagement.adapter.in.rest.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.ordermanagement.adapter.in.rest.mapper.OrderMapper;
import pl.ordermanagement.adapter.in.rest.mapper.generated.OrderMapperImpl;
import pl.ordermanagement.application.order.api.port.in.query.GetOrderDetailsQuery;
import pl.ordermanagement.application.order.api.port.in.usecase.CancelOrderUseCase;
import pl.ordermanagement.application.order.api.port.in.usecase.CompleteOrderUseCase;
import pl.ordermanagement.application.order.api.port.in.usecase.ConfirmOrderUseCase;
import pl.ordermanagement.application.order.api.port.in.usecase.CreateOrderUseCase;
import pl.ordermanagement.application.shared.datatype.OrderKey;

@ExtendWith(MockitoExtension.class)
class OrderRestControllerTest {
    private static final String ORDER_KEY_STRING = UUID.randomUUID().toString();
    private static final OrderKey ORDER_KEY = new OrderKey(ORDER_KEY_STRING);
    private static final String ORDER_CONTROLLER_API_URL = "/api/order";
    @Mock
    private GetOrderDetailsQuery getOrderDetailsQuery;
    @Mock
    private CreateOrderUseCase createOrderUseCase;
    @Mock
    private ConfirmOrderUseCase confirmOrderUseCase;
    @Mock
    private CancelOrderUseCase cancelOrderUseCase;
    @Mock
    private CompleteOrderUseCase completeOrderUseCase;
    private final OrderMapper mapper = new OrderMapperImpl();
    private OrderRestController controller;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        controller = new OrderRestController(getOrderDetailsQuery, createOrderUseCase, confirmOrderUseCase, cancelOrderUseCase, completeOrderUseCase, mapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    void shouldCreateOrder() throws Exception {
        // when
        mockMvc.perform(post(ORDER_CONTROLLER_API_URL + "/create", ORDER_KEY_STRING)
                        .content(getCreateOrderRequestBody())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(createOrderUseCase).apply(any(CreateOrderUseCase.Command.class));
        // then
    }

    @Test
    void shouldReturnOrderDetails() throws Exception {
        // given
        // when + then
        mockMvc.perform(get(ORDER_CONTROLLER_API_URL + "/{orderKey}", ORDER_KEY_STRING))
                .andExpect(status().isOk());
        verify(getOrderDetailsQuery).execute(ORDER_KEY);
    }

    @Test
    void shouldConfirmOrder() throws Exception {
        // given
        // when + then
        mockMvc.perform(put(ORDER_CONTROLLER_API_URL + "/confirm/{orderKey}", ORDER_KEY_STRING))
                .andExpect(status().isOk());
        verify(confirmOrderUseCase).apply(ORDER_KEY);
    }

    @Test
    void shouldReturnCancelOrder() throws Exception {
        // given
        // when + then
        mockMvc.perform(put(ORDER_CONTROLLER_API_URL + "/cancel/{orderKey}", ORDER_KEY_STRING))
                .andExpect(status().isOk());
        verify(cancelOrderUseCase).apply(ORDER_KEY);
    }

    @Test
    void shouldReturnCompleteOrder() throws Exception {
        // given
        // when + then
        mockMvc.perform(put(ORDER_CONTROLLER_API_URL + "/complete/{orderKey}", ORDER_KEY_STRING))
                .andExpect(status().isOk());
        verify(completeOrderUseCase).apply(ORDER_KEY);
    }

    private String getCreateOrderRequestBody(){
        return "{\"customerKey\":\"KAMIN12\",\"orderItems\":[{\"productIdentifier\":\"PL2324\",\"quantity\":5}]}";
    }
}
