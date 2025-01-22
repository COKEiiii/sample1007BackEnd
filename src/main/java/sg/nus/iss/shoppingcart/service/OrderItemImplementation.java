package sg.nus.iss.shoppingcart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.nus.iss.shoppingcart.interfacemethods.OrderItemInterface;
import sg.nus.iss.shoppingcart.repository.OrderItemRepository;
import sg.nus.iss.shoppingcart.model.OrderItem;

import java.util.List;

/**
 * @ClassName OrderItemImplementation
 * @Description This class is used to implement the OrderItemInterface
 * @Author Chang Wang
 * @StudentID A0310544R
 * @Date 2024/10/5
 * @Version 1.0
 */
@Service
public class OrderItemImplementation implements OrderItemInterface {

    private final OrderItemRepository orderItemRepository;

    /**
     * Constructor for OrderItemImplementation.
     *
     * @param orderItemRepository the repository for order items
     */
    public OrderItemImplementation(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    /**
     * Adds a new order item.
     *
     * @param orderItem the order item to be added
     * @return the added OrderItem
     */
    @Transactional
    public OrderItem addOrderItem(OrderItem orderItem) {
        if (orderItem == null) {
            throw new IllegalArgumentException("OrderItem must not be null");
        }
        return orderItemRepository.save(orderItem);
    }

    /**
     * Updates an existing order item.
     *
     * @param itemId the ID of the order item to be updated
     * @param updatedItem the updated order item
     * @return the updated OrderItem
     */
    @Transactional
    public OrderItem updateOrderItem(Long itemId, OrderItem updatedItem) {
        if (updatedItem == null) {
            throw new IllegalArgumentException("UpdatedOrderItem must not be null");
        }
        OrderItem existingItem = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("OrderItem not found with ID: " + itemId));

        existingItem.setOrderItemName(updatedItem.getOrderItemName());
        existingItem.setQuantity(updatedItem.getQuantity());
        existingItem.setSubtotal(updatedItem.getSubtotal());

        return orderItemRepository.save(existingItem);
    }

    /**
     * Deletes an order item by its ID.
     *
     * @param itemId the ID of the order item to be deleted
     */
    @Transactional
    public void deleteOrderItem(Long itemId) {
        if (!orderItemRepository.existsById(itemId)) {
            throw new RuntimeException("OrderItem not found for deletion with ID: " + itemId);
        }
        orderItemRepository.deleteById(itemId);
    }

    /**
     * Retrieves order items by order ID.
     *
     * @param orderId the ID of the order
     * @return a list of OrderItem for the given order ID
     */
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findAllByOrderId(orderId);
    }
}
