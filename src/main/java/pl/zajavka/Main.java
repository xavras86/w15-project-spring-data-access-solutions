package pl.zajavka;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.zajavka.business.CustomerService;
import pl.zajavka.business.RandomDataService;
import pl.zajavka.domain.Customer;
import pl.zajavka.infrastructure.configuration.ApplicationConfiguration;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        RandomDataService randomDataService = context.getBean(RandomDataService.class);
        randomDataService.create();

        CustomerService customerService = context.getBean(CustomerService.class);
        List<Customer> allCustomers = customerService.findAll();
        System.out.println(allCustomers);
    }
}