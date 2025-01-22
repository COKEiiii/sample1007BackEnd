package sg.nus.iss.shoppingcart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.shoppingcart.enums.ResponseStatus;
import sg.nus.iss.shoppingcart.interfacemethods.ReviewInterface;
import sg.nus.iss.shoppingcart.model.Review;
import sg.nus.iss.shoppingcart.utils.Response;

import java.util.List;

/**
 * @ClassName ReviewController
 * @Description Handles review related operations like adding a review, viewing all reviews
 */
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewInterface reviewInterface;

    /**
     * Constructor for ReviewController.
     *
     * @param reviewInterface the implementation of ReviewInterface
     */
    public ReviewController(ReviewInterface reviewInterface) {
        this.reviewInterface = reviewInterface;
    }

    /**
     * Adds a new review.
     *
     * @param review the review to add
     * @return a ResponseEntity containing the added review or an error message
     */
    @PostMapping("/add")
    public ResponseEntity<Response<Review>> addReview(@RequestBody Review review) {
        try {
            Response<Review> response = reviewInterface.addReview(review);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR, "Failed to add review: " + e.getMessage(), null));
        }
    }

    /**
     * Retrieves reviews for a specific product by its ID.
     *
     * @param productId the ID of the product
     * @return a ResponseEntity containing a list of reviews for the given product or an error message
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<Response<List<Review>>> getReviewsByProduct(@PathVariable int productId) {
        try {
            Response<List<Review>> response = reviewInterface.getReviewsByProduct(productId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR, "Failed to get reviews: " + e.getMessage(), null));
        }
    }

    /**
     * Retrieves all reviews.
     *
     * @return a ResponseEntity containing a list of all reviews or an error message
     */
    @GetMapping("/all")
    public ResponseEntity<Response<List<Review>>> getAllReviews() {
        try {
            Response<List<Review>> response = reviewInterface.getAllReviews();
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR, "Failed to get all reviews: " + e.getMessage(), null));
        }
    }
}
