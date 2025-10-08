package sg.nus.iss.shoppingcart.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cartItemId;

	@ManyToOne
	@JoinColumn(name = "cart_id", nullable = false)
	private Cart cart;

	@OneToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(nullable = false)
	@Min(value = 1, message = "Quantity must be at least 1")
	private int quantity;

	@Min(value = 1, message = "Subtotal must be at least 1")
	@Column(nullable = false)
	private double subTotalPrice;

	public CartItem() {
		super();
		this.quantity = 1;
		this.subTotalPrice = 0.0;
	}

	public CartItem(Cart cart, Product product) {
		super();
		this.cart = cart;
		this.product = product;
	}

	public int getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(int cartItemId) {
		this.cartItemId = cartItemId;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getsubTotalPrice() {
		return subTotalPrice;
	}

	public void setsubTotalPrice(double subTotalPrice) {
		this.subTotalPrice = subTotalPrice;
	}
}
