package sk.vrto.invoices.command.invoice;

import lombok.Value;

@Value
public class CancelInvoiceCommand {
    InvoiceId invoiceId;
}
