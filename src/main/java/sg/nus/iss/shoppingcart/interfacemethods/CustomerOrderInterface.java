package sg.nus.iss.shoppingcart.interfacemethods;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sg.nus.iss.shoppingcart.model.CustomerOrder;
import sg.nus.iss.shoppingcart.model.OrderItem;
import sg.nus.iss.shoppingcart.model.User;

import java.util.List;

public interface CustomerOrderInterface {

    CustomerOrder createOrder(User user);

    CustomerOrder getOrderById(Long orderId);

    List<CustomerOrder> getOrdersByUser(User user);

    CustomerOrder updateOrder(Long orderId, List<OrderItem> items);

    void deleteOrder(Long orderId);
}