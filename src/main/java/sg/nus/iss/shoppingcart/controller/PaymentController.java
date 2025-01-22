package sg.nus.iss.shoppingcart.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.shoppingcart.enums.OrderStatus;
import sg.nus.iss.shoppingcart.enums.PaymentMethods;
import sg.nus.iss.shoppingcart.interfacemethods.PaymentInterface;
import sg.nus.iss.shoppingcart.model.CustomerOrder;
import sg.nus.iss.shoppingcart.model.DTO.OrderAndPaymethod;
import sg.nus.iss.shoppingcart.model.Payment;
import sg.nus.iss.shoppingcart.model.User;
import sg.nus.iss.shoppingcart.service.PaymentImplementation;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * @ClassName PaymentController
 * @Description
 * @Author V_E
 * @StudentID A0307246H
 * @Date 2024/10/5
 * @Version 3.0
 */

@RestController
@RequestMapping("/Payment")
public class PaymentController {

    private final PaymentInterface paymentService;

    /**
     * Constructor for PaymentController.
     *
     * @param paymentService the service for processing payments
     */
    public PaymentController(PaymentImplementation paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Endpoint to process a payment for a given customer order and payment method.
     *
     * @param orderAndMethod an object containing the customer order and payment method
     * @return a ResponseEntity containing a boolean indicating the payment status, or an error status
     */
    @PostMapping("/make-payment")
    public ResponseEntity<Boolean> makePayment(@RequestBody OrderAndPaymethod orderAndMethod) {
        try {
            CustomerOrder order = orderAndMethod.order();
            PaymentMethods paymentMethod = PaymentMethods.valueOf(orderAndMethod.paymentMethod());

            boolean paymentStatus = paymentService.makePayment(order, paymentMethod);
            return paymentStatus
                    ? ResponseEntity.ok(true)
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    /**
     * Endpoint to generate a receipt for a given payment ID.
     *
     * @param paymentId the ID of the payment for which to generate the receipt
     * @return a ResponseEntity containing the receipt PDF as a ByteArrayResource, or an error status
     */
    @GetMapping("/generateReceipt/{paymentId}")
    public ResponseEntity<ByteArrayResource> generateReceipt(@PathVariable int paymentId) {
        try {
            Payment payment = paymentService.findByPaymentId(paymentId);
            if (payment == null) {
                System.out.println("Payment not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            //CustomerOrder order = payment.getOrder();
            CustomerOrder order = new CustomerOrder(
                    new User(),
                    payment,
                    OrderStatus.CANCELLED,
                    new BigDecimal(12),
                    LocalDateTime.MAX
            );


            byte[] pdfBytes = paymentService.generateReceipt(order, payment);


            if (pdfBytes == null || pdfBytes.length == 0) {
                return ResponseEntity.noContent().build();
            }

            ByteArrayResource resource = new ByteArrayResource(pdfBytes);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=receipt-" + order.getOrderId() + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfBytes.length)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint to process a refund for a given payment.
     *
     * @param paymentId the ID of the payment to be refunded
     * @return a ResponseEntity with a success message or an error status
     */
    @PostMapping("/refund-payment")
    public ResponseEntity<String> refundPayment(@RequestParam int paymentId) {
        try {
            Payment payment = paymentService.findByPaymentId(paymentId);
            if (payment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");
            }

            paymentService.Refund(payment);
            return ResponseEntity.ok("Payment refunded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing refund: " + e.getMessage());
        }
    }
}
