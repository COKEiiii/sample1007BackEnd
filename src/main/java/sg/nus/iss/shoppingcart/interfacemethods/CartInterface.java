package sg.nus.iss.shoppingcart.interfacemethods;

import java.util.List;

import sg.nus.iss.shoppingcart.model.Cart;
import sg.nus.iss.shoppingcart.model.CartItem;
import sg.nus.iss.shoppingcart.model.DTO.CartItemDTO;
import sg.nus.iss.shoppingcart.model.Product;

/**
 * @ClassName CartInterface
 * @Description Interface for cart operations, providing methods to manage products and cart items.
 * @Author YAO YIYANG
 * @StudentID A0294873L
 * @Date 2024/10/3
 * @Version 1.0
 */
public interface CartInterface {

    Cart addProductToCart(Long userId, Long productId, Long quantity);

    /**
     * Retrieves all products.
     *
     * @return a list of all products
     */
    public List<Product> findAllProducts();

    /**
     * Searches for products by name.
     *
     * @param name the name of the product to search for
     * @return a list of products matching the search criteria
     */
    public List<Product> SearchProductByName(String name);

    /**
     * Finds a product by its ID.
     *
     * @param id the ID of the product
     * @return the product with the given ID
     */
    public Product findProductById(Integer id);

    /**
     * Adds a product to the cart.
     *
     * @param productId the ID of the product to add
     * @param quantity the quantity of the product to add
     */
    public boolean addProductToCart(Integer productId, Integer quantity);

    /**
     * Retrieves all cart items.
     *
     * @return a list of all cart items
     */
    public List<CartItemDTO> findAllCartItems();

    /**
     * Searches for cart items by name.
     *
     * @param name the name of the cart item to search for
     * @return a list of cart items matching the search criteria
     */
    public List<CartItemDTO> SearchCartItemByName(String name);

    /**
     * Finds a cart item by its ID.
     *
     * @param id the ID of the cart item
     * @return the cart item with the given ID
     */
    public CartItemDTO findCartItemById(Integer id);

    /**
     * Deletes a cart item by its ID.
     *
     * @param id the ID of the cart item to delete
     * @return true if the cart item was deleted, false otherwise
     */
    public boolean deleteCartItemById(Integer id);

    /**
     * Deletes multiple cart items by their IDs.
     *
     * @param cartItemIds the list of IDs of the cart items to delete
     * @return true if the cart items were deleted, false otherwise
     */
    public boolean deleteCartItemsByIds(List<Integer> cartItemIds);

    /**
     * Saves a cart item.
     *
     * @param cartItem the cart item to save
     * @return true if the cart item was saved, false otherwise
     */
    public boolean saveCartItem(CartItem cartItem);
}