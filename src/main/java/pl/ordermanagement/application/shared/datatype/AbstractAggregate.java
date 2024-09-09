package pl.ordermanagement.application.shared.datatype;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractAggregate {

    private Long id;

    private Long version;

    private LocalDateTime created;

}
