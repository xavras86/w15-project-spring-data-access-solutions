package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.ProductRepository;
import pl.zajavka.domain.Product;
import pl.zajavka.infrastructure.configuration.DatabaseConfiguration;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class ProductDataBaseRepository implements ProductRepository {

    private static final String SELECT_ALL_WHERE_ID = "SELECT * FROM PRODUCT WHERE ID = :id";
    private static final String SELECT_ALL_WHERE_PRODUCT_CODE = "SELECT * FROM PRODUCT WHERE PRODUCT_CODE = :productCode";
    private static final String SELECT_ALL = "SELECT * FROM PRODUCT";
    private static final String DELETE_WHERE_PRODUCT_CODE = "DELETE FROM PRODUCT WHERE PRODUCT_CODE = :productCode";
    private static final String DELETE_ALL = "DELETE FROM PRODUCT";

    private final SimpleDriverDataSource simpleDriverDataSource;

    private final DataBaseDataMapper dataBaseDataMapper;

    @Override
    public Product createProduct(final Product product) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(simpleDriverDataSource)
            .withTableName(DatabaseConfiguration.PRODUCT_TABLE)
            .usingGeneratedKeyColumns(DatabaseConfiguration.PRODUCT_TABLE_PKEY.toLowerCase());

        Map<String, Object> params = dataBaseDataMapper.mapProductParams(product);
        Number productId = jdbcInsert.executeAndReturnKey(params);
        Product productCreated = product.withId((long) productId.intValue());
        log.info("Product created: [{}]", productCreated);
        return productCreated;
    }

    @Override
    public Optional<Product> find(final Long productId) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                SELECT_ALL_WHERE_ID,
                Map.of("id", productId),
                dataBaseDataMapper::productRowMapper));
        } catch (Exception e) {
            log.warn("Trying to find non-existing productId: [{}]", productId);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Product> find(final String productCode) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                SELECT_ALL_WHERE_PRODUCT_CODE,
                Map.of("productCode", productCode),
                dataBaseDataMapper::productRowMapper));
        } catch (Exception e) {
            log.warn("Trying to find non-existing productCode: [{}]", productCode);
            return Optional.empty();
        }
    }

    @Override
    public List<Product> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(simpleDriverDataSource);
        List<Product> result = jdbcTemplate.query(SELECT_ALL, dataBaseDataMapper::productRowMapper);
        log.debug("All products: [{}]", result);
        return result;
    }

    @Override
    public void remove(final String productCode) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);
        jdbcTemplate.update(DELETE_WHERE_PRODUCT_CODE, Map.of("productCode", productCode));
    }

    @Override
    public void removeAll() {
        new JdbcTemplate(simpleDriverDataSource).update(DELETE_ALL);
    }
}
