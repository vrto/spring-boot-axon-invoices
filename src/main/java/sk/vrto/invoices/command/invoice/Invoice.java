package sk.vrto.invoices.command.invoice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.commandhandling.model.AggregateRoot;
import org.axonframework.eventhandling.EventHandler;
import sk.vrto.invoices.command.customer.CustomerToInvoiceAddedEvent;
import sk.vrto.invoices.command.event.InvoiceCreatedEvent;

import java.time.ZonedDateTime;

@AggregateRoot
@Getter
@NoArgsConstructor
public class Invoice {

    @AggregateIdentifier
    InvoiceId invoiceId;
    private long amount;
    private String customerName;

    Invoice(InvoiceId invoiceId, String customerName, long amount) {
        val todayNoon = ZonedDateTime.now().withHour(12).withMinute(0).withSecond(0).withNano(0);
        val dueDate = todayNoon.plusDays(30);

        AggregateLifecycle.apply(
                new InvoiceCreatedEvent(
                        invoiceId, customerName, amount, todayNoon, dueDate));
    }

    @EventHandler
    void onInvoiceCreated(InvoiceCreatedEvent event) {
        this.invoiceId = event.getInvoiceId();
        this.amount = event.getAmount();
        this.customerName = event.getCustomerName();
    }

    public void addCustomerToInvoice(String customerName) {
        AggregateLifecycle.apply(new CustomerToInvoiceAddedEvent(invoiceId, customerName));
    }
}
