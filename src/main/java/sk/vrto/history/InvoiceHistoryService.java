package sk.vrto.history;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import sk.vrto.invoices.command.event.InvoiceCancelledEvent;

@Component
public class InvoiceHistoryService {

    @EventHandler
    void onInvoiceCanceled(InvoiceCancelledEvent event) {
        System.out.println("Creating history entry for the cancelled invoice ..."); // not really ;)
    }
}
