package pl.zajavka.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;

@With
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    Long id;
    String productCode;
    String productName;
    BigDecimal productPrice;
    Boolean adultsOnly;
    String description;
    Producer producer;
}
