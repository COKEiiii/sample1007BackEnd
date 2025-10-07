package sg.nus.iss.shoppingcart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.shoppingcart.model.CustomerOrder;
import sg.nus.iss.shoppingcart.model.OrderItem;
import sg.nus.iss.shoppingcart.model.User;
import sg.nus.iss.shoppingcart.service.CustomerOrderImplementation;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class CustomerOrderController {

    private final CustomerOrderImplementation orderService;

    public CustomerOrderController(CustomerOrderImplementation orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<CustomerOrder> createOrder(@RequestBody User user) {
        try {
            CustomerOrder order = orderService.createOrder(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<List<CustomerOrder>> getUserOrders(@RequestBody User user) {
        try {
            List<CustomerOrder> orders = orderService.getOrdersByUser(user);
            return orders.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).build() : ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/order")
    public ResponseEntity<CustomerOrder> getOrderById(@RequestParam Long orderId) {
        try {
            CustomerOrder order = orderService.getOrderById(orderId);
            return order != null ? ResponseEntity.ok(order) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<CustomerOrder> updateOrder(@PathVariable Long orderId, @RequestBody List<OrderItem> items) {
        try {
            CustomerOrder updatedOrder = orderService.updateOrder(orderId, items);
            return updatedOrder != null ? ResponseEntity.ok(updatedOrder)
                    : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

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
