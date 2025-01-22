package sg.nus.iss.shoppingcart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.nus.iss.shoppingcart.interfacemethods.CustomerOrderInterface;
import sg.nus.iss.shoppingcart.model.*;
import sg.nus.iss.shoppingcart.repository.CustomerOrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName CustomerOrderImplementation
 * @Description Implementation of the CustomerOrderInterface, providing services related to customer orders.
 */
@Service
public class CustomerOrderImplementation implements CustomerOrderInterface {

    private final CustomerOrderRepository orderRepository;

    /**
     * Constructor for CustomerOrderImplementation.
     *
     * @param orderRepository the repository for customer orders
     */
    public CustomerOrderImplementation(CustomerOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Creates a new customer order.
     *
     * @param user the user placing the order
     * @return the created customer order
     * @throws IllegalArgumentException if the user or cart is null, or if the cart is empty
     * @throws RuntimeException if there is an error creating the order
     */
    @Transactional
    public CustomerOrder createOrder(User user) {
        try {
            if (user == null || user.getCart() == null) {
                throw new IllegalArgumentException("User or Cart cannot be null");
            }

            Cart cart = user.getCart();
            List<CartItem> cartItems = cart.getCartItems();

            if (cartItems.isEmpty()) {
                throw new IllegalArgumentException("Cart cannot be empty");
            }

            CustomerOrder order = new CustomerOrder();
            List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setSubtotal(cartItem.getsubTotalPrice());
                orderItem.setOrder(order);
                return orderItem;
            }).collect(Collectors.toList());

            order.setItems(orderItems);
            double totalPrice = orderItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
            order.setTotalPrice(BigDecimal.valueOf(totalPrice));

            return orderRepository.save(order);
        } catch (Exception e) {
            throw new RuntimeException("Error creating order: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves orders placed by a user.
     *
     * @param user the user whose orders are to be retrieved
     * @return a list of customer orders
     * @throws IllegalArgumentException if the user is null
     */
    public List<CustomerOrder> getOrdersByUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return user.getOrders();
    }

    /**
     * Retrieves a customer order by its ID.
     *
     * @param orderId the ID of the order to retrieve
     * @return the customer order
     * @throws RuntimeException if the order is not found or if there is an error retrieving the order
     */
    public CustomerOrder getOrderById(Long orderId) {
        try {
            return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving order: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing customer order.
     *
     * @param orderId the ID of the order to update
     * @param items the updated list of order items
     * @return the updated customer order
     * @throws IllegalArgumentException if the order items are null or empty
     * @throws RuntimeException if there is an error updating the order
     */
    @Transactional
    public CustomerOrder updateOrder(Long orderId, List<OrderItem> items) {
        try {
            if (items == null || items.isEmpty()) {
                throw new IllegalArgumentException("Order items cannot be null or empty");
            }

            CustomerOrder order = getOrderById(orderId);
            order.setItems(items);
            double totalPrice = items.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
            order.setTotalPrice(BigDecimal.valueOf(totalPrice));

            items.forEach(item -> item.setOrder(order));

            return orderRepository.save(order);
        } catch (Exception e) {
            throw new RuntimeException("Error updating order: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a customer order by its ID.
     *
     * @param orderId the ID of the order to delete
     * @throws RuntimeException if the order is not found or if there is an error deleting the order
     */
    public void deleteOrder(Long orderId) {
        try {
            if (!orderRepository.existsById(orderId)) {
                throw new RuntimeException("Order not found");
            }
            orderRepository.deleteById(orderId);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting order: " + e.getMessage(), e);
        }
    }
}