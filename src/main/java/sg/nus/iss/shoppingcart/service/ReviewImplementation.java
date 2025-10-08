package sg.nus.iss.shoppingcart.service;

import org.springframework.stereotype.Service;
import sg.nus.iss.shoppingcart.interfacemethods.ReviewInterface;
import sg.nus.iss.shoppingcart.model.Review;
import sg.nus.iss.shoppingcart.repository.ReviewRepository;
import sg.nus.iss.shoppingcart.utils.Response;
import sg.nus.iss.shoppingcart.enums.ResponseStatus;

import java.util.List;

@Service
public class ReviewImplementation implements ReviewInterface {

    private final ReviewRepository reviewRepository;

    public ReviewImplementation(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Response<Review> addReview(Review review) {
        try {
            Review savedReview = reviewRepository.save(review);
            return new Response<>(ResponseStatus.SUCCESS, "Review added successfully", savedReview);
        } catch (Exception e) {
            return new Response<>(ResponseStatus.BAD_REQUEST, "Failed to add review: " + e.getMessage());
        }
    }

    @Override
    public Response<List<Review>> getReviewsByProduct(int productId) {
        try {
            List<Review> reviews = reviewRepository.findByProductId(productId);
            if (reviews.isEmpty()) {
                return new Response<>(ResponseStatus.NOT_FOUND, "No reviews found for the product", null);
            }
            return new Response<>(ResponseStatus.SUCCESS, "Reviews retrieved successfully", reviews);
        } catch (Exception e) {
            return new Response<>(ResponseStatus.BAD_REQUEST, "Error retrieving reviews: " + e.getMessage(), null);
        }
    }

    @Override
    public Response<List<Review>> getAllReviews() {
        try {
            List<Review> reviews = reviewRepository.findAll();
            return new Response<>(ResponseStatus.SUCCESS, "All reviews retrieved successfully", reviews);
        } catch (Exception e) {
            return new Response<>(ResponseStatus.BAD_REQUEST, "Error retrieving reviews: " + e.getMessage(), null);
        }
    }
}
