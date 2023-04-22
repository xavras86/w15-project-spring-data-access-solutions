package pl.zajavka.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ProductRemovalService {

    private ProductService productService;

    private OpinionService opinionService;

    private PurchaseService purchaseService;

    @Transactional
    public void removeCompletely(String productCode) {
        purchaseService.removeAllByProduct(productCode);
        opinionService.removeAllByProduct(productCode);
        productService.remove(productCode);
    }
}
