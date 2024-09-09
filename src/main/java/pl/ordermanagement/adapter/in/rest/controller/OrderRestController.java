package pl.ordermanagement.adapter.in.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ordermanagement.adapter.in.rest.mapper.OrderMapper;

import pl.ordermanagement.adapter.in.rest.model.CreateOrderRequest;
import pl.ordermanagement.adapter.in.rest.model.Order;
import pl.ordermanagement.adapter.in.rest.model.OrderDetails;
import pl.ordermanagement.application.order.api.port.in.usecase.CancelOrderUseCase;
import pl.ordermanagement.application.order.api.port.in.usecase.CompleteOrderUseCase;
import pl.ordermanagement.application.order.api.port.in.usecase.ConfirmOrderUseCase;
import pl.ordermanagement.application.order.api.port.in.usecase.CreateOrderUseCase;
import pl.ordermanagement.application.order.api.port.in.query.GetOrderDetailsQuery;
import pl.ordermanagement.application.shared.datatype.OrderKey;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
public class OrderRestController {
    private final GetOrderDetailsQuery getOrderDetailsQuery;
    private final CreateOrderUseCase createOrderUseCase;
    private final ConfirmOrderUseCase confirmOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;
    private final CompleteOrderUseCase completeOrderUseCase;
    private final OrderMapper mapper;

    @GetMapping(path = "/{orderKey}")
    public ResponseEntity<OrderDetails> getOrder(@PathVariable OrderKey orderKey) {
        GetOrderDetailsQuery.QueryResult queryResult = getOrderDetailsQuery.execute(orderKey);
        return ResponseEntity.ok(mapper.map(queryResult));
    }

    @PostMapping(path = "/create")
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request) {
        CreateOrderUseCase.Command command = mapper.map(request);
        pl.ordermanagement.application.order.domain.model.Order createdOrder = createOrderUseCase.apply(command);
        return ResponseEntity.ok(mapper.map(createdOrder));
    }

    @PutMapping(path = "/confirm/{orderKey}")
    public ResponseEntity<String> confirmOrder(@PathVariable OrderKey orderKey) {
        confirmOrderUseCase.apply(orderKey);
        return ResponseEntity.ok("Order has been confirmed");
    }

    @PutMapping(path = "/cancel/{orderKey}")
    public ResponseEntity<String> declineOrder(@PathVariable OrderKey orderKey) {
        cancelOrderUseCase.apply(orderKey);
        return ResponseEntity.ok("Order has been canceled");
    }

    @PutMapping(path = "/complete/{orderKey}")
    public ResponseEntity<String> completeOrder(@PathVariable OrderKey orderKey) {
        completeOrderUseCase.apply(orderKey);
        return ResponseEntity.ok("Order has been completed");
    }


}
