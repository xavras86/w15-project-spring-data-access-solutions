package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.Producer;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ProducerService {

    private ProductService productService;

    private ProducerRepository producerRepository;

    public Producer create(Producer producer) {
        return producerRepository.createProducer(producer);
    }

    public List<Producer> findAll() {
        return producerRepository.findAll();
    }

    @Transactional
    public void removeAll() {
        productService.removeAll();
        producerRepository.removeAll();
    }
}
