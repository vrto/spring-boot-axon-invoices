package sk.vrto.invoices.command.invoice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.commandhandling.model.AggregateRoot;
import org.axonframework.eventhandling.EventHandler;
import sk.vrto.invoices.command.customer.CustomerToInvoiceAddedEvent;
import sk.vrto.invoices.command.event.InvoiceCancellationRefusedEvent;
import sk.vrto.invoices.command.event.InvoiceCancelledEvent;
import sk.vrto.invoices.command.event.InvoiceCreatedEvent;
import sk.vrto.invoices.command.event.InvoicePaymentSuccessfulEvent;

import java.time.ZonedDateTime;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@AggregateRoot
@Getter
@NoArgsConstructor
public class Invoice {

    enum State { PAID, UNPAID }

    @AggregateIdentifier
    InvoiceId invoiceId;

    private long amount;
    private String customerName;
    State state;

    Invoice(InvoiceId invoiceId, String customerName, long amount) {
        val todayNoon = ZonedDateTime.now().withHour(12).withMinute(0).withSecond(0).withNano(0);
        val dueDate = todayNoon.plusDays(30);

        apply(new InvoiceCreatedEvent(
                invoiceId, customerName, amount, todayNoon, dueDate));
    }

    public void addCustomerToInvoice(String customerName) {
        apply(new CustomerToInvoiceAddedEvent(invoiceId, customerName));
    }

    public void payInvoice(long amount) {
        apply(new InvoicePaymentSuccessfulEvent(invoiceId, amount));
    }

    public void cancelInvoice() {
        if (state == State.PAID) {
            apply(new InvoiceCancellationRefusedEvent(invoiceId));
        } else {
            apply(new InvoiceCancelledEvent(invoiceId));
        }
    }

    @EventHandler
    void onInvoiceCreated(InvoiceCreatedEvent event) {
        this.invoiceId = event.getInvoiceId();
        this.amount = event.getAmount();
        this.customerName = event.getCustomerName();
        this.state = State.UNPAID;
    }

    @EventHandler
    void onInvoicePaymentSuccessful(InvoicePaymentSuccessfulEvent event) {
        this.state = State.PAID;
    }
}
