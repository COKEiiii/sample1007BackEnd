package sg.nus.iss.shoppingcart.interfacemethods;

import sg.nus.iss.shoppingcart.model.Review;
import sg.nus.iss.shoppingcart.utils.Response;

import java.util.List;

public interface ReviewInterface {

    public Response<Review> addReview(Review review);

    public Response<List<Review>> getReviewsByProduct(int productId);

    public Response<List<Review>> getAllReviews();
}