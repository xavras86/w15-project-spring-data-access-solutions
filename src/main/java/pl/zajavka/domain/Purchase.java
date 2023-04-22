package pl.zajavka.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.OffsetDateTime;

@With
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {
    Long id;
    Customer customer;
    Product product;
    Integer quantity;
    OffsetDateTime dateTime;
}
