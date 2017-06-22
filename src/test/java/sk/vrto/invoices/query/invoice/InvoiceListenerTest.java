package sk.vrto.invoices.query.invoice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sk.vrto.invoices.command.event.InvoiceCreatedEvent;
import sk.vrto.invoices.command.invoice.InvoiceId;

import java.time.ZonedDateTime;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InvoiceListenerTest {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    InvoiceListener invoiceListener;

    @Test
    public void shouldHandleInvoiceCreatedEvent() {
        assertThat(invoiceRepository.findOne("invoice-1")).isNull();

        invoiceListener.onInvoiceCreated(new InvoiceCreatedEvent(
                new InvoiceId("invoice-1"),
                "Tesla",
                1000,
                ZonedDateTime.now(),
                ZonedDateTime.now().plusDays(14)
        ));

        assertThat(invoiceRepository.findOne("invoice-1")).isNotNull();
    }

}