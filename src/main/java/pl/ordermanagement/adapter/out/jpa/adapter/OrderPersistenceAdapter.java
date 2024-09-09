package pl.ordermanagement.adapter.out.jpa.adapter;

import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.ordermanagement.adapter.out.jpa.entity.PurchaseOrderEntity;
import pl.ordermanagement.adapter.out.jpa.mapper.OrderEntityMapper;
import pl.ordermanagement.adapter.out.jpa.repository.OrderRepository;
import pl.ordermanagement.application.order.api.port.out.FindOrderByOrderKeyPort;
import pl.ordermanagement.application.order.api.port.out.CreateOrderPort;
import pl.ordermanagement.application.order.api.port.out.UpdateOrderPort;
import pl.ordermanagement.application.order.domain.model.Order;
import pl.ordermanagement.application.shared.datatype.OrderKey;


@Component
@RequiredArgsConstructor
class OrderPersistenceAdapter implements CreateOrderPort, UpdateOrderPort, FindOrderByOrderKeyPort {
    private final OrderRepository repository;
    private final OrderEntityMapper mapper;

    @Override
    public Order create(Order order) {
        PurchaseOrderEntity purchaseOrderEntity = mapToNewEntity(order);
        PurchaseOrderEntity savedPurchaseOrderEntity = repository.save(purchaseOrderEntity);
        return mapper.map(savedPurchaseOrderEntity);
    }

    @Override
    public Order update(Order order) {
        PurchaseOrderEntity purchaseOrderEntity = mapToExistingEntity(order);
        PurchaseOrderEntity updatedPurchaseOrderEntity = repository.save(purchaseOrderEntity);
        return mapper.map(updatedPurchaseOrderEntity);
    }

    @Override
    public Optional<Order> findByOrderKey(OrderKey orderKey) {
        return repository.findByOrderKey(orderKey).map(mapper::map);
    }

    private PurchaseOrderEntity mapToNewEntity(Order order) {
        PurchaseOrderEntity purchaseOrderEntity = mapper.mapToNewEntity(order);
        purchaseOrderEntity.getOrderItems().forEach(orderItem -> orderItem.setOrder(purchaseOrderEntity));
        return purchaseOrderEntity;
    }

    private PurchaseOrderEntity mapToExistingEntity(Order order) {
        PurchaseOrderEntity exsistingPurchaseOrderEntity = repository.findById(order.getId()).orElseThrow(EntityNotFoundException::new);
        mapper.mapExisting(order, exsistingPurchaseOrderEntity);
        exsistingPurchaseOrderEntity.getOrderItems().forEach(orderItem -> orderItem.setOrder(exsistingPurchaseOrderEntity));
        return exsistingPurchaseOrderEntity;
    }
}
