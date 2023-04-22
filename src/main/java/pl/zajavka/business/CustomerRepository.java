package pl.zajavka.business;

import pl.zajavka.domain.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    Customer createCustomer(Customer customer);

    Optional<Customer> find(String email);

    List<Customer> findAll();

    void remove(String email);

    void removeAll();
}
