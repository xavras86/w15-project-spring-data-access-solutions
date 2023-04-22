package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.Customer;
import pl.zajavka.domain.Opinion;
import pl.zajavka.domain.Purchase;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class OpinionService {

    private PurchaseService purchaseService;

    private OpinionRepository opinionRepository;

    @Transactional
    public Opinion create(Customer customer, Opinion opinion) {
        List<Purchase> purchases = purchaseService.findAll(customer.getEmail(), opinion.getProduct().getProductCode());
        log.debug("Customer: [{}] made: [{}] purchases of product: [{}]",
            customer.getEmail(), purchases.size(), opinion.getProduct().getProductCode());

        if (purchases.isEmpty()) {
            throw new RuntimeException(
                String.format("Product codes mismatch. Customer: [%s] wants to give opinion for product: [%s] that didnt purchase",
                    customer.getEmail(), opinion.getProduct().getProductCode()));
        }

        return opinionRepository.createOpinion(opinion);
    }

    public List<Opinion> findAll() {
        return opinionRepository.findAll();
    }

    public List<Opinion> findAll(final String email) {
        return opinionRepository.findAll(email);
    }

    public List<Opinion> findAllByProduct(final String productCode) {
        return opinionRepository.findAllByProduct(productCode);
    }

    public List<Opinion> findUnwantedOpinions() {
        return opinionRepository.findUnwantedOpinions();
    }

    @Transactional
    public void removeUnwantedOpinions() {
        opinionRepository.removeUnwantedOpinions();
    }

    @Transactional
    public void removeAll(final String email) {
        opinionRepository.removeAll(email);
    }

    @Transactional
    public void removeAllByProduct(String productCode) {
        opinionRepository.removeAllByProduct(productCode);
    }

    @Transactional
    public void removeAll() {
        opinionRepository.removeAll();
    }

    public boolean customerGivesUnwantedOpinions(final String email) {
        return opinionRepository.customerGivesUnwantedOpinions(email);
    }
}
