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

@RestController
@RequestMapping("/Payment")
public class PaymentController {

    private final PaymentInterface paymentService;

    public PaymentController(PaymentImplementation paymentService) {
        this.paymentService = paymentService;
    }

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

    @GetMapping("/generateReceipt/{paymentId}")
    public ResponseEntity<ByteArrayResource> generateReceipt(@PathVariable int paymentId) {
        try {
            Payment payment = paymentService.findByPaymentId(paymentId);
            if (payment == null) {
                System.out.println("Payment not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            CustomerOrder order = new CustomerOrder(
                    new User(),
                    payment,
                    OrderStatus.CANCELLED,
                    new BigDecimal(12),
                    LocalDateTime.MAX);

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
