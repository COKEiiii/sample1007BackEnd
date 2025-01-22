package sg.nus.iss.shoppingcart.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import sg.nus.iss.shoppingcart.enums.ProductStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static sg.nus.iss.shoppingcart.enums.ProductStatus.AVAILABLE;

/**
 * @ClassName Product
 * @Description
 * @Author YAO YIYANG
 * @StudentID A0294873L
 * @Date 2024/10/2
 * @Version 1.0
 */

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ProductId;

    @Column(nullable = false)
    private String name;

    private String description;
    
    @Column(nullable = false)
    private double price;

	@Min(
			value = 0,
			message = "Stock must be at least 0"
	)
    @Column(nullable = false)
    private int stock;

	@Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    private LocalDateTime createdAt = LocalDateTime.now();

	private String imageUrl;
    
    @ManyToOne
    @JoinColumn(name="category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    public Product() {}

    public Product(String name, String description, double price, int stock, ProductStatus productStatus,String imageUrl) {
        this.setName(name);
        this.setDescription(description);
        this.setPrice(price);
        this.setStock(stock);
        this.setStatus(productStatus);
		this.setImageUrl(imageUrl);
    }

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public ProductStatus getStatus() {
		return status;
	}

	public void setStatus(ProductStatus status) {
		this.status = status;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return ProductId;
	}

	public void setId(int id) {
		this.ProductId = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<Review> getReview() {
		return reviews;
	}

	public void setReview(Review review) {
		this.reviews.add(review);
	}
	
	@Override
	public String toString() {
		return "Product [id="+ProductId+", name="+name+", description="+description+", price="+price+", stock="+stock+", status="+status+"]";
	}
}
