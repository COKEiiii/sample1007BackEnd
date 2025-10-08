package sg.nus.iss.shoppingcart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.nus.iss.shoppingcart.interfacemethods.OrderItemInterface;
import sg.nus.iss.shoppingcart.repository.OrderItemRepository;
import sg.nus.iss.shoppingcart.model.OrderItem;

import java.util.List;

@Service
public class OrderItemImplementation implements OrderItemInterface {

    private final OrderItemRepository orderItemRepository;

    public OrderItemImplementation(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public OrderItem addOrderItem(OrderItem orderItem) {
        if (orderItem == null) {
            throw new IllegalArgumentException("OrderItem must not be null");
        }
        return orderItemRepository.save(orderItem);
    }

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

    @Transactional
    public void deleteOrderItem(Long itemId) {
        if (!orderItemRepository.existsById(itemId)) {
            throw new RuntimeException("OrderItem not found for deletion with ID: " + itemId);
        }
        orderItemRepository.deleteById(itemId);
    }

    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findAllByOrderId(orderId);
    }
}
