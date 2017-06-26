package sk.vrto.bookkeeping;

import org.axonframework.eventhandling.EventHandler;
import sk.vrto.invoices.command.event.InvoiceCancelledEvent;
import sk.vrto.invoices.command.event.InvoicePaymentSuccessfulEvent;

public class BookKeepingService {

    @EventHandler
    void onInvoicePaymentSuccessful(InvoicePaymentSuccessfulEvent event) {
        System.out.println("Adding invoice to book keeping system ....");
    }

    @EventHandler
    void onInvoiceCancelled(InvoiceCancelledEvent event) {
        System.out.println("Removing invoice from the book keeping system ...");
    }
}
