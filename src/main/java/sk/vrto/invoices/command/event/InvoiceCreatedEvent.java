package sk.vrto.invoices.command.event;

import lombok.Value;
import sk.vrto.invoices.command.invoice.InvoiceId;

import java.time.ZonedDateTime;

@Value
public class InvoiceCreatedEvent {

    InvoiceId invoiceId;
    String customerName;
    long amount;
    ZonedDateTime issuedAt;
    ZonedDateTime dueTo;

}
