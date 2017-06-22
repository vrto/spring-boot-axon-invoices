package sk.vrto.invoices.query.invoice;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class InvoiceLineItemEntry {

    String id;
    String description;
    double quantity;
    BigDecimal unitPrice;
}
