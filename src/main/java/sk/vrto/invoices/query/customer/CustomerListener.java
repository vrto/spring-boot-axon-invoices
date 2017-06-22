package sk.vrto.invoices.query.customer;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sk.vrto.invoices.command.customer.CustomerToInvoiceAddedEvent;
import sk.vrto.invoices.query.invoice.InvoiceRepository;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerListener {

    private final CustomerRepository customerRepository;
    private final InvoiceRepository invoiceRepository;

    @EventHandler
    void handleCustomerToInvoiceAdded(CustomerToInvoiceAddedEvent event) {
        val customer = customerRepository.findByName(event.getCustomerName());
        val invoice = invoiceRepository.findOne(event.getInvoiceId().getIdentifier());
        invoice.setCustomer(customer);
        invoiceRepository.save(invoice);
    }
}
