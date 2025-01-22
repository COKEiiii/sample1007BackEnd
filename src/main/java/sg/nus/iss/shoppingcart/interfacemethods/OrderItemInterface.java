package sg.nus.iss.shoppingcart.interfacemethods;

import sg.nus.iss.shoppingcart.model.OrderItem;

import java.util.List;

/**
 * @ClassName OrderItemInterface
 * @Description This interface defines the methods that can be used to manipulate OrderItem objects in the database.
 * @Author Chang Wang
 * @StudentID A0310544R
 * @Date 2024/10/5
 * @Version 1.0
 */
public interface OrderItemInterface {

    /**
     * Adds a new OrderItem to the database.
     *
     * @param orderItem the OrderItem to add
     * @return the added OrderItem
     */
    OrderItem addOrderItem(OrderItem orderItem);

    /**
     * Updates an existing OrderItem in the database.
     *
     * @param itemId the ID of the OrderItem to update
     * @param updatedItem the updated OrderItem object
     * @return the updated OrderItem
     */
    OrderItem updateOrderItem(Long itemId, OrderItem updatedItem);

    /**
     * Deletes an OrderItem from the database.
     *
     * @param itemId the ID of the OrderItem to delete
     */
    void deleteOrderItem(Long itemId);

    /**
     * Retrieves a list of OrderItems by the order ID.
     *
     * @param orderId the ID of the order
     * @return a list of OrderItems for the given order ID
     */
    List<OrderItem> getOrderItemsByOrderId(Long orderId);
}