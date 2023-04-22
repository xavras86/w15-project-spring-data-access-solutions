package pl.zajavka.business;

import pl.zajavka.domain.Purchase;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository {
    Purchase createPurchase(Purchase purchase);

    Optional<Purchase> find(Long purchaseId);

    List<Purchase> findAll();

    List<Purchase> findAll(String email);

    List<Purchase> findAll(String email, String productCode);

    List<Purchase> findAllByProductCode(String productCode);

    void removeAll(String email);

    void removeAllByProduct(String productCode);

    void removeAll();
}
