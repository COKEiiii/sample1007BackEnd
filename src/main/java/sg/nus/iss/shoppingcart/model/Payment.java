package sg.nus.iss.shoppingcart.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import sg.nus.iss.shoppingcart.enums.PaymentMethods;
import sg.nus.iss.shoppingcart.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class representing a Payment in the shopping cart system.
 */
@Entity
public class Payment {
    /**
     * Unique identifier for the payment.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentId;

    /**
     * The order associated with this payment.
     */
    @OneToOne(mappedBy = "payment")
    private CustomerOrder order = new CustomerOrder(); // default order avoid null pointer exception

    /**
     * The time when the payment was made.
     */
    @Column(nullable = false)
    private LocalDateTime paymentTime = LocalDateTime.now();  // default time is now

    /**
     * The amount of the payment.
     */
    @Column(nullable = false)
    @Min(
            value = 1,
            message = "Amount must be at least 1"
    )
    private BigDecimal amount;

    /**
     * The method used for the payment.
     */
    @Column(nullable = false)
    private PaymentMethods paymentMethod = PaymentMethods.CREDIT_CARD;  // default method is credit card

    /**
     * The status of the payment.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.UNPAID;  // default status is unpaid

    /**
     * Default constructor.
     */
    public Payment() {
    }

    /**
     * Constructs a new Payment with the specified order, amount, payment method, and status.
     *
     * @param order the order associated with this payment ,Because create a new payment must have an order first
     * @param amount the amount of the payment
     * @param paymentMethod the method used for the payment
     * @param status the status of the payment
     */
    public Payment(CustomerOrder order, BigDecimal amount, PaymentMethods paymentMethod, PaymentStatus status) {
        this.order = order;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    /**
     * Gets the payment ID.
     *
     * @return the payment ID
     */
    public int getPaymentId() {
        return paymentId;
    }

    /**
     * Gets the order associated with this payment.
     *
     * @return the order
     */
    public CustomerOrder getOrder() {
        return order;
    }

    /**
     * Sets the order associated with this payment.
     *
     * @param order the order to set
     */
    public void setOrder(CustomerOrder order) {
        this.order = order;
    }

    /**
     * Gets the payment time.
     *
     * @return the payment time
     */
    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    /**
     * Gets the amount of the payment.
     *
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the payment.
     *
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Gets the payment method.
     *
     * @return the payment method
     */
    public PaymentMethods getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Sets the payment method.
     *
     * @param paymentMethod the payment method to set
     */
    public void setPaymentMethod(PaymentMethods paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * Gets the payment status.
     *
     * @return the payment status
     */
    public PaymentStatus getStatus() {
        return status;
    }

    /**
     * Sets the payment status.
     *
     * @param status the payment status to set
     */
    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}