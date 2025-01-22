package sg.nus.iss.shoppingcart.model;

import jakarta.persistence.*;
import sg.nus.iss.shoppingcart.model.User;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cartId; //primary key for Cart Table

	//修改
	@OneToOne(mappedBy = "cart")
	private User user; //// This field represents the relationship to the User entity (foreign key)

	//修改
	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
	List<CartItem> cartItems; // List of CartItem objects in the Cart
	
	public Cart() {
		super();
	}
	
	// This field represents the relationship to the User entity (foreign key)
	public Cart(User user) {
		super();
		this.user = user;
	}
	
	// Getter Method - for the cartId field, used to retrieve the cart ID
	public int getCartId() {
		return cartId;
	}
	
	  // Setter method for the cartId field, used to set the cart ID
	public void setCartId(int cartId) {
		this.cartId = cartId;
	}
	
	// Getter method for the user field, used to retrieve the User associated with the Cart
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}


	//修改
	public List<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItem(CartItem cartItem) {
		this.cartItems.add(cartItem);
	}

}
