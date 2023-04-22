package pl.zajavka.business.domain;

import pl.zajavka.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.OffsetDateTime;

public class StoreFixtures {

    public static Customer someCustomer() {
        return Customer.builder()
            .userName("zajavkowyzajavkowicz")
            .email("zajavkowyzajavkowicz@gmail.com")
            .name("Administrator")
            .surname("Zajavka")
            .dateOfBirth(LocalDate.of(1984, 10, 2))
            .telephoneNumber("+56847542165")
            .build();
    }

    public static Opinion someOpinion(Customer customer, Product product) {
        return Opinion.builder()
            .customer(customer)
            .product(product)
            .stars((byte) 4)
            .comment("My opinion is my opinion")
            .dateTime(OffsetDateTime.of(2020, 1, 4, 12, 44, 30, 0, ZoneOffset.ofHours(5)))
            .build();
    }

    public static Producer someProducer() {
        return Producer.builder()
            .producerName("Best Producer")
            .address("Best Address")
            .build();
    }

    public static Product someProduct(Producer producer) {
        return Product.builder()
            .productCode("6HsQ")
            .productName("Charger")
            .productPrice(BigDecimal.valueOf(165.52))
            .adultsOnly(false)
            .description("Some Product Description")
            .producer(producer)
            .build();
    }

    public static Purchase somePurchase(Customer customer, Product product) {
        return Purchase.builder()
            .customer(customer)
            .product(product)
            .quantity(6)
            .dateTime(OffsetDateTime.of(2020, 1, 2, 11, 34, 10, 0, ZoneOffset.ofHours(2)))
            .build();
    }

}
