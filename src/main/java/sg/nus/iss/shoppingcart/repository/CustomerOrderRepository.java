package sg.nus.iss.shoppingcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sg.nus.iss.shoppingcart.model.CustomerOrder;

import java.util.List;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    @Query("SELECT o FROM CustomerOrder o WHERE o.user.id = :userId")
    List<CustomerOrder> findByUserId(@Param("userId") Long userId);
}