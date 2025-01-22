package sg.nus.iss.shoppingcart.enums;

/**
 * @ClassName PaymentStatus
 * @Description
 * @Author V_E
 * @StudentID A0307246H
 * @Date 2024/10/2
 * @Version 1.0
 */

/**
 * Enum representing the status of a payment.
 */
public enum PaymentStatus {
    /**
     * Payment.java has not been made.
     */
    UNPAID,

    /**
     * Payment.java has been completed.
     */
    PAID,

    /**
     * Payment.java has been refunded.
     */
    REFUNDED
}