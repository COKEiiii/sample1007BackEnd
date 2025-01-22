package sg.nus.iss.shoppingcart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;
import sg.nus.iss.shoppingcart.model.CartItem;

/**
 * @ClassName CartItemRepository
 * @Description Repository interface for CartItem entity, providing CRUD operations and custom queries.
 * @Author YAO YIYANG
 * @StudentID A0294873L
 * @Date 2024/10/3
 * @Version 1.0
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    /**
     * Fuzzy search for products by name or category.
     *
     * @param keyword the keyword to search for in product name or category
     * @return a list of CartItem matching the search criteria
     */
    @Query("Select c from CartItem as c where c.product.name like CONCAT('%',:k,'%') AND c.product.category.name like CONCAT('%',:k,'%')")
    List<CartItem> SearchCartItemByName(@Param("k") String keyword);
}