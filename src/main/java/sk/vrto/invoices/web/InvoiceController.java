package sk.vrto.invoices.web;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sk.vrto.invoices.command.invoice.CreateInvoiceCommand;
import sk.vrto.invoices.command.invoice.InvoiceId;
import sk.vrto.invoices.query.invoice.InvoiceEntry;
import sk.vrto.invoices.query.invoice.InvoiceRepository;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvoiceController {

    private final InvoiceRepository invoiceRepository;
    private final CommandBus commandBus;

    @GetMapping("/invoices/{invoiceId}")
    ResponseEntity<InvoiceEntry> getInvoice(@PathVariable("invoiceId") String invoiceId) {
        val invoice = invoiceRepository.findOne(invoiceId);
        return ResponseEntity.ok(invoice);
    }

    @PostMapping("/invoices/{invoiceId}")
    ResponseEntity<?> createInvoice(@PathVariable("invoiceId") String invoiceId, @RequestBody InvoiceBody invoice) {
        //TODO payload validation ...

        val command = new CreateInvoiceCommand(
                new InvoiceId(invoiceId),
                invoice.getCustomerName(),
                invoice.getAmount());
        commandBus.dispatch(new GenericCommandMessage<>(command));

        val location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{invoiceId}")
                .buildAndExpand(invoiceId)
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
