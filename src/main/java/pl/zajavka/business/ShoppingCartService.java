package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.Customer;
import pl.zajavka.domain.Product;
import pl.zajavka.domain.Purchase;

import java.time.OffsetDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class ShoppingCartService {

    private CustomerService customerService;

    private ProductService productService;

    private PurchaseService purchaseService;

    @Transactional
    public void makeAPurchase(String email, String productCode, Integer quantity) {
        Customer customer = customerService.find(email);
        Product product = productService.find(productCode);
        Purchase purchase = purchaseService.create(Purchase.builder()
            .customer(customer)
            .product(product)
            .quantity(quantity)
            .dateTime(OffsetDateTime.now())
            .build());

        log.info("Customer: [{}] successfully made a purchase of product: [{}], quantity: [{}]", email, productCode, quantity);
        log.debug("Customer: [{}] successfully made a purchase of product: [{}], quantity: [{}], purchase: [{}]", email, productCode, quantity, purchase);
    }
}
