package sg.nus.iss.shoppingcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sg.nus.iss.shoppingcart.model.OrderItem;

import java.util.List;

/**
 * @ClassName OrderItemRepository
 * @Description This class is used to access the database for OrderItem entity
 * @Author Chang Wang
 * @StudentID A0310544R
 * @Date 2024/10/3
 * @Version 1.0
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Finds all OrderItems by the given order ID.
     *
     * @param orderId the ID of the order
     * @return a list of OrderItems for the given order ID
     */
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.orderId = :orderId")
    List<OrderItem> findAllByOrderId(@Param("orderId") Long orderId);

}