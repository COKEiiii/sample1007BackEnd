package sg.nus.iss.shoppingcart.interfacemethods;

import sg.nus.iss.shoppingcart.enums.PaymentMethods;
import sg.nus.iss.shoppingcart.model.CustomerOrder;
import sg.nus.iss.shoppingcart.model.Payment;

/**
 * @ClassName PaymentInterface
 * @Description
 * @Author V_E
 * @StudentID A0307246H
 * @Date 2024/10/5
 * @Version 3.0
 */

/**
 * PaymentInterface defines the methods for processing payments, refunds, and generating receipts.
 */
public interface PaymentInterface {

    /**
     * Processes the payment for a given customer order and payment method.
     *
     * @param order the customer order to be paid
     * @param paymentMethod the method of payment
     * @return a byte array representing the generated receipt PDF, or null if payment fails
     */
    boolean makePayment(CustomerOrder order, PaymentMethods paymentMethod);

    /**
     * Executes the payment operation.
     *
     * @return true if payment is successful, false otherwise
     */
    boolean Pay();

    /**
     * Processes a refund for a given payment.
     *
     * @param payment the payment to be refunded
     */
    void Refund(Payment payment);

    /**
     * Generates a receipt for a given customer order and payment.
     *
     * @param order the customer order
     * @param payment the payment details
     * @return a byte array representing the generated receipt PDF, or null if an error occurs
     */
    byte[] generateReceipt(CustomerOrder order, Payment payment);

    /**
     * Finds a Payment entity by its payment ID.
     *
     * @param paymentId the ID of the payment to find
     * @return the Payment entity with the specified payment ID
     */
    Payment findByPaymentId(int paymentId);
}