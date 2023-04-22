package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.CustomerRepository;
import pl.zajavka.domain.Customer;
import pl.zajavka.infrastructure.configuration.DatabaseConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class CustomerDataBaseRepository implements CustomerRepository {

    private static final String SELECT_ALL = "SELECT * FROM CUSTOMER";
    private static final String SELECT_ALL_WHERE_EMAIL = "SELECT * FROM CUSTOMER WHERE EMAIL = :email";
    private static final String DELETE_WHERE_EMAIL = "DELETE FROM CUSTOMER WHERE EMAIL = :email";
    private static final String DELETE_ALL = "DELETE FROM CUSTOMER";

    private final SimpleDriverDataSource simpleDriverDataSource;

    private final DataBaseDataMapper dataBaseDataMapper;

    @Override
    public Customer createCustomer(final Customer customer) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(simpleDriverDataSource)
            .withTableName(DatabaseConfiguration.CUSTOMER_TABLE)
            .usingGeneratedKeyColumns(DatabaseConfiguration.CUSTOMER_TABLE_PKEY.toLowerCase());

        // opcja mniej automatyczna
        Map<String, Object> params = dataBaseDataMapper.mapCustomerParams(customer);

        // opcja bardziej automatyczna
        final BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(customer);

        Number customerId = jdbcInsert.executeAndReturnKey(parameterSource);
        return customer.withId((long) customerId.intValue());
    }

    @Override
    public Optional<Customer> find(String email) {
        final var jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);

        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_ALL_WHERE_EMAIL, params, dataBaseDataMapper::customerRowMapper));
        } catch (Exception e) {
            log.warn("Trying to find non-existing user: [{}]", email);
            return Optional.empty();
        }
    }

    @Override
    public List<Customer> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(simpleDriverDataSource);

        // można albo tak
        List<Customer> result1 = jdbcTemplate.query(SELECT_ALL, dataBaseDataMapper::customerRowMapper);

        // albo tak, ale tutaj pola muszą się mapować 1:1 baza_danych:obiekt_java
        BeanPropertyRowMapper<Customer> personBeanPropertyRowMapper = BeanPropertyRowMapper.newInstance(Customer.class);
        return jdbcTemplate.query(SELECT_ALL, personBeanPropertyRowMapper);
    }

    @Override
    public void remove(final String email) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);

        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        int result = jdbcTemplate.update(DELETE_WHERE_EMAIL, params);
        log.debug("Removed: [{}] rows for email: [{}]", result, email);
    }

    @Override
    public void removeAll() {
        new JdbcTemplate(simpleDriverDataSource).update(DELETE_ALL);
    }
}
