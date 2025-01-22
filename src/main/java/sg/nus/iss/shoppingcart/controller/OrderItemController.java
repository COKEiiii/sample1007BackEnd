package sg.nus.iss.shoppingcart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.shoppingcart.interfacemethods.OrderItemInterface;
import sg.nus.iss.shoppingcart.model.OrderItem;

import java.util.List;

/**
 * @ClassName OrderItemController
 * @Description This class is used to handle HTTP requests for OrderItem entity
 * @Author Chang Wang
 * @StudentID A0310544R
 * @Date 2024/10/5
 * @Version 1.0
 */

@RestController
@RequestMapping("/order-items")
public class OrderItemController {

    private final OrderItemInterface orderItemService;

    /**
     * Constructor for OrderItemController.
     *
     * @param orderItemService the implementation of OrderItemInterface
     */
    public OrderItemController(OrderItemInterface orderItemService) {
        this.orderItemService = orderItemService;
    }

    /**
     * Adds a new order item.
     *
     * @param orderItem the order item to add
     * @return a ResponseEntity containing a success or error message
     */
    @PostMapping
    public ResponseEntity<String> addOrderItem(@RequestBody OrderItem orderItem) {
        try {
            if (orderItemService.addOrderItem(orderItem) != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Order item added successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order item not added");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding the order item");
        }
    }

    /**
     * Updates an existing order item.
     *
     * @param itemId the ID of the order item to update
     * @param updatedItem the updated order item object
     * @return a ResponseEntity containing a success or error message
     */
    @PutMapping("/{itemId}")
    public ResponseEntity<String> updateOrderItem(@PathVariable Long itemId, @RequestBody OrderItem updatedItem) {
        try {
            if (orderItemService.updateOrderItem(itemId, updatedItem) != null) {
                return ResponseEntity.status(HttpStatus.OK).body("Order item updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order item not updated");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the order item");
        }
    }

    /**
     * Deletes an order item by its ID.
     *
     * @param itemId the ID of the order item to delete
     * @return a ResponseEntity containing a success message
     */
    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> deleteOrderItem(@PathVariable Long itemId) {
        try {
            orderItemService.deleteOrderItem(itemId);
            return ResponseEntity.status(HttpStatus.OK).body("Order item deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the order item");
        }
    }

    /**
     * Retrieves order items for a specific order.
     *
     * @param orderId the ID of the order
     * @return a ResponseEntity containing the list of order items or a NO_CONTENT status
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItem>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        try {
            List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderId(orderId);
            if (orderItems.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(orderItems);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
