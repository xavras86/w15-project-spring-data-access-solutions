package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.ProducerRepository;
import pl.zajavka.domain.Producer;
import pl.zajavka.infrastructure.configuration.DatabaseConfiguration;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class ProducerDataBaseRepository implements ProducerRepository {

    private static final String SELECT_WHERE_PRODUCER_NAME = "SELECT * FROM PRODUCER WHERE PRODUCER_NAME = :producerName";
    private static final String SELECT_ALL = "SELECT * FROM PRODUCER";
    private static final String DELETE_ALL = "DELETE FROM PRODUCER";

    private final SimpleDriverDataSource simpleDriverDataSource;

    private final DataBaseDataMapper dataBaseDataMapper;

    @Override
    public Producer createProducer(final Producer producer) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(simpleDriverDataSource)
            .withTableName(DatabaseConfiguration.PRODUCER_TABLE)
            .usingGeneratedKeyColumns(DatabaseConfiguration.PRODUCER_TABLE_PKEY.toLowerCase());

        Map<String, Object> params = dataBaseDataMapper.mapProducerParams(producer);
        Number producerId = jdbcInsert.executeAndReturnKey(params);
        Producer producerCreated = producer.withId((long) producerId.intValue());
        log.info("Producer created: [{}]", producerCreated);
        return producerCreated;
    }

    @Override
    public Optional<Producer> find(final String producerName) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                SELECT_WHERE_PRODUCER_NAME,
                Map.of("producerName", producerName),
                dataBaseDataMapper::producerRowMapper));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Producer> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(simpleDriverDataSource);
        List<Producer> result = jdbcTemplate.query(SELECT_ALL, dataBaseDataMapper::producerRowMapper);
        log.debug("All producers: [{}]", result);
        return result;
    }

    @Override
    public void removeAll() {
        new JdbcTemplate(simpleDriverDataSource).update(DELETE_ALL);
    }
}
