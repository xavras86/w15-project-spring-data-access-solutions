package pl.zajavka.integration;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import pl.zajavka.business.OpinionService;
import pl.zajavka.business.ProductRemovalService;
import pl.zajavka.business.ProductService;
import pl.zajavka.business.PurchaseService;
import pl.zajavka.business.ReloadDataService;
import pl.zajavka.domain.Opinion;
import pl.zajavka.domain.Product;
import pl.zajavka.domain.Purchase;
import pl.zajavka.infrastructure.configuration.ApplicationConfiguration;

import java.util.List;
import java.util.Objects;

@SpringJUnitConfig(classes = {ApplicationConfiguration.class})
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductServiceTest {

    private ReloadDataService reloadDataService;

    private OpinionService opinionService;

    private ProductService productService;

    private PurchaseService purchaseService;

    private ProductRemovalService productRemovalService;

    @BeforeEach
    public void setUp() {
        Assertions.assertNotNull(reloadDataService);
        Assertions.assertNotNull(opinionService);
        Assertions.assertNotNull(productService);
        Assertions.assertNotNull(purchaseService);
        Assertions.assertNotNull(productRemovalService);
        reloadDataService.reloadData();
    }

    @Test
    @DisplayName("Polecenie 10")
    void thatProductIsWipedCompletely() {
        // given
        final var productCode = "60560-1072";
        Product before = productService.find(productCode);
        List<Opinion> opinionsBefore = opinionService.findAllByProduct(productCode);
        List<Purchase> purchasesBefore = purchaseService.findAllByProductCode(productCode);
        Assertions.assertTrue(Objects.nonNull(before));
        Assertions.assertEquals(3, opinionsBefore.size());
        Assertions.assertEquals(5, purchasesBefore.size());

        // when
        productRemovalService.removeCompletely(productCode);

        // then
        Throwable exception = Assertions.assertThrows(RuntimeException.class, () -> productService.find(productCode));
        Assertions.assertEquals(String.format("Product with productCode: [%s] doesn't exist", productCode), exception.getMessage());
        Assertions.assertTrue(opinionService.findAllByProduct(productCode).isEmpty());
        Assertions.assertTrue(purchaseService.findAllByProductCode(productCode).isEmpty());
    }
}
