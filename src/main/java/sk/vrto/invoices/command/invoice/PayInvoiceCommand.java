package sk.vrto.invoices.command.invoice;

import lombok.Value;

@Value
public class PayInvoiceCommand {
    InvoiceId invoiceId;
    long amount;
}
