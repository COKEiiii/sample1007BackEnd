package sg.nus.iss.shoppingcart.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import sg.nus.iss.shoppingcart.enums.PaymentMethods;
import sg.nus.iss.shoppingcart.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentId;

    @OneToOne(mappedBy = "payment")
    private CustomerOrder order = new CustomerOrder();

    @Column(nullable = false)
    private LocalDateTime paymentTime = LocalDateTime.now();

    @Column(nullable = false)
    @Min(value = 1, message = "Amount must be at least 1")
    private BigDecimal amount;

    @Column(nullable = false)
    private PaymentMethods paymentMethod = PaymentMethods.CREDIT_CARD;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.UNPAID;

    public Payment() {
    }

    public Payment(CustomerOrder order, BigDecimal amount, PaymentMethods paymentMethod, PaymentStatus status) {
        this.order = order;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public CustomerOrder getOrder() {
        return order;
    }

    public void setOrder(CustomerOrder order) {
        this.order = order;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMethods getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethods paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}