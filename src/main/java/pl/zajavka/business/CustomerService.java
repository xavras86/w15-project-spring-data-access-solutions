package pl.zajavka.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.Customer;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerService {

    private OpinionService opinionService;

    private PurchaseService purchaseService;

    private CustomerRepository customerRepository;

    @Transactional
    public Customer create(Customer customer) {
        return customerRepository.createCustomer(customer);
    }

    public Customer find(final String email) {
        return customerRepository.find(email)
            .orElseThrow(() -> new RuntimeException(String.format("Customer with email: [%s] doesn't exist", email)));
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Transactional
    public void remove(final String email) {
        Optional<Customer> existingCustomer = customerRepository.find(email);
        if (existingCustomer.isEmpty()) {
            throw new RuntimeException(String.format("Customer with email: [%s] not found", email));
        }
        opinionService.removeAll(email);
        purchaseService.removeAll(email);

        // Biznes dochodzi do wniosku, że nie można usunąć klientów, którzy mają więcej niż 40 lat
        if (isOlderThan40(existingCustomer.get())) {
            throw new RuntimeException(String.format("Could not remove customer because he/she is older than 40, email: [%s]", email));
        }
        customerRepository.remove(email);
    }

    private boolean isOlderThan40(final Customer existingCustomer) {
        return LocalDate.now().getYear() - existingCustomer.getDateOfBirth().getYear() > 40;
    }

    @Transactional
    public void removeAll() {
        opinionService.removeAll();
        purchaseService.removeAll();
        customerRepository.removeAll();
    }

    @Transactional
    public void removeUnwantedCustomers() {
        List<Customer> customers = customerRepository.findAll().stream()
            .filter(customer -> !isOlderThan40(customer))
            .filter(customer -> opinionService.customerGivesUnwantedOpinions(customer.getEmail()))
            .toList();

        customers.forEach(customer -> remove(customer.getEmail()));
    }
}
