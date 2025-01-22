package sg.nus.iss.shoppingcart.model;

import jakarta.persistence.*;
import sg.nus.iss.shoppingcart.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CustomerOrder
 * @Description This class represents an order in the shopping cart system.
 * @Author Chang Wang
 * @StudentID A0310544R
 * @Date 2024/10/2
 * @Version 1.0
 */

@Entity
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy="order",cascade=CascadeType.ALL)
    List<OrderItem> orderItems = new ArrayList<>();

    // Structured constructor
    public CustomerOrder(User user, Payment payment, OrderStatus status, BigDecimal totalPrice, LocalDateTime createdAt) {
        this.user = user;
        this.payment = payment;
        this.status = status;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }
    // Default constructor
    public CustomerOrder() {
    }

    // Getters and Setters

    public Integer getOrderId() {return orderId;}

    public void setOrderId(Integer orderId) {this.orderId = orderId;}

    public Long getUserId() {return user.getId();}   //Or you can use getUser then getId

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}

    public Payment getPayment() {return payment;}

    public void setPayment(Payment payment) {this.payment = payment;}

    public OrderStatus getStatus() {return status;}

    public void setStatus(OrderStatus status) {this.status = status;}

    //    public BigDecimal getTotalPrice() {return totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP);}
    public BigDecimal getTotalPrice() {return totalPrice;}


    public void setTotalPrice(BigDecimal totalPrice) {this.totalPrice = totalPrice;}

    public LocalDateTime getCreatedAt() {return createdAt;}

    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

    public List<OrderItem> getOrderItems() {return orderItems;}

    public void setOrderItem(OrderItem orderItems) {this.orderItems.add(orderItems);}

    public void setItems(List<OrderItem> items) {
        this.orderItems = items;
    }
}
