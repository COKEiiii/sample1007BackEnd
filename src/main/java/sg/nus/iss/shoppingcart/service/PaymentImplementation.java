package sg.nus.iss.shoppingcart.service;

import jakarta.transaction.Transactional;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;
import sg.nus.iss.shoppingcart.enums.PaymentMethods;
import sg.nus.iss.shoppingcart.enums.PaymentStatus;
import sg.nus.iss.shoppingcart.interfacemethods.PaymentInterface;
import sg.nus.iss.shoppingcart.model.CustomerOrder;
import sg.nus.iss.shoppingcart.model.OrderItem;
import sg.nus.iss.shoppingcart.model.Payment;
import sg.nus.iss.shoppingcart.repository.PaymentRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * @ClassName PaymentImplementation
 * @Description Implementation of the PaymentInterface, providing services related to payment processing.
 */
@Service
public class PaymentImplementation implements PaymentInterface {

    private final PaymentRepository paymentRepository;

    /**
     * Constructor for PaymentImplementation.
     *
     * @param paymentRepository the repository for payments
     */
    public PaymentImplementation(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Processes a payment for a given order.
     *
     * @param order the customer order
     * @param paymentMethod the payment method
     * @return true if the payment is successful, false otherwise
     */
    @Override
    @Transactional
    public boolean makePayment(CustomerOrder order, PaymentMethods paymentMethod) {
        try {
            Payment payment = new Payment(order, order.getTotalPrice(), paymentMethod, PaymentStatus.UNPAID);
            order.setPayment(payment);
            payment.setOrder(order);

            if (Pay()) {
                payment.setStatus(PaymentStatus.PAID);
                paymentRepository.save(payment);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException("Payment processing failed", e);
        }
    }

    /**
     * Simulates a payment process.
     *
     * @return true if the payment is successful, false otherwise
     */
    @Override
    public boolean Pay() {
        boolean isSuccess = new Random().nextInt(100) < 80;
        System.out.println(isSuccess ? "Payment successful" : "Slow Internet, Payment failed");
        return isSuccess;
    }

    /**
     * Processes a refund for a given payment.
     *
     * @param payment the payment to refund
     */
    @Override
    @Transactional
    public void Refund(Payment payment) {
        try {
            payment.setStatus(PaymentStatus.REFUNDED);
            paymentRepository.save(payment);
            System.out.println("Refund successful");
        } catch (Exception e) {
            throw new RuntimeException("Refund processing failed", e);
        }
    }

    /**
     * Generates a receipt for a given order and payment.
     *
     * @param order the customer order
     * @param payment the payment
     * @return a byte array representing the receipt PDF
     */
    @Override
    public byte[] generateReceipt(CustomerOrder order, Payment payment) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setNonStrokingColor(255, 255, 255);
                contentStream.fillRect(0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight());

                contentStream.setNonStrokingColor(0, 0, 0);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.beginText();
                contentStream.newLineAtOffset(200, 750);
                contentStream.showText("Payment Receipt");
                contentStream.endText();

                addOrderInfo(contentStream, order);
                drawSeparator(contentStream);
                addPaymentInfo(contentStream, payment);
            }

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                document.save(baos);
                return baos.toByteArray();
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to generate receipt", e);
        }
    }

    /**
     * Adds order information to the receipt.
     *
     * @param contentStream the content stream to write to
     * @param order the customer order
     * @throws IOException if an I/O error occurs
     */
    private void addOrderInfo(PDPageContentStream contentStream, CustomerOrder order) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.setNonStrokingColor(0, 0, 0);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 700);
        contentStream.showText("Order ID: " + order.getOrderId());
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Customer Name: " + order.getUser().getUsername());
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Item Count: " + order.getOrderItems().size());

        for (OrderItem item : order.getOrderItems()) {
            contentStream.showText("Item: " + item.getProduct().getName() + " | Price: " + item.getProduct().getPrice() + " | Quantity: " + item.getQuantity());
            contentStream.newLineAtOffset(0, -20);
        }
        contentStream.endText();
    }

    /**
     * Draws a separator line on the receipt.
     *
     * @param contentStream the content stream to write to
     * @throws IOException if an I/O error occurs
     */
    private void drawSeparator(PDPageContentStream contentStream) throws IOException {
        contentStream.setStrokingColor(0);
        contentStream.moveTo(50, 580);
        contentStream.lineTo(500, 580);
        contentStream.stroke();
    }

    /**
     * Adds payment information to the receipt.
     *
     * @param contentStream the content stream to write to
     * @param payment the payment
     * @throws IOException if an I/O error occurs
     */
    private void addPaymentInfo(PDPageContentStream contentStream, Payment payment) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 560);
        contentStream.showText("Amount: " + payment.getAmount());
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Payment Method: " + payment.getPaymentMethod());
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Payment Status: " + payment.getStatus());
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Date: " + java.time.LocalDate.now());
        contentStream.endText();
    }

    /**
     * Finds a payment by its ID.
     *
     * @param paymentId the payment ID
     * @return the found payment
     */
    @Override
    @Transactional
    public Payment findByPaymentId(int paymentId) {
        try {
            return paymentRepository.findByPaymentId(paymentId);
        } catch (Exception e) {
            throw new RuntimeException("Finding payment record failed", e);
        }
    }
}