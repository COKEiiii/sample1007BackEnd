package sg.nus.iss.shoppingcart.model;

import jakarta.persistence.*;
import sg.nus.iss.shoppingcart.model.User;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cartId;

	@OneToOne(mappedBy = "cart")
	private User user;

	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
	List<CartItem> cartItems;

	public Cart() {
		super();
	}

	public Cart(User user) {
		super();
		this.user = user;
	}

	public int getCartId() {
		return cartId;
	}

	public void setCartId(int cartId) {
		this.cartId = cartId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItem(CartItem cartItem) {
		this.cartItems.add(cartItem);
	}

}
