package sg.nus.iss.shoppingcart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sg.nus.iss.shoppingcart.model.CartItem;
import sg.nus.iss.shoppingcart.model.Cart;
import sg.nus.iss.shoppingcart.model.Product;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    @Query("Select c from CartItem as c where c.product.name like CONCAT('%',:k,'%') AND c.product.category.name like CONCAT('%',:k,'%')")
    List<CartItem> SearchCartItemByName(@Param("k") String keyword);
    
    // 添加缺失的方法
    CartItem findByCartAndProduct(Cart cart, Product product);
    
    // 添加按购物车查找的方法
    List<CartItem> findByCart(Cart cart);
}