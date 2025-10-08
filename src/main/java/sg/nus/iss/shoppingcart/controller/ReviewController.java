package sg.nus.iss.shoppingcart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.shoppingcart.enums.ResponseStatus;
import sg.nus.iss.shoppingcart.interfacemethods.ReviewInterface;
import sg.nus.iss.shoppingcart.model.Review;
import sg.nus.iss.shoppingcart.utils.Response;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewInterface reviewInterface;

    public ReviewController(ReviewInterface reviewInterface) {
        this.reviewInterface = reviewInterface;
    }

    @PostMapping("/add")
    public ResponseEntity<Response<Review>> addReview(@RequestBody Review review) {
        try {
            Response<Review> response = reviewInterface.addReview(review);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Failed to add review: " + e.getMessage(), null));
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Response<List<Review>>> getReviewsByProduct(@PathVariable int productId) {
        try {
            Response<List<Review>> response = reviewInterface.getReviewsByProduct(productId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Failed to get reviews: " + e.getMessage(), null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Response<List<Review>>> getAllReviews() {
        try {
            Response<List<Review>> response = reviewInterface.getAllReviews();
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Failed to get all reviews: " + e.getMessage(), null));
        }
    }
}
