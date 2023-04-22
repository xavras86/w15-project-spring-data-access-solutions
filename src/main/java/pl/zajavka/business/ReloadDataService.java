package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Slf4j
@Service
@AllArgsConstructor
public class ReloadDataService {

    private CustomerService customerService;

    private ProducerService producerService;

    private ReloadDataRepository reloadDataRepository;

    @Transactional
    public void reloadData() {
        customerService.removeAll();
        producerService.removeAll();
        try {
            Path filePath = ResourceUtils.getFile("classpath:w15-project-sql-inserts.sql").toPath();
            Arrays.stream(Files.readString(filePath).split("INSERT"))
                .filter(line -> !line.isBlank())
                .map(line -> "INSERT" + line)
                .toList()
                .forEach(sql -> reloadDataRepository.run(sql));
        } catch (IOException ex) {
            log.error("Unable to load SQL inserts", ex);
        }

    }
}
