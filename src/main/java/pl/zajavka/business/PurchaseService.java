package pl.zajavka.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.Purchase;

import java.util.List;

@Service
@AllArgsConstructor
public class PurchaseService {

    private PurchaseRepository purchaseRepository;

    @Transactional
    public Purchase create(Purchase purchase) {
        return purchaseRepository.createPurchase(purchase);
    }

    public Purchase find(Long purchaseId) {
        return purchaseRepository.find(purchaseId)
            .orElseThrow(() -> new RuntimeException(String.format("Could not find purchase with id: [%s]", purchaseId)));
    }

    public List<Purchase> findAll() {
        return purchaseRepository.findAll();
    }

    public List<Purchase> findAll(final String email) {
        return purchaseRepository.findAll(email);
    }

    public List<Purchase> findAll(final String email, final String productCode) {
        return purchaseRepository.findAll(email, productCode);
    }

    public List<Purchase> findAllByProductCode(final String productCode) {
        return purchaseRepository.findAllByProductCode(productCode);
    }

    @Transactional
    public void removeAll(final String email) {
        purchaseRepository.removeAll(email);
    }

    @Transactional
    public void removeAllByProduct(final String productCode) {
        purchaseRepository.removeAllByProduct(productCode);
    }

    @Transactional
    public void removeAll() {
        purchaseRepository.removeAll();
    }
}
