package pl.ordermanagement.adapter.out.jpa.adapter;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ordermanagement.adapter.out.jpa.entity.OrderItemEntity;
import pl.ordermanagement.adapter.out.jpa.entity.PurchaseOrderEntity;
import pl.ordermanagement.adapter.out.jpa.mapper.OrderEntityMapper;
import pl.ordermanagement.adapter.out.jpa.mapper.generated.OrderEntityMapperImpl;
import pl.ordermanagement.adapter.out.jpa.repository.OrderRepository;
import pl.ordermanagement.application.order.domain.model.Order;
import pl.ordermanagement.application.order.domain.model.OrderItem;
import pl.ordermanagement.application.shared.datatype.CustomerKey;
import pl.ordermanagement.application.shared.datatype.OrderKey;
import pl.ordermanagement.application.shared.datatype.ProductIdentifier;
import pl.ordermanagement.application.shared.datatype.Status;

@ExtendWith(MockitoExtension.class)
class OrderPersistenceAdapterTest {
    private static final OrderKey ORDER_KEY = new OrderKey(UUID.randomUUID().toString());
    private static final CustomerKey CUSTOMER_KEY = new CustomerKey("KAMIN12");
    private static final ProductIdentifier PRODUCT_IDENTIFIER = new ProductIdentifier("PL2324");
    private static final int QUANTITY = 5;
    private static final long ENTITY_ID = 1L;
    private static final Status CONFIRMED = Status.CONFIRMED;
    private static final Status CREATED = Status.CREATED;
    @Mock
    private OrderRepository repository;
    private final OrderEntityMapper mapper = new OrderEntityMapperImpl();

    private OrderPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new OrderPersistenceAdapter(repository, mapper);
    }

    @Test
    void shouldCreateOrder() {
        // given
        Order order = createOrder();
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        // when
        Order createdOrder = adapter.create(order);
        // then
        assertThat(createdOrder.getOrderKey()).isEqualTo(ORDER_KEY);
        assertThat(createdOrder.getCustomerKey()).isEqualTo(CUSTOMER_KEY);
        assertThat(createdOrder.getStatus()).isEqualTo(CONFIRMED);
        assertThat(createdOrder.getOrderItems()).hasSize(1);
        assertThat(createdOrder.getOrderItems().getFirst().getProductIdentifier()).isEqualTo(PRODUCT_IDENTIFIER);
        assertThat(createdOrder.getOrderItems().getFirst().getQuantity()).isEqualTo(QUANTITY);
    }

    @Test
    void shouldUpdateOrder() {
        // given
        Order order = createOrder();
        order.setId(ENTITY_ID);
        PurchaseOrderEntity purchaseOrderEntity = createPurchaseOrderEntity();
        when(repository.findById(anyLong())).thenReturn(Optional.of(purchaseOrderEntity));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        // when
        Order updatedOrder = adapter.update(order);
        // then
        assertThat(updatedOrder.getStatus()).isEqualTo(order.getStatus());
        assertThat(updatedOrder.getOrderKey()).isEqualTo(ORDER_KEY);
        assertThat(updatedOrder.getCustomerKey()).isEqualTo(CUSTOMER_KEY);
        assertThat(updatedOrder.getStatus()).isEqualTo(CONFIRMED);
        assertThat(updatedOrder.getOrderItems()).hasSize(1);
        assertThat(updatedOrder.getOrderItems().getFirst().getProductIdentifier()).isEqualTo(PRODUCT_IDENTIFIER);
        assertThat(updatedOrder.getOrderItems().getFirst().getQuantity()).isEqualTo(QUANTITY);
    }

    @Test
    void shouldThrowExceptionWhenEntityWithGivenOrderIDDoesNotExsist() {
        // given
        Order order = createOrder();
        order.setId(ENTITY_ID);
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        // when + then
        assertThrows(EntityNotFoundException.class, () -> adapter.update(order));
    }

    @Test
    void shouldFindOrderByOrderKey() {
        // given
        when(repository.findByOrderKey(ORDER_KEY)).thenReturn(Optional.of(createPurchaseOrderEntity()));
        // when
        Optional<Order> foundOrderOptional = adapter.findByOrderKey(ORDER_KEY);
        // then
        assertThat(foundOrderOptional).isPresent();
        Order foundOrder = foundOrderOptional.get();
        assertThat(foundOrder.getOrderKey()).isEqualTo(ORDER_KEY);
        assertThat(foundOrder.getCustomerKey()).isEqualTo(CUSTOMER_KEY);
        assertThat(foundOrder.getStatus()).isEqualTo(CREATED);
        assertThat(foundOrder.getOrderItems()).hasSize(1);
        assertThat(foundOrder.getOrderItems().getFirst().getProductIdentifier()).isEqualTo(PRODUCT_IDENTIFIER);
        assertThat(foundOrder.getOrderItems().getFirst().getQuantity()).isEqualTo(QUANTITY);
    }

    private Order createOrder() {
        return new Order(ORDER_KEY, CUSTOMER_KEY, CONFIRMED, List.of(new OrderItem(PRODUCT_IDENTIFIER, QUANTITY)));
    }

    private PurchaseOrderEntity createPurchaseOrderEntity() {
        PurchaseOrderEntity purchaseOrderEntity = new PurchaseOrderEntity(ENTITY_ID, ORDER_KEY, CUSTOMER_KEY, CREATED, null);
        OrderItemEntity orderItemEntity = new OrderItemEntity(ENTITY_ID, PRODUCT_IDENTIFIER, Long.valueOf(QUANTITY), purchaseOrderEntity);
        ArrayList<OrderItemEntity> orderItemEntities = new ArrayList<>();
        orderItemEntities.add(orderItemEntity);
        purchaseOrderEntity.setOrderItems(orderItemEntities);
        return purchaseOrderEntity;
    }
}
