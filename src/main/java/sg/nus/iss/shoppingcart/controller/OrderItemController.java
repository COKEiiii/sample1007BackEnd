package sg.nus.iss.shoppingcart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.shoppingcart.interfacemethods.OrderItemInterface;
import sg.nus.iss.shoppingcart.model.OrderItem;

import java.util.List;

@RestController
@RequestMapping("/order-items")
public class OrderItemController {

    private final OrderItemInterface orderItemService;

    public OrderItemController(OrderItemInterface orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PostMapping
    public ResponseEntity<String> addOrderItem(@RequestBody OrderItem orderItem) {
        try {
            if (orderItemService.addOrderItem(orderItem) != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Order item added successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order item not added");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while adding the order item");
        }
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<String> updateOrderItem(@PathVariable Long itemId, @RequestBody OrderItem updatedItem) {
        try {
            if (orderItemService.updateOrderItem(itemId, updatedItem) != null) {
                return ResponseEntity.status(HttpStatus.OK).body("Order item updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order item not updated");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the order item");
        }
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> deleteOrderItem(@PathVariable Long itemId) {
        try {
            orderItemService.deleteOrderItem(itemId);
            return ResponseEntity.status(HttpStatus.OK).body("Order item deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the order item");
        }
    }

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
