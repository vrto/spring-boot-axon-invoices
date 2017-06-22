package sk.vrto.invoices.query.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<CustomerEntry, String> {
    CustomerEntry findByName(@Param("name") String name);
}
