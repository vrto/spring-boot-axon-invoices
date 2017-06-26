package sk.vrto.invoices.query.invoice;

import lombok.*;
import lombok.experimental.FieldDefaults;
import sk.vrto.invoices.query.customer.CustomerEntry;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceEntry {

    @Id
    String id;

    @Column(name = "issued_at")
    ZonedDateTime issuedAt;

    @Column(name = "due_to")
    ZonedDateTime dueTo;

    @OneToOne
    @JoinColumn(name = "cus_id")
    CustomerEntry customer;

    @Column(name = "invoice_total")
    long invoiceTotal;

    String currency;

    boolean paid;

    InvoiceEntry(String identifier, ZonedDateTime issuedAt, ZonedDateTime dueTo, long amount) {
        this.id = identifier;
        this.issuedAt = issuedAt;
        this.dueTo = dueTo;
        this.invoiceTotal = amount;
        this.currency = "USD"; // TODO curency service based on customer
        this.paid = false;
    }

    public String getCustomerName() {
        return customer.getName();
    }

    public String getCustomerAddress() {
        return customer.getAddress();
    }

    public boolean canDoPayment(long amount) {
        return this.invoiceTotal == amount;
    }
}
