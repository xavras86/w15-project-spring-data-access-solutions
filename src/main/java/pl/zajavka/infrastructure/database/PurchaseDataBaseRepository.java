package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.PurchaseRepository;
import pl.zajavka.domain.Purchase;
import pl.zajavka.infrastructure.configuration.DatabaseConfiguration;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class PurchaseDataBaseRepository implements PurchaseRepository {

    private static final String SELECT_WHERE_ID = "SELECT * FROM PURCHASE WHERE ID = :id";
    private static final String SELECT_ALL = "SELECT * FROM PURCHASE ORDER BY DATE_TIME";
    private static final String SELECT_ALL_WHERE_CUSTOMER_EMAIL =
        "SELECT * FROM PURCHASE AS PUR " +
            "INNER JOIN CUSTOMER AS CUS ON CUS.ID = PUR.CUSTOMER_ID WHERE CUS.EMAIL = :email " +
            "ORDER BY DATE_TIME";
    private static final String SELECT_ALL_WHERE_PRODUCT_CODE =
        "SELECT * FROM PURCHASE AS PUR " +
            "INNER JOIN PRODUCT AS PROD ON PROD.ID = PUR.PRODUCT_ID " +
            "WHERE PROD.PRODUCT_CODE = :productCode " +
            "ORDER BY DATE_TIME";
    private static final String SELECT_ALL_WHERE_CUSTOMER_EMAIL_AND_PRODUCT_CODE =
        "SELECT * FROM PURCHASE AS PUR " +
            "INNER JOIN CUSTOMER AS CUS ON CUS.ID = PUR.CUSTOMER_ID " +
            "INNER JOIN PRODUCT AS PROD ON PROD.ID = PUR.PRODUCT_ID " +
            "WHERE CUS.EMAIL = :email " +
            "AND PROD.PRODUCT_CODE = :productCode " +
            "ORDER BY DATE_TIME";
    private static final String DELETE_ALL_WHERE_CUSTOMER_EMAIL =
        "DELETE FROM PURCHASE WHERE CUSTOMER_ID IN (SELECT ID FROM CUSTOMER WHERE EMAIL = :email)";
    private static final String DELETE_ALL_WHERE_PRODUCT_CODE =
        "DELETE FROM PURCHASE WHERE PRODUCT_ID IN (SELECT ID FROM PRODUCT WHERE PRODUCT_CODE = :productCode)";
    private static final String DELETE_ALL = "DELETE FROM PURCHASE";

    private final SimpleDriverDataSource simpleDriverDataSource;

    private final DataBaseDataMapper dataBaseDataMapper;

    @Override
    public Purchase createPurchase(final Purchase purchase) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(simpleDriverDataSource)
            .withTableName(DatabaseConfiguration.PURCHASE_TABLE)
            .usingGeneratedKeyColumns(DatabaseConfiguration.PURCHASE_TABLE_PKEY.toLowerCase());

        Map<String, Object> purchaseParams = dataBaseDataMapper.mapPurchaseParams(purchase);

        Number purchaseId = jdbcInsert.executeAndReturnKey(purchaseParams);
        Purchase purchaseCreated = purchase.withId((long) purchaseId.intValue());
        log.info("Purchase created: [{}]", purchaseCreated);
        return purchaseCreated;
    }

    @Override
    public Optional<Purchase> find(Long purchaseId) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);

        try {
            return Optional.ofNullable(jdbcTemplate
                .queryForObject(SELECT_WHERE_ID, Map.of("id", purchaseId), dataBaseDataMapper::purchaseRowMapper));
        } catch (Exception e) {
            log.warn("Could not find purchase with id: [{}]", purchaseId);
            return Optional.empty();
        }
    }

    @Override
    public List<Purchase> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(simpleDriverDataSource);

        List<Purchase> result = jdbcTemplate.query(SELECT_ALL, dataBaseDataMapper::purchaseRowMapper);
        log.debug("All purchases: [{}]", result);
        return result;
    }

    @Override
    public List<Purchase> findAll(final String email) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);
        return jdbcTemplate.query(SELECT_ALL_WHERE_CUSTOMER_EMAIL, Map.of("email", email), dataBaseDataMapper::purchaseRowMapper);
    }

    @Override
    public List<Purchase> findAll(final String email, final String productCode) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);
        return jdbcTemplate.query(
            SELECT_ALL_WHERE_CUSTOMER_EMAIL_AND_PRODUCT_CODE,
            Map.of(
                "email", email,
                "productCode", productCode
            ),
            dataBaseDataMapper::purchaseRowMapper);
    }


    @Override
    public List<Purchase> findAllByProductCode(final String productCode) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);
        return jdbcTemplate.query(SELECT_ALL_WHERE_PRODUCT_CODE, Map.of("productCode", productCode), dataBaseDataMapper::purchaseRowMapper);
    }

    @Override
    public void removeAll(final String email) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);
        jdbcTemplate.update(DELETE_ALL_WHERE_CUSTOMER_EMAIL, Map.of("email", email));
    }

    @Override
    public void removeAllByProduct(final String productCode) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);
        jdbcTemplate.update(DELETE_ALL_WHERE_PRODUCT_CODE, Map.of("productCode", productCode));
    }

    @Override
    public void removeAll() {
        new JdbcTemplate(simpleDriverDataSource).update(DELETE_ALL);
    }
}
