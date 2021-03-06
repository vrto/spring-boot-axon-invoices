package sk.vrto.invoices.command.customer;

import lombok.Value;
import sk.vrto.invoices.command.invoice.InvoiceId;

@Value
public class CustomerToInvoiceAddedEvent {
    InvoiceId invoiceId;
    String customerName;
}
