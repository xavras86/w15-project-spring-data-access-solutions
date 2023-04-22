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
public class Opinion {
    Long id;
    Customer customer;
    Product product;
    Byte stars;
    String comment;
    OffsetDateTime dateTime;
    // https://stackoverflow.com/questions/58434148/what-is-better-to-persist-in-database-offsetdatetime-or-instant
}
