package pl.zajavka.business;

import org.springframework.stereotype.Service;
import pl.zajavka.domain.Customer;
import pl.zajavka.domain.Opinion;
import pl.zajavka.domain.Producer;
import pl.zajavka.domain.Product;
import pl.zajavka.domain.Purchase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.stream.IntStream;

@Service
public class RandomDataPreparationService {

    public Customer someCustomer() {
        String userName = randomString(65, 90, 1) + randomString(97, 122, 10) + randomString(48, 57, 2);
        return Customer.builder()
            .userName(userName)
            .email(userName + "@gmail.com")
            .name("name")
            .surname("surname")
            .dateOfBirth(LocalDate.of(1984, 10, 2))
            .telephoneNumber("+" + randomString(48, 57, 11))
            .build();
    }

    public Opinion someOpinion(Customer customer, Product product) {
        return Opinion.builder()
            .customer(customer)
            .product(product)
            .stars((byte) 4)
            .comment("My opinion is my opinion")
            .dateTime(OffsetDateTime.of(2020, 1, 4, 12, 44, 30, 1, ZoneOffset.ofHours(5)))
            .build();
    }

    public Producer someProducer() {
        return Producer.builder()
            .producerName(randomString(65, 90, 1) + randomString(97, 122, 10))
            .address("address")
            .build();
    }

    public Product someProduct(Producer producer) {
        return Product.builder()
            .productCode(randomString(65, 90, 3) + randomString(97, 122, 4) + randomString(48, 57, 2))
            .productName("productName")
            .productPrice(BigDecimal.valueOf(165.52))
            .adultsOnly(false)
            .description("someDescription")
            .producer(producer)
            .build();
    }

    public Purchase somePurchase(Customer customer, Product product) {
        return Purchase.builder()
            .customer(customer)
            .product(product)
            .quantity(randomInt(1, 5))
            .dateTime(OffsetDateTime.of(2020, 1, 2, 11, 34, 10, 754, ZoneOffset.ofHours(2)))
            .build();
    }

    private String randomString(final int min, final int max, final int length) {
        return IntStream.range(0, length)
            .boxed()
            .reduce("", (previous, next) -> previous + (char) randomInt(min, max), String::concat);
    }

    private int randomInt(final int min, final int max) {
        return new Random().nextInt(max - min) + min;
    }
}
