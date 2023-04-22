package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.zajavka.domain.Customer;
import pl.zajavka.domain.Opinion;
import pl.zajavka.domain.Producer;
import pl.zajavka.domain.Product;
import pl.zajavka.domain.Purchase;
import pl.zajavka.infrastructure.database.CustomerDataBaseRepository;
import pl.zajavka.infrastructure.database.OpinionDataBaseRepository;
import pl.zajavka.infrastructure.database.ProducerDataBaseRepository;
import pl.zajavka.infrastructure.database.ProductDataBaseRepository;
import pl.zajavka.infrastructure.database.PurchaseDataBaseRepository;

@Slf4j
@Service
@AllArgsConstructor
public class RandomDataService {

    private final RandomDataPreparationService randomDataPreparationService;

    private final CustomerDataBaseRepository customerDataBaseRepository;

    private final ProducerDataBaseRepository producerDataBaseRepository;

    private final ProductDataBaseRepository productDataBaseRepository;

    private final PurchaseDataBaseRepository purchaseDataBaseRepository;

    private final OpinionDataBaseRepository opinionDataBaseRepository;

    public void create() {
        Customer customer = customerDataBaseRepository.createCustomer(randomDataPreparationService.someCustomer());
        Producer producer = producerDataBaseRepository.createProducer(randomDataPreparationService.someProducer());
        Product product = productDataBaseRepository.createProduct(randomDataPreparationService.someProduct(producer));
        Purchase purchase = purchaseDataBaseRepository.createPurchase(randomDataPreparationService.somePurchase(customer, product));
        Opinion opinion = opinionDataBaseRepository.createOpinion(randomDataPreparationService.someOpinion(customer, product));

        log.debug("Random customer created: [[{}]", customer);
        log.debug("Random producer created: [[{}]", producer);
        log.debug("Random product created: [[{}]", product);
        log.debug("Random purchase created: [[{}]", purchase);
        log.debug("Random opinion created: [[{}]", opinion);
    }

}
