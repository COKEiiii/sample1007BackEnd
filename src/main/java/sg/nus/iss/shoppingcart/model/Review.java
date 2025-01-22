package sg.nus.iss.shoppingcart.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Max(value = 5, message = "Rating must be less than 5")
    @Min(value = 1, message = "Rating must be at least 1")
    private int rating; // Rating out of 5

    private String content;

    //New field for image path
    private String imagePath;     //Store image path

    public Review() {
    }

    public Review(User user,Product product, int rating, String content) {
        this.user = user;
        this.product = product;
        this.rating = rating;
        this.content = content;
        this.imagePath = imagePath;
    }

    // Getters and setters
    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return content;
    }

    public void setComment(String content) {
        this.content = content;
    }
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", user=" + user.getUsername() +
                ", product=" + product.getName() +
                ", rating=" + rating +
                ", content='" + content + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
        }
    }
