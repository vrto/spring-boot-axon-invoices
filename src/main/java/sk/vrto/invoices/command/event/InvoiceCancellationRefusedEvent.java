package sk.vrto.invoices.command.event;

import lombok.Value;
import sk.vrto.invoices.command.invoice.InvoiceId;

/**
 * Would be handled by some sort of statistics service ...
 */
@Value
public class InvoiceCancellationRefusedEvent {
    InvoiceId invoiceId;
}
