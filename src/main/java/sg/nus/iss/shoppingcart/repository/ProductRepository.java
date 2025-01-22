package sg.nus.iss.shoppingcart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;
import sg.nus.iss.shoppingcart.model.Product;

/**
 * @ClassName ProductRepository
 * @Description Repository interface for Product entity, providing CRUD operations and custom queries.
 * @Author YAO YIYANG & Alifah
 * @StudentID A0294873L & A0295324B
 * @Date 2024/10/3
 * @Version 1.0
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    /**
     * Custom query to search products by name with 'like' for partial matches.
     *
     * @param keyword the keyword to search for in product name
     * @return a list of products matching the search criteria
     */
    @Query("SELECT p FROM Product p WHERE p.name LIKE CONCAT ('%', :k, '%')")
    List<Product> SearchProductByName(@Param("k") String keyword);

    /**
     * Custom query to search products by category.
     *
     * @param category the category to search for
     * @return a list of products matching the search criteria
     */
    @Query("SELECT p FROM Product p WHERE p.category = :category")
    List<Product> SearchProductByCategory(@Param("category") String category);

    /**
     * Fuzzy search for products by name or category.
     *
     * @param keyword the keyword to search for in product name or category
     * @return a list of available products matching the search criteria
     */
    @Query("Select p from Product as p where p.status='AVAILABLE' and (p.name like CONCAT('%',:k,'%') OR  p.category.name like CONCAT('%',:k,'%'))")
    List<Product> SearchAvailableProductByName(@Param("k") String keyword);

    /**
     * Search products by multiple filters including name, category, and price range.
     *
     * @param keyword the keyword to search for in product name
     * @param category the category to search for
     * @param minPrice the minimum price of the product
     * @param maxPrice the maximum price of the product
     * @return a list of available products matching the search criteria
     */
    @Query("Select p from Product as p where p.status='AVAILABLE' and p.name like CONCAT('%',:k,'%') and p.category.name like CONCAT('%',:c,'%') and p.price between :minPrice and :maxPrice")
    List<Product> SearchProductsByFilters(@Param("k") String keyword, @Param("c") String category, @Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice);
}