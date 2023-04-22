package pl.zajavka.business;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajavka.domain.Opinion;
import pl.zajavka.business.domain.StoreFixtures;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpinionServiceTest {

    @InjectMocks
    private OpinionService opinionService;

    @Mock
    private PurchaseService purchaseService;

    @Mock
    private OpinionRepository opinionRepository;

    @Test
    @DisplayName("Polecenie 5 cz.1")
    void thatOpinionCanBeCreatedForProductThatCustomerActuallyBought() {
        // given
        final var customer = StoreFixtures.someCustomer();
        final var producer = StoreFixtures.someProducer();
        final var product = StoreFixtures.someProduct(producer);
        final var purchase = StoreFixtures.somePurchase(customer, product);
        final var opinion = StoreFixtures.someOpinion(customer, product.withId(1L));

        when(purchaseService.findAll(anyString(), anyString())).thenReturn(List.of(purchase.withId(1L)));
        when(opinionRepository.createOpinion(any(Opinion.class))).thenReturn(opinion.withId(10L));

        // when
        Opinion result = opinionService.create(customer, opinion);

        // then
        verify(opinionRepository).createOpinion(any(Opinion.class));
        Assertions.assertEquals(opinion.withId(10L), result);
    }

    @Test
    @DisplayName("Polecenie 5 cz.2")
    void thatOpinionCanNotBeCreatedForProductThatCustomerDidNotBuy() {
        // given
        final var customer = StoreFixtures.someCustomer();
        final var producer = StoreFixtures.someProducer();
        final var product = StoreFixtures.someProduct(producer);
        final var opinion = StoreFixtures.someOpinion(customer, product.withProductCode("testCode"));

        when(purchaseService.findAll(anyString(), anyString())).thenReturn(List.of());

        // when, then
        Throwable exception = Assertions.assertThrows(RuntimeException.class, () -> opinionService.create(customer, opinion));
        Assertions.assertEquals(
            String.format("Product codes mismatch. Customer: [%s] wants to give opinion for product: [%s] that didnt purchase",
                customer.getEmail(), opinion.getProduct().getProductCode()),
            exception.getMessage()
        );

        verify(opinionRepository, never()).createOpinion(any(Opinion.class));
    }

}