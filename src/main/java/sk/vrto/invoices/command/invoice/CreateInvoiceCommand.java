package sk.vrto.invoices.command.invoice;

import lombok.Value;

@Value
public class CreateInvoiceCommand {
    InvoiceId invoiceId;
    String customerName;
    long amount;
}
