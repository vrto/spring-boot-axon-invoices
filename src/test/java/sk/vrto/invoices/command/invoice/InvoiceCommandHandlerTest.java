package sk.vrto.invoices.command.invoice;

import lombok.val;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;
import sk.vrto.invoices.command.customer.AddCustomerToInvoiceCommand;
import sk.vrto.invoices.command.customer.CustomerToInvoiceAddedEvent;
import sk.vrto.invoices.command.event.InvoiceCreatedEvent;

import java.time.ZonedDateTime;

public class InvoiceCommandHandlerTest {

    private FixtureConfiguration<Invoice> fixture;

    @Before
    public void setUp() {
        fixture = new AggregateTestFixture<>(Invoice.class);
        val commandHandler = new InvoiceCommandHandler(fixture.getRepository());
        fixture.registerAnnotatedCommandHandler(commandHandler);
    }

    @Test
    public void shouldCreateInvoice() {
        fixture
            .given()
            .when(new CreateInvoiceCommand(new InvoiceId("new-id"), "Apple", 100))
            .expectEvents(new InvoiceCreatedEvent(new InvoiceId("new-id"), "Apple", 100, todayNoon(), in30days()));
    }

    @Test
    public void shouldAddCustomerToInvoice() {
        val invoiceId = new InvoiceId("new-id");
        fixture
            .given(new InvoiceCreatedEvent(invoiceId, "Apple", 100, todayNoon(), in30days()))
            .when(new AddCustomerToInvoiceCommand(invoiceId, "Apple"))
            .expectEvents(new CustomerToInvoiceAddedEvent(invoiceId, "Apple"));
    }

    private ZonedDateTime todayNoon() {
        return ZonedDateTime.now().withHour(12).withMinute(0).withSecond(0).withNano(0);
    }

    private ZonedDateTime in30days() {
        return todayNoon().plusDays(30);
    }

}