package sk.vrto.invoices.query.invoice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<InvoiceEntry, String> {

    List<QuickInvoiceProjection> findAllProjectedBy();
}
