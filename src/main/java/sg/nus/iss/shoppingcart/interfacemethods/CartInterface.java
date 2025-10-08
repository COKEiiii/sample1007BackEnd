package sg.nus.iss.shoppingcart.interfacemethods;

import java.util.List;

import sg.nus.iss.shoppingcart.model.Cart;
import sg.nus.iss.shoppingcart.model.CartItem;
import sg.nus.iss.shoppingcart.model.DTO.CartItemDTO;
import sg.nus.iss.shoppingcart.model.Product;

public interface CartInterface {

    Cart addProductToCart(Long userId, Long productId, Long quantity);

    public List<Product> findAllProducts();

    public List<Product> SearchProductByName(String name);

    public Product findProductById(Integer id);

    public boolean addProductToCart(Integer productId, Integer quantity);

    public List<CartItemDTO> findAllCartItems();

    public List<CartItemDTO> SearchCartItemByName(String name);

    public CartItemDTO findCartItemById(Integer id);

    public boolean deleteCartItemById(Integer id);

    public boolean deleteCartItemsByIds(List<Integer> cartItemIds);

    public boolean saveCartItem(CartItem cartItem);
}