package pl.zajavka.integration;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import pl.zajavka.business.CustomerService;
import pl.zajavka.business.OpinionService;
import pl.zajavka.business.ProducerService;
import pl.zajavka.business.ProductService;
import pl.zajavka.business.PurchaseService;
import pl.zajavka.business.ReloadDataService;
import pl.zajavka.infrastructure.configuration.ApplicationConfiguration;

@SpringJUnitConfig(classes = {ApplicationConfiguration.class})
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ReloadDataServiceTest {

    private ReloadDataService reloadDataService;

    private CustomerService customerService;

    private OpinionService opinionService;

    private ProducerService producerService;

    private ProductService productService;

    private PurchaseService purchaseService;

    @BeforeEach
    public void setUp() {
        Assertions.assertNotNull(customerService);
        Assertions.assertNotNull(opinionService);
        Assertions.assertNotNull(producerService);
        Assertions.assertNotNull(productService);
        Assertions.assertNotNull(purchaseService);
        customerService.removeAll();
        producerService.removeAll();
    }

    @Test
    @DisplayName("Przypadek 6")
    void testReloading() {
        // given
        Assertions.assertTrue(customerService.findAll().isEmpty());
        Assertions.assertTrue(opinionService.findAll().isEmpty());
        Assertions.assertTrue(producerService.findAll().isEmpty());
        Assertions.assertTrue(productService.findAll().isEmpty());
        Assertions.assertTrue(purchaseService.findAll().isEmpty());

        // when
        reloadDataService.reloadData();

        // then
        Assertions.assertEquals(100, customerService.findAll().size());
        Assertions.assertEquals(140, opinionService.findAll().size());
        Assertions.assertEquals(20, producerService.findAll().size());
        Assertions.assertEquals(50, productService.findAll().size());
        Assertions.assertEquals(300, purchaseService.findAll().size());
    }
}
