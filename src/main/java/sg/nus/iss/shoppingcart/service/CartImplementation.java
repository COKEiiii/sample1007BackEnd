package sg.nus.iss.shoppingcart.service;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import sg.nus.iss.shoppingcart.interfacemethods.CartInterface;
import sg.nus.iss.shoppingcart.model.Cart;
import sg.nus.iss.shoppingcart.model.CartItem;
import sg.nus.iss.shoppingcart.model.DTO.CartItemDTO;
import sg.nus.iss.shoppingcart.model.Product;
import sg.nus.iss.shoppingcart.repository.CartItemRepository;
import sg.nus.iss.shoppingcart.repository.ProductRepository;
import sg.nus.iss.shoppingcart.service.Mapper.CartItemMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName CartImplementation
 * @Description Implementation of the CartInterface, providing services related to the shopping cart.
 * @Author YAO YIYANG
 * @StudentID A0294873L
 * @Date 2024/10/3
 * @Version 1.0
 */
@Service
@Transactional
public class CartImplementation implements CartInterface {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    /**
     * Constructor for CartImplementation.
     *
     * @param cartItemRepository the repository for cart items
     * @param productRepository the repository for products
     */
    public CartImplementation(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Cart addProductToCart(Long userId, Long productId, Long quantity) {
        return null;
    }

    @Override
    @Transactional
    public List<Product> findAllProducts() {
        try {
            return productRepository.findAll();
        } catch (Exception e) {
            return new ArrayList<>(); // Return an empty list
        }
    }

    @Override
    @Transactional
    public List<Product> SearchProductByName(String name) {
        try {
            return productRepository.SearchAvailableProductByName(name);
        } catch (Exception e) {
            return new ArrayList<>(); // Return an empty list
        }
    }

    @Override
    @Transactional
    public Product findProductById(Integer id) {
        try {
            return productRepository.findById(id).orElse(null);
        } catch (Exception e) {
            return null; // Return null to indicate the product was not found
        }
    }

    @Override
    @Transactional
    public boolean addProductToCart(Integer productId, Integer quantity) {
        try {
            Product product = findProductById(productId);
            if (product == null || quantity <= 0) {
                return false; // Handle error for invalid product or quantity
            }
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
            return true; // Return true to indicate success
        } catch (Exception e) {
            return false; // Indicate failure
        }
    }

    @Override
    @Transactional
    public List<CartItemDTO> findAllCartItems() {
        try {
            return cartItemRepository.findAll().stream().map(CartItemMapper::toCartItemDTO).toList();
        } catch (Exception e) {
            return new ArrayList<>(); // Return an empty list
        }
    }

    @Override
    @Transactional
    public List<CartItemDTO> SearchCartItemByName(String name) {
        try {
            return cartItemRepository.SearchCartItemByName(name).stream().map(CartItemMapper::toCartItemDTO).toList();
        } catch (Exception e) {
            return new ArrayList<>(); // Return an empty list
        }
    }

    @Override
    @Transactional
    public CartItemDTO findCartItemById(Integer id) {
        try {
            return CartItemMapper.toCartItemDTO(Objects.requireNonNull(cartItemRepository.findById(id).orElse(null)));
        } catch (Exception e) {
            return null; // Return null to indicate the cart item was not found
        }
    }

    @Override
    @Transactional
    public boolean deleteCartItemById(Integer id) {
        try {
            if (id == null) {
                return false; // Return false for null ID
            }
            if (cartItemRepository.existsById(id)) {
                cartItemRepository.deleteById(id);
                return true; // Indicate success
            }
            return false; // Indicate that the ID does not exist
        } catch (Exception e) {
            return false; // Indicate failure
        }
    }

    @Override
    @Transactional
    public boolean deleteCartItemsByIds(List<Integer> cartItemIds) {
        if (cartItemIds == null || cartItemIds.isEmpty()) {
            return false; // Handle empty or null list
        }
        boolean allDeleted = true;
        for (Integer id : cartItemIds) {
            if (!deleteCartItemById(id)) {
                allDeleted = false;
            }
        }
        return allDeleted;
    }

    @Override
    @Transactional
    public boolean saveCartItem(CartItem cartItem) {
        try {
            if (cartItem == null) {
                return false; // Handle error for null cart item
            }
            cartItemRepository.save(cartItem);
            return true; // Indicate success
        } catch (Exception e) {
            return false; // Indicate failure
        }
    }
}
