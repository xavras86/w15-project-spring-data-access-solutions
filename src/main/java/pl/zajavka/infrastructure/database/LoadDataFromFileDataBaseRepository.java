package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.ReloadDataRepository;

@Slf4j
@Repository
@AllArgsConstructor
public class LoadDataFromFileDataBaseRepository implements ReloadDataRepository {

    private SimpleDriverDataSource simpleDriverDataSource;

    @Override
    public void run(final String sql) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(simpleDriverDataSource);
        int result = jdbcTemplate.update(sql);
        log.debug("LoadData updated rows: [{}]", result);
    }
}
