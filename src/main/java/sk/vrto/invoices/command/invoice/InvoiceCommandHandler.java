package sk.vrto.invoices.command.invoice;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import sk.vrto.invoices.command.customer.AddCustomerToInvoiceCommand;

@Component
public class InvoiceCommandHandler {

    private final Repository<Invoice> repository;

    @Autowired
    public InvoiceCommandHandler(@Qualifier("invoiceAggregateRepository") Repository<Invoice> repository) {
        this.repository = repository;
    }

    @CommandHandler
    public void handleCreateInvoice(CreateInvoiceCommand command) throws Exception {
        repository.newInstance(() ->
                new Invoice(command.getInvoiceId(), command.getCustomerName(), command.getAmount()));
    }

    @CommandHandler
    public void handleAddCustomerToInvoice(AddCustomerToInvoiceCommand command) {
        Aggregate<Invoice> invoice = repository.load(command.getInvoiceId().getIdentifier());
        invoice.execute(aggregateRoot -> aggregateRoot.addCustomerToInvoice(command.getCustomerName()));
    }

    @CommandHandler
    public void handlePayInvoice(PayInvoiceCommand command) {
        Aggregate<Invoice> invoice = repository.load(command.getInvoiceId().getIdentifier());
        invoice.execute(aggregateRoot -> aggregateRoot.payInvoice(command.getAmount()));
    }

    @CommandHandler
    public void handleCancelInvoice(CancelInvoiceCommand command) {
        Aggregate<Invoice> invoice = repository.load(command.getInvoiceId().getIdentifier());
        invoice.execute(aggregateRoot -> aggregateRoot.cancelInvoice());
    }
}
