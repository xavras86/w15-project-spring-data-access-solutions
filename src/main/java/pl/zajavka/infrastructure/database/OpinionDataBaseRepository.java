package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.OpinionRepository;
import pl.zajavka.domain.Opinion;
import pl.zajavka.infrastructure.configuration.DatabaseConfiguration;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@AllArgsConstructor
public class OpinionDataBaseRepository implements OpinionRepository {

    private static final String SELECT_ALL = "SELECT * FROM OPINION ORDER BY DATE_TIME";
    private static final String SELECT_ALL_WHERE_CUSTOMER_EMAIL =
        "SELECT * FROM OPINION AS OPN " +
            "INNER JOIN CUSTOMER AS CUS ON CUS.ID = OPN.CUSTOMER_ID WHERE CUS.EMAIL = :email " +
            "ORDER BY DATE_TIME";
    private static final String SELECT_ALL_WHERE_PRODUCT_CODE =
        "SELECT * FROM OPINION AS OPN " +
            "INNER JOIN PRODUCT AS PRD ON PRD.ID = OPN.PRODUCT_ID WHERE PRD.PRODUCT_CODE = :productCode " +
            "ORDER BY DATE_TIME";
    private static final String SELECT_UNWANTED_OPINIONS = "SELECT * FROM OPINION WHERE STARS < 4";
    private static final String SELECT_UNWANTED_OPINIONS_FOR_EMAIL =
        "SELECT * FROM OPINION WHERE STARS < 4 AND CUSTOMER_ID IN (SELECT ID FROM CUSTOMER WHERE EMAIL = :email)";

    // https://www.postgresqltutorial.com/postgresql-tutorial/postgresql-delete-join/
    // W przypadku PostgreSql to nie zadziała:
    // DELETE FROM OPINION AS OPN INNER JOIN CUSTOMER AS CUS ON CUS.ID = OPN.CUSTOMER_ID WHERE CUS.EMAIL = :email
    private static final String DELETE_ALL_WHERE_CUSTOMER_EMAIL =
        "DELETE FROM OPINION WHERE CUSTOMER_ID IN (SELECT ID FROM CUSTOMER WHERE EMAIL = :email)";
    private static final String DELETE_ALL_WHERE_PRODUCT_CODE =
        "DELETE FROM OPINION WHERE PRODUCT_ID IN (SELECT ID FROM PRODUCT WHERE PRODUCT_CODE = :productCode)";
    private static final String DELETE_ALL = "DELETE FROM OPINION";
    private static final String DELETE_UNWANTED_OPINIONS = "DELETE FROM OPINION WHERE STARS < 4;";

    private final SimpleDriverDataSource simpleDriverDataSource;

    private final DataBaseDataMapper dataBaseDataMapper;

    @Override
    public Opinion createOpinion(final Opinion opinion) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(simpleDriverDataSource)
            .withTableName(DatabaseConfiguration.OPINION_TABLE)
            .usingGeneratedKeyColumns(DatabaseConfiguration.OPINION_TABLE_PKEY.toLowerCase());

        Map<String, Object> params = dataBaseDataMapper.mapOpinionParams(opinion);
        Number opinionId = jdbcInsert.executeAndReturnKey(params);
        Opinion opinionCreated = opinion.withId((long) opinionId.intValue());
        log.info("Opinion created: [{}]", opinionCreated);
        return opinionCreated;
    }

    @Override
    public List<Opinion> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(simpleDriverDataSource);

        List<Opinion> result = jdbcTemplate.query(SELECT_ALL, dataBaseDataMapper::opinionRowMapper);
        log.debug("All opinions: [{}]", result);
        return result;
    }

    @Override
    public List<Opinion> findAll(final String email) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);
        return jdbcTemplate.query(SELECT_ALL_WHERE_CUSTOMER_EMAIL, Map.of("email", email), dataBaseDataMapper::opinionRowMapper);
    }

    @Override
    public List<Opinion> findAllByProduct(final String productCode) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);
        return jdbcTemplate.query(SELECT_ALL_WHERE_PRODUCT_CODE, Map.of("productCode", productCode), dataBaseDataMapper::opinionRowMapper);
    }

    @Override
    public List<Opinion> findUnwantedOpinions() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(simpleDriverDataSource);
        return jdbcTemplate.query(SELECT_UNWANTED_OPINIONS, dataBaseDataMapper::opinionRowMapper);
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

    @Override
    public void removeUnwantedOpinions() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(simpleDriverDataSource);

        int result = jdbcTemplate.update(DELETE_UNWANTED_OPINIONS);
        log.debug("Removed: [{}]  opinions", result);
    }

    @Override
    public boolean customerGivesUnwantedOpinions(final String email) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);
        return !jdbcTemplate.query(SELECT_UNWANTED_OPINIONS_FOR_EMAIL, Map.of("email", email), dataBaseDataMapper::opinionRowMapper).isEmpty();
    }
}
