package sg.nus.iss.shoppingcart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import sg.nus.iss.shoppingcart.controller.ReviewController;
import sg.nus.iss.shoppingcart.enums.ResponseStatus;
import sg.nus.iss.shoppingcart.interfacemethods.ReviewInterface;
import sg.nus.iss.shoppingcart.model.Review;
import sg.nus.iss.shoppingcart.service.ReviewImplementation;
import sg.nus.iss.shoppingcart.utils.Response;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest
@WebAppConfiguration
class ReviewControllerTest {

    private MockMvc mockMvc;

    private ReviewController reviewController;

    @Mock
    private ReviewImplementation reviewInterface;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reviewController = new ReviewController(reviewInterface);
    }

    // Test the happy path for adding a review
    @Test
    void testAddReview_HappyPath() {
        Review review = new Review(); // Assume the Review object is initialized
        Response<Review> response = new Response<>(ResponseStatus.SUCCESS, "Review added successfully", review);
        when(reviewInterface.addReview(any())).thenReturn(response);

        ResponseEntity<Response<Review>> result = reviewController.addReview(review);

        assertEquals(OK, result.getStatusCode());
        assertEquals("Review added successfully", result.getBody().getMessage());
    }

    // Test exception scenario when adding a review
    @Test
    void testAddReview_Exception() {
        Review review = new Review(); // Assume the Review object is initialized
        when(reviewInterface.addReview(any())).thenThrow(new RuntimeException("Server error"));

        ResponseEntity<Response<Review>> result = reviewController.addReview(review);

        assertEquals(INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertTrue(result.getBody().getMessage().contains("Failed to add review"));
    }

    // Test the happy path for getting reviews by product
    @Test
    void testGetReviewsByProduct_HappyPath() {
        int productId = 1;
        Review review = new Review(); // Assume the Review object is initialized
        Response<List<Review>> response = new Response<>(ResponseStatus.SUCCESS, "Fetch successful", Arrays.asList(review));
        when(reviewInterface.getReviewsByProduct(productId)).thenReturn(response);

        ResponseEntity<Response<List<Review>>> result = reviewController.getReviewsByProduct(productId);

        assertEquals(OK, result.getStatusCode());
        assertEquals("Fetch successful", result.getBody().getMessage());
    }

    // Test exception scenario when getting reviews by product
    @Test
    void testGetReviewsByProduct_Exception() {
        int productId = 1;
        when(reviewInterface.getReviewsByProduct(productId)).thenThrow(new RuntimeException("Server error"));

        ResponseEntity<Response<List<Review>>> result = reviewController.getReviewsByProduct(productId);

        assertEquals(INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertTrue(result.getBody().getMessage().contains("Failed to fetch reviews"));
    }

    // Test the happy path for getting all reviews
    @Test
    void testGetAllReviews_HappyPath() {
        Review review = new Review(); // Assume the Review object is initialized
        Response<List<Review>> response = new Response<>(ResponseStatus.SUCCESS, "Successfully fetched all reviews", Arrays.asList(review));
        when(reviewInterface.getAllReviews()).thenReturn(response);

        ResponseEntity<Response<List<Review>>> result = reviewController.getAllReviews();

        assertEquals(OK, result.getStatusCode());
        assertEquals("Successfully fetched all reviews", result.getBody().getMessage());
    }

    // Test exception scenario when getting all reviews
    @Test
    void testGetAllReviews_Exception() {
        when(reviewInterface.getAllReviews()).thenThrow(new RuntimeException("Server error"));

        ResponseEntity<Response<List<Review>>> result = reviewController.getAllReviews();

        assertEquals(INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertTrue(result.getBody().getMessage().contains("Failed to fetch all reviews"));
    }
}
