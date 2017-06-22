package sk.vrto.invoices.command.invoice;

import lombok.Value;

@Value
public class InvoiceId {
    private final String identifier;

    @Override
    public String toString() {
        return identifier;
    }
}
