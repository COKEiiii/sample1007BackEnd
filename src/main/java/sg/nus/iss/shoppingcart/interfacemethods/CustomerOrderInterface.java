package sg.nus.iss.shoppingcart.interfacemethods;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sg.nus.iss.shoppingcart.model.CustomerOrder;
import sg.nus.iss.shoppingcart.model.OrderItem;
import sg.nus.iss.shoppingcart.model.User;

import java.util.List;

/**
 * @ClassName CustomerOrderInterface
 * @Description Interface for customer order operations, providing methods to manage customer orders.
 * @Author [Your Name]
 * @Date [Current Date]
 * @Version 1.0
 */
public interface CustomerOrderInterface {

    /**
     * Creates a new order for the given user.
     *
     * @param user the user for whom the order is created
     * @return the created CustomerOrder
     */
    CustomerOrder createOrder(User user);

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId the ID of the order to retrieve
     * @return the CustomerOrder with the given ID
     */
    CustomerOrder getOrderById(Long orderId);

    /**
     * Retrieves all orders for a given user.
     *
     * @param user the user whose orders are to be retrieved
     * @return a list of CustomerOrders for the given user
     */
    List<CustomerOrder> getOrdersByUser(User user);

    /**
     * Updates an existing order with the given items.
     *
     * @param orderId the ID of the order to update
     * @param items the list of OrderItems to update the order with
     * @return the updated CustomerOrder
     */
    CustomerOrder updateOrder(Long orderId, List<OrderItem> items);

    /**
     * Deletes an order by its ID.
     *
     * @param orderId the ID of the order to delete
     */
    void deleteOrder(Long orderId);
}