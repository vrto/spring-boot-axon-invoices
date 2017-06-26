package sk.vrto.invoices.web;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class InvoiceControllerTest {

    @Value("${local.server.port}")
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DatabaseSetup("/dbunit/invoices.xml")
    public void shouldGetInvoice() {
        get("/invoices/invoice-1").then()
            .statusCode(OK.value())
            .body("customerName", equalTo("Tesla"))
            .body("customerAddress", equalTo("Silicon Valley"))
            .body("currency", equalTo("USD"))
            .body("invoiceTotal", equalTo(500))
            .body("paid", equalTo(false));
    }

    @Test
    @DatabaseSetup("/dbunit/invoices.xml")
    public void shouldListQuickInvoices() {
        get("/invoices/quick").then()
            .statusCode(OK.value())
            .body("[0].id", equalTo("invoice-1"))
            .body("[0].invoiceTotal", equalTo(500))
            .body("[1].id", equalTo("invoice-2"))
            .body("[1].invoiceTotal", equalTo(199));
    }

    @Test
    public void shouldCreateInvoice() {
        given()
            .contentType(ContentType.JSON)
            .body(new InvoiceBody("Apple", 300))
        .when()
            .post("/invoices/new-id")
        .then()
            .statusCode(CREATED.value());
    }

    @Test
    @DatabaseSetup("/dbunit/invoices.xml")
    public void shouldRefuseInvoicePayment_IfAmountDoesNotMatch() {
        given()
            .contentType(ContentType.JSON)
            .body(new InvoicePayment(499))
        .when()
            .patch("/invoices/invoice-1")
        .then()
            .statusCode(BAD_REQUEST.value());
    }

    @Test
    @DatabaseSetup("/dbunit/invoices.xml")
    public void shouldPayInvoice() {
        given()
            .contentType(ContentType.JSON)
            .body(new InvoicePayment(500))
        .when()
            .patch("/invoices/invoice-1")
        .then()
            .statusCode(NO_CONTENT.value());

    }

    @Test
    @DatabaseSetup("/dbunit/invoices.xml")
    public void shouldCancelInvoice() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .post("/invoices/invoice-1/cancellation")
        .then()
            .statusCode(ACCEPTED.value()); // in full CQRS clients don't know the result immediately - they can GET the same resource and see
    }
}