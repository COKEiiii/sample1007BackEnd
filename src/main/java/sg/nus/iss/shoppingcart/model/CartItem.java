package sg.nus.iss.shoppingcart.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
public class CartItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cartItemId; //primary key for Cart.java Item Table
	
	@ManyToOne
	@JoinColumn(name = "cart_id", nullable = false) // Foreign key to CartItem
	private Cart cart;
	
	@OneToOne
	@JoinColumn(name = "product_id", nullable = false) //// Foreign key to CartItem
	private Product product;
	
	@Column(nullable = false)
	@Min(
			value = 1,
			message = "Quantity must be at least 1"
	) // Ensure quantity is at least 1
	private int quantity;

	@Min(
			value = 1,
			message = "Subtotal must be at least 1"
	) // Ensure subtotal is at least 1
	@Column(nullable = false) // Define precision and scale for decimal values
	private double subTotalPrice;
	
	public CartItem() {
		super();
		this.quantity = 1; //Default value
		this.subTotalPrice = 0.0;
	}
	
	//Added a constructor that initializes both Cart.java and Product, with defaults for quantity and subTotalPrice.
	public CartItem(Cart cart, Product product) {
		super();
		this.cart = cart;
		this.product = product;
	}
	
	
	// Getter for cartItemId
	public int getCartItemId() {
		return cartItemId;
	}
	
	// Setter for cartItemId
	public void setCartItemId(int cartItemId) {
		this.cartItemId = cartItemId;
	}
	
	//Getter for cart
	public Cart getCart( ) {
		return cart;
	}
	
	// Setter for cart
	public void setCart(Cart cart) {
		this.cart = cart;
	}
	
	//Getter for product
	public Product getProduct() {
		return product;
	}
	
	//Setter for product
	public void setProduct(Product product) {
		this.product = product;
	}
	
	//Getter for qty
	public int getQuantity() {
		return quantity;
	}
	
	//Setter for qty
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	//Getter for subtotal
	public double getsubTotalPrice() {
		return subTotalPrice;
	}
	
	//Setter for subtotal
	public void setsubTotalPrice(double subTotalPrice) {
		this.subTotalPrice=subTotalPrice;
	}
}
