package sk.vrto.invoices.query.invoice;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import sk.vrto.invoices.command.event.InvoiceCreatedEvent;
import sk.vrto.invoices.command.event.InvoicePaymentSuccessfulEvent;
import sk.vrto.invoices.command.invoice.InvoiceId;

import java.time.ZonedDateTime;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
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

    @Test
    @DatabaseSetup("/dbunit/invoices.xml")
    public void shouldHandlePaymentSuccessfulEvent() {
        invoiceListener.onInvoicePaymentSuccessful(new InvoicePaymentSuccessfulEvent(new InvoiceId("invoice-1"), 500));

        val invoice = invoiceRepository.findOne("invoice-1");
        assertThat(invoice.isPaid()).isTrue();
    }

}