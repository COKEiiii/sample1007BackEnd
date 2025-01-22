package sg.nus.iss.shoppingcart.controller;

/**
 * @ClassName CustomerOrderController
 * @Description This class is used to handle customer order related requests
 * @Author Chang Wang
 * @StudentID A0310544R
 * @Date 2024/10/3
 * @Version 1.0
 */
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.shoppingcart.model.CustomerOrder;
import sg.nus.iss.shoppingcart.model.OrderItem;
import sg.nus.iss.shoppingcart.model.User;
import sg.nus.iss.shoppingcart.service.CustomerOrderImplementation;

import java.util.List;

/**
 * @ClassName CustomerOrderController
 * @Description This class is used to handle customer order related requests
 * @Author Chang Wang
 * @StudentID A0310544R
 * @Date 2024/10/5
 * @Version 1.1
 */

@RestController
@RequestMapping("/orders")
public class CustomerOrderController {

    private final CustomerOrderImplementation orderService;

    /**
     * Constructor for CustomerOrderController.
     *
     * @param orderService the implementation of CustomerOrderImplementation
     */
    public CustomerOrderController(CustomerOrderImplementation orderService) {
        this.orderService = orderService;
    }

    /**
     * Creates a new customer order.
     *
     * @param user the user for whom the order is created
     * @return a ResponseEntity containing the created CustomerOrder
     */
    @PostMapping("/create")
    public ResponseEntity<CustomerOrder> createOrder(@RequestBody User user) {
        try {
            CustomerOrder order = orderService.createOrder(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Retrieves orders for a specific user.
     *
     * @param user whose orders are to be retrieved
     * @return a ResponseEntity containing the list of CustomerOrders or a NO_CONTENT status
     */
    @PostMapping("/{userId}/orders")
    public ResponseEntity<List<CustomerOrder>> getUserOrders(@RequestBody User user) {
        try {
            List<CustomerOrder> orders = orderService.getOrdersByUser(user);
            return orders.isEmpty() ?
                    ResponseEntity.status(HttpStatus.NO_CONTENT).build() :
                    ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId the ID of the order to retrieve
     * @return a ResponseEntity containing the CustomerOrder or a NO_CONTENT status
     */
    @GetMapping("/order")
    public ResponseEntity<CustomerOrder> getOrderById(@RequestParam Long orderId) {
        try {
            CustomerOrder order = orderService.getOrderById(orderId);
            return order != null ?
                    ResponseEntity.ok(order) :
                    ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Updates an existing order.
     *
     * @param orderId the ID of the order to update
     * @param items   the list of OrderItems to update
     * @return a ResponseEntity containing the updated CustomerOrder or a NO_CONTENT status
     */
    @PutMapping("/{orderId}")
    public ResponseEntity<CustomerOrder> updateOrder(@PathVariable Long orderId, @RequestBody List<OrderItem> items) {
        try {
            CustomerOrder updatedOrder = orderService.updateOrder(orderId, items);
            return updatedOrder != null ?
                    ResponseEntity.ok(updatedOrder) :
                    ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Deletes an order by its ID.
     *
     * @param orderId the ID of the order to delete
     * @return a ResponseEntity containing a success message
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Order deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting order");
        }
    }
}
