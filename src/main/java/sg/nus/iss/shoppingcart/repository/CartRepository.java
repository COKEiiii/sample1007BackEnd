package sg.nus.iss.shoppingcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import sg.nus.iss.shoppingcart.model.Cart;

/**
 * @ClassName CartRepository
 * @Description
 * @Author YAO YIYANG
 * @StudentID A0294873L
 * @Date 2024/10/3
 * @Version 1.0
 */

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
}
