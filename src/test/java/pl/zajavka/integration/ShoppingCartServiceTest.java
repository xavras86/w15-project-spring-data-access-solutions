package pl.zajavka.integration;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import pl.zajavka.business.CustomerService;
import pl.zajavka.business.ProducerService;
import pl.zajavka.business.ProductService;
import pl.zajavka.business.PurchaseService;
import pl.zajavka.business.ShoppingCartService;
import pl.zajavka.domain.Customer;
import pl.zajavka.domain.Producer;
import pl.zajavka.domain.Product;
import pl.zajavka.domain.Purchase;
import pl.zajavka.business.domain.StoreFixtures;
import pl.zajavka.infrastructure.configuration.ApplicationConfiguration;

import java.util.List;

@SpringJUnitConfig(classes = {ApplicationConfiguration.class})
@AllArgsConstructor(onConstructor = @__(@Autowired))
class ShoppingCartServiceTest {

    private CustomerService customerService;

    private ShoppingCartService shoppingCartService;

    private ProducerService producerService;

    private PurchaseService purchaseService;

    private ProductService productService;

    @BeforeEach
    public void setUp() {
        Assertions.assertNotNull(customerService);
        Assertions.assertNotNull(shoppingCartService);
        Assertions.assertNotNull(producerService);
        Assertions.assertNotNull(purchaseService);
        customerService.removeAll();
        producerService.removeAll();
    }

    @Test
    @DisplayName("Polecenie 9")
    void thatProductWasBoughtSuccessfully() {
        // given
        final Customer customer = customerService.create(StoreFixtures.someCustomer());
        final Producer producer = producerService.create(StoreFixtures.someProducer());
        final Product product1 = productService.create(StoreFixtures.someProduct(producer).withProductCode("g22s").withProductName("Shoes"));
        final Product product2 = productService.create(StoreFixtures.someProduct(producer).withProductCode("Kl09").withProductName("TV"));
        List<Purchase> before = purchaseService.findAll(customer.getEmail(), product1.getProductCode());

        // when
        shoppingCartService.makeAPurchase(customer.getEmail(), product1.getProductCode(), 54);

        // then
        List<Purchase> after = purchaseService.findAll(customer.getEmail(), product1.getProductCode());
        Assertions.assertEquals(before.size() + 1, after.size());
    }
}