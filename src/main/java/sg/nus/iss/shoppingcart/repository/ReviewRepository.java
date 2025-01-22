package sg.nus.iss.shoppingcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sg.nus.iss.shoppingcart.model.Review;

import java.util.List;

/**
 * @ClassName ReviewRepository
 * @Description Repository interface for Review entity, providing CRUD operations and custom queries.
 * @Author [Your Name]
 * @Date [Current Date]
 * @Version 1.0
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    /**
     * Finds reviews by product ID.
     *
     * @param productId the ID of the product
     * @return a list of reviews for the given product ID
     */
    List<Review> findByProductId(int productId);
}