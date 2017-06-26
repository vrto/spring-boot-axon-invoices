package sk.vrto.invoices.command.event;

import lombok.Value;
import sk.vrto.invoices.command.invoice.InvoiceId;

@Value
public class InvoicePaymentSuccessfulEvent {

    InvoiceId invoiceId;
    long amount;
}
