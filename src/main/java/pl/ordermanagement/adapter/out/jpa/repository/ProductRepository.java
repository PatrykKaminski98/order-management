package pl.ordermanagement.adapter.out.jpa.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ordermanagement.adapter.out.jpa.entity.ProductEntity;
import pl.ordermanagement.application.shared.datatype.ProductIdentifier;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByProductIdentifierIn(Set<ProductIdentifier> productIdentifiers);
}
