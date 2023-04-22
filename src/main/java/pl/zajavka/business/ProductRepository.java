package pl.zajavka.business;

import pl.zajavka.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product createProduct(Product product);

    Optional<Product> find(Long productId);

    Optional<Product> find(String productCode);

    List<Product> findAll();

    void remove(String productCode);

    void removeAll();
}
