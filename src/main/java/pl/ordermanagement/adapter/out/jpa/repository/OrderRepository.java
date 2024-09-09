package pl.ordermanagement.adapter.out.jpa.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import pl.ordermanagement.adapter.out.jpa.entity.PurchaseOrderEntity;
import pl.ordermanagement.application.shared.datatype.OrderKey;

public interface OrderRepository extends CrudRepository<PurchaseOrderEntity, Long>{
    Optional<PurchaseOrderEntity> findByOrderKey(OrderKey orderKey);
}
