package sg.nus.iss.shoppingcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sg.nus.iss.shoppingcart.model.Category;
import sg.nus.iss.shoppingcart.model.Product;

import java.util.List;

/**
 * @ClassName CategoryRepository
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2024/10/8
 * @Version 1.0
 */

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
