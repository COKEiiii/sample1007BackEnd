package sg.nus.iss.shoppingcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sg.nus.iss.shoppingcart.model.CustomerOrder;

import java.util.List;

/**
 * @ClassName CustomerOrderRepository
 * @Description Repository interface for CustomerOrder entity, providing CRUD operations and custom queries.
 * @Author Chang Wang
 * @StudentID A0310544R
 * @Date 2024/10/3
 * @Version 1.0
 */
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    /**
     * Finds customer orders by user ID.
     *
     * @param userId the ID of the user
     * @return a list of CustomerOrder for the given user ID
     */
    @Query("SELECT o FROM CustomerOrder o WHERE o.user.id = :userId")
    List<CustomerOrder> findByUserId(@Param("userId") Long userId);
}