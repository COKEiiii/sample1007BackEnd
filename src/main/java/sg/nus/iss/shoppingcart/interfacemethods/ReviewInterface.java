package sg.nus.iss.shoppingcart.interfacemethods;

import sg.nus.iss.shoppingcart.model.Review;
import sg.nus.iss.shoppingcart.utils.Response;

import java.util.List;

/**
 * @ClassName ReviewInterface
 * @Description Interface for review operations, providing methods to manage reviews.
 * @Author [Your Name]
 * @Date [Current Date]
 * @Version 1.0
 */
public interface ReviewInterface {

    /**
     * Adds a new review.
     *
     * @param review the review to add
     * @return a Response object containing the added review
     */
    public Response<Review> addReview(Review review);

    /**
     * Retrieves reviews for a specific product.
     *
     * @param productId the ID of the product
     * @return a Response object containing a list of reviews for the given product
     */
    public Response<List<Review>> getReviewsByProduct(int productId);

    /**
     * Retrieves all reviews.
     *
     * @return a Response object containing a list of all reviews
     */
    public Response<List<Review>> getAllReviews();
}