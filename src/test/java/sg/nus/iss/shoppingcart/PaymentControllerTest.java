package sg.nus.iss.shoppingcart;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.web.WebAppConfiguration;
import sg.nus.iss.shoppingcart.controller.PaymentController;
import sg.nus.iss.shoppingcart.model.DTO.OrderAndPaymethod;
import sg.nus.iss.shoppingcart.model.CustomerOrder;
import sg.nus.iss.shoppingcart.model.Payment;
import sg.nus.iss.shoppingcart.enums.PaymentMethods;
import sg.nus.iss.shoppingcart.service.PaymentImplementation;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the PaymentController class.
 */
@SpringBootTest
@WebAppConfiguration
class PaymentControllerTest {

    @Mock
    private PaymentImplementation paymentService;

    @InjectMocks
    private PaymentController paymentController;


    /**
     * Tests the happy path for making a payment.
     */
    @Test
    void makePayment_HappyPath() {
        CustomerOrder order = new CustomerOrder();
        OrderAndPaymethod orderAndMethod = new OrderAndPaymethod(order, PaymentMethods.CREDIT_CARD.name());

        Mockito.when(paymentService.makePayment(order, PaymentMethods.CREDIT_CARD)).thenReturn(true);

        ResponseEntity<Boolean> response = paymentController.makePayment(orderAndMethod);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    /**
     * Tests making a payment with an invalid payment method.
     */
    @Test
    void makePayment_InvalidPaymentMethod() {
        CustomerOrder order = new CustomerOrder();
        OrderAndPaymethod orderAndMethod = new OrderAndPaymethod(order, "INVALID_METHOD");

        ResponseEntity<Boolean> response = paymentController.makePayment(orderAndMethod);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody());
    }

    /**
     * Tests making a payment when the service returns false.
     */
    @Test
    void makePayment_ServiceReturnsFalse() {
        CustomerOrder order = new CustomerOrder();
        OrderAndPaymethod orderAndMethod = new OrderAndPaymethod(order, PaymentMethods.CREDIT_CARD.name());

        Mockito.when(paymentService.makePayment(order, PaymentMethods.CREDIT_CARD)).thenReturn(false);

        ResponseEntity<Boolean> response = paymentController.makePayment(orderAndMethod);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody());
    }

    /**
     * Tests generating a receipt for a valid payment.
     */
    @Test
    void generateReceipt_HappyPath() {
        int paymentId = 1;
        Payment payment = new Payment();
        Mockito.when(paymentService.findByPaymentId(paymentId)).thenReturn(payment);
        Mockito.when(paymentService.generateReceipt(Mockito.any(), Mockito.any())).thenReturn(new byte[]{1, 2, 3});

        ResponseEntity<ByteArrayResource> response = paymentController.generateReceipt(paymentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * Tests generating a receipt when the payment is not found.
     */
    @Test
    void generateReceipt_PaymentNotFound() {
        int paymentId = 1;
        Mockito.when(paymentService.findByPaymentId(paymentId)).thenReturn(null);

        ResponseEntity<ByteArrayResource> response = paymentController.generateReceipt(paymentId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Tests refunding a payment successfully.
     */
    @Test
    void refundPayment_HappyPath() {
        int paymentId = 1;
        Payment payment = new Payment();
        Mockito.when(paymentService.findByPaymentId(paymentId)).thenReturn(payment);

        ResponseEntity<String> response = paymentController.refundPayment(paymentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Payment refunded successfully", response.getBody());
    }

    /**
     * Tests refunding a payment when the payment is not found.
     */
    @Test
    void refundPayment_PaymentNotFound() {
        int paymentId = 1;
        Mockito.when(paymentService.findByPaymentId(paymentId)).thenReturn(null);

        ResponseEntity<String> response = paymentController.refundPayment(paymentId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Payment not found", response.getBody());
    }
}