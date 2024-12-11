package co.istad.reporting.order;

import co.istad.reporting.domain.oracle.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends
        JpaRepository<Product, Long> {
}
