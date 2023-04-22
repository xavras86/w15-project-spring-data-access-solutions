package pl.zajavka.infrastructure.database;

import org.springframework.stereotype.Component;
import pl.zajavka.domain.Customer;
import pl.zajavka.domain.Opinion;
import pl.zajavka.domain.Producer;
import pl.zajavka.domain.Product;
import pl.zajavka.domain.Purchase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class DataBaseDataMapper {

    public static final DateTimeFormatter DATABASE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX");

    Map<String, Object> mapCustomerParams(final Customer customer) {
        return Map.of(
            "USER_NAME", customer.getUserName(),
            "EMAIL", customer.getEmail(),
            "NAME", customer.getName(),
            "SURNAME", customer.getSurname(),
            "DATE_OF_BIRTH", customer.getDateOfBirth(),
            "TELEPHONE_NUMBER", customer.getTelephoneNumber()
        );
    }

    Map<String, Object> mapProducerParams(final Producer producer) {
        return Map.of(
            "PRODUCER_NAME", producer.getProducerName(),
            "ADDRESS", producer.getAddress()
        );
    }

    Map<String, Object> mapProductParams(final Product product) {
        return Map.of(
            "PRODUCT_CODE", product.getProductCode(),
            "PRODUCT_NAME", product.getProductName(),
            "PRODUCT_PRICE", product.getProductPrice(),
            "ADULTS_ONLY", product.getAdultsOnly(),
            "DESCRIPTION", product.getDescription(),
            "PRODUCER_ID", product.getProducer().getId()
        );
    }


    Map<String, Object> mapPurchaseParams(final Purchase purchase) {
        return Map.of(
            "CUSTOMER_ID", purchase.getCustomer().getId(),
            "PRODUCT_ID", purchase.getProduct().getId(),
            "QUANTITY", purchase.getQuantity(),
            "DATE_TIME", DATABASE_DATE_FORMAT.format(purchase.getDateTime())
        );
    }

    Map<String, Object> mapOpinionParams(final Opinion opinion) {
        return Map.of(
            "CUSTOMER_ID", opinion.getCustomer().getId(),
            "PRODUCT_ID", opinion.getProduct().getId(),
            "STARS", opinion.getStars(),
            "COMMENT", opinion.getComment(),
            "DATE_TIME", DATABASE_DATE_FORMAT.format(opinion.getDateTime())
        );
    }

    @SuppressWarnings("unused")
    Customer customerRowMapper(ResultSet resultSet, int rowNum) throws SQLException {
        return Customer.builder()
            .id(resultSet.getLong("ID"))
            .userName(resultSet.getString("USER_NAME"))
            .email(resultSet.getString("EMAIL"))
            .name(resultSet.getString("NAME"))
            .surname(resultSet.getString("SURNAME"))
            .dateOfBirth(LocalDate.parse(resultSet.getString("DATE_OF_BIRTH")))
            .telephoneNumber(resultSet.getString("TELEPHONE_NUMBER"))
            .build();
    }

    @SuppressWarnings("unused")
    Producer producerRowMapper(ResultSet resultSet, int rowNum) throws SQLException {
        return Producer.builder()
            .id(resultSet.getLong("ID"))
            .producerName(resultSet.getString("PRODUCER_NAME"))
            .address(resultSet.getString("ADDRESS"))
            .build();
    }

    @SuppressWarnings("unused")
    Product productRowMapper(ResultSet resultSet, int rowNum) throws SQLException {
        return Product.builder()
            .id(resultSet.getLong("ID"))
            .productCode(resultSet.getString("PRODUCT_CODE"))
            .productName(resultSet.getString("PRODUCT_NAME"))
            .productPrice(resultSet.getBigDecimal("PRODUCT_PRICE"))
            .adultsOnly(resultSet.getBoolean("ADULTS_ONLY"))
            .description(resultSet.getString("DESCRIPTION"))
            .producer(Producer.builder().id(resultSet.getLong("PRODUCER_ID")).build())
            .build();
    }

    @SuppressWarnings("unused")
    Purchase purchaseRowMapper(ResultSet resultSet, int rowNum) throws SQLException {
        return Purchase.builder()
            .id(resultSet.getLong("ID"))
            .customer(Customer.builder().id(resultSet.getLong("CUSTOMER_ID")).build())
            .product(Product.builder().id(resultSet.getLong("PRODUCT_ID")).build())
            .quantity(resultSet.getInt("QUANTITY"))
            .dateTime(OffsetDateTime.parse(resultSet.getString("DATE_TIME"), DATABASE_DATE_FORMAT).withOffsetSameInstant(ZoneOffset.UTC))
            .build();
    }

    @SuppressWarnings("unused")
    Opinion opinionRowMapper(ResultSet resultSet, int rowNum) throws SQLException {
        return Opinion.builder()
            .id(resultSet.getLong("ID"))
            .customer(Customer.builder().id(resultSet.getLong("CUSTOMER_ID")).build())
            .product(Product.builder().id(resultSet.getLong("PRODUCT_ID")).build())
            .stars(resultSet.getByte("STARS"))
            .comment(resultSet.getString("COMMENT"))
            .dateTime(OffsetDateTime.parse(resultSet.getString("DATE_TIME"), DATABASE_DATE_FORMAT).withOffsetSameInstant(ZoneOffset.UTC))
            .build();
    }
}
