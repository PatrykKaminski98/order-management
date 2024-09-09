package pl.ordermanagement.adapter.out.jpa.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.ordermanagement.application.shared.datatype.ProductIdentifier;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PRODUCT")
public class ProductEntity extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private ProductIdentifier productIdentifier;
    @NotNull
    private String name;
    private String producer;
    @NotNull
    private BigDecimal price;
    @NotNull
    private int quantityAvailable;

}
