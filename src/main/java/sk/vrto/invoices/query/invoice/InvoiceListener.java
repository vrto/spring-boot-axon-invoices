package sk.vrto.invoices.query.invoice;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sk.vrto.invoices.command.customer.AddCustomerToInvoiceCommand;
import sk.vrto.invoices.command.event.InvoiceCancelledEvent;
import sk.vrto.invoices.command.event.InvoiceCreatedEvent;
import sk.vrto.invoices.command.event.InvoicePaymentSuccessfulEvent;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class InvoiceListener {

    private final InvoiceRepository invoiceRepository;
    private final CommandBus commandBus;

    @EventHandler
    void onInvoiceCreated(InvoiceCreatedEvent event) {
        val entry = new InvoiceEntry(event.getInvoiceId().getIdentifier(), event.getIssuedAt(), event.getDueTo(), event.getAmount());
        invoiceRepository.save(entry);

        val command = new AddCustomerToInvoiceCommand(event.getInvoiceId(), event.getCustomerName());
        commandBus.dispatch(new GenericCommandMessage<>(command));
    }

    @EventHandler
    void onInvoicePaymentSuccessful(InvoicePaymentSuccessfulEvent event) {
        val invoice = invoiceRepository.findOne(event.getInvoiceId().toString());
        invoice.setPaid(true);
        invoiceRepository.save(invoice);
    }

    @EventHandler
    void onInvoiceCanceled(InvoiceCancelledEvent event) {
        //TODO
        // modify invoice state in the database
    }
}
