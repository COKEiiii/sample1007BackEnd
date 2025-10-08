package sg.nus.iss.shoppingcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.nus.iss.shoppingcart.model.Cart;
import sg.nus.iss.shoppingcart.model.User;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    // 根据用户查找购物车
    Cart findByUser(User user);
}