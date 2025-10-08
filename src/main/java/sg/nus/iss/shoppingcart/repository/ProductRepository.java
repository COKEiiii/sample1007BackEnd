package sg.nus.iss.shoppingcart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;
import sg.nus.iss.shoppingcart.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.name LIKE CONCAT ('%', :k, '%')")
    List<Product> SearchProductByName(@Param("k") String keyword);

    @Query("SELECT p FROM Product p WHERE p.category = :category")
    List<Product> SearchProductByCategory(@Param("category") String category);

    @Query("Select p from Product as p where p.status='AVAILABLE' and (p.name like CONCAT('%',:k,'%') OR  p.category.name like CONCAT('%',:k,'%'))")
    List<Product> SearchAvailableProductByName(@Param("k") String keyword);

    @Query("Select p from Product as p where p.status='AVAILABLE' and p.name like CONCAT('%',:k,'%') and p.category.name like CONCAT('%',:c,'%') and p.price between :minPrice and :maxPrice")
    List<Product> SearchProductsByFilters(@Param("k") String keyword, @Param("c") String category,
            @Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice);
}