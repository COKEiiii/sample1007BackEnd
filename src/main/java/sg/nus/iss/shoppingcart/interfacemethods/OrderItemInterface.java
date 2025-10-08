package sg.nus.iss.shoppingcart.interfacemethods;

import sg.nus.iss.shoppingcart.model.OrderItem;

import java.util.List;

public interface OrderItemInterface {

    OrderItem addOrderItem(OrderItem orderItem);

    OrderItem updateOrderItem(Long itemId, OrderItem updatedItem);

    void deleteOrderItem(Long itemId);

    List<OrderItem> getOrderItemsByOrderId(Long orderId);
}