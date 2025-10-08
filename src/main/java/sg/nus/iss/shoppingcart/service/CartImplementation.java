package sg.nus.iss.shoppingcart.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import sg.nus.iss.shoppingcart.interfacemethods.CartInterface;
import sg.nus.iss.shoppingcart.model.Cart;
import sg.nus.iss.shoppingcart.model.CartItem;
import sg.nus.iss.shoppingcart.model.DTO.CartItemDTO;
import sg.nus.iss.shoppingcart.model.Product;
import sg.nus.iss.shoppingcart.model.User;
import sg.nus.iss.shoppingcart.repository.CartItemRepository;
import sg.nus.iss.shoppingcart.repository.CartRepository;
import sg.nus.iss.shoppingcart.repository.ProductRepository;
import sg.nus.iss.shoppingcart.repository.UserRepository;
import sg.nus.iss.shoppingcart.service.Mapper.CartItemMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class CartImplementation implements CartInterface {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public CartImplementation(CartItemRepository cartItemRepository, 
                             ProductRepository productRepository,
                             CartRepository cartRepository,
                             UserRepository userRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
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
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public List<Product> SearchProductByName(String name) {
        try {
            return productRepository.SearchAvailableProductByName(name);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public Product findProductById(Integer id) {
        try {
            return productRepository.findById(id).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @Transactional
    public boolean addProductToCart(Integer productId, Integer quantity) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return false;
            }
            
            String username = authentication.getName();
            User user = userRepository.findByUsername(username);
            if (user == null) {
                return false;
            }

            Cart cart = cartRepository.findByUser(user);
            if (cart == null) {
                cart = new Cart();
                cart.setUser(user);
                cartRepository.save(cart);
            }

            Product product = findProductById(productId);
            if (product == null || quantity <= 0) {
                return false;
            }

            CartItem existingItem = cartItemRepository.findByCartAndProduct(cart, product);
            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                existingItem.setsubTotalPrice(product.getPrice() * existingItem.getQuantity());
                cartItemRepository.save(existingItem);
            } else {
                CartItem cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);
                cartItem.setCart(cart);
                cartItem.setsubTotalPrice(product.getPrice() * quantity);
                cartItemRepository.save(cartItem);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public List<CartItemDTO> findAllCartItems() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return new ArrayList<>();
            }
            
            String username = authentication.getName();
            User user = userRepository.findByUsername(username);
            if (user == null) {
                return new ArrayList<>();
            }
            
            Cart cart = cartRepository.findByUser(user);
            if (cart == null) {
                return new ArrayList<>();
            }
            
            return cartItemRepository.findByCart(cart)
                    .stream()
                    .map(CartItemMapper::toCartItemDTO)
                    .toList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public List<CartItemDTO> SearchCartItemByName(String name) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return new ArrayList<>();
            }
            
            String username = authentication.getName();
            User user = userRepository.findByUsername(username);
            if (user == null) {
                return new ArrayList<>();
            }
            
            Cart cart = cartRepository.findByUser(user);
            if (cart == null) {
                return new ArrayList<>();
            }
            
            return cartItemRepository.SearchCartItemByName(name)
                    .stream()
                    .filter(item -> item.getCart().equals(cart))
                    .map(CartItemMapper::toCartItemDTO)
                    .toList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public CartItemDTO findCartItemById(Integer id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return null;
            }
            
            String username = authentication.getName();
            User user = userRepository.findByUsername(username);
            if (user == null) {
                return null;
            }
            
            CartItem cartItem = cartItemRepository.findById(id).orElse(null);
            if (cartItem == null || !cartItem.getCart().getUser().equals(user)) {
                return null;
            }
            
            return CartItemMapper.toCartItemDTO(cartItem);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @Transactional
    public boolean deleteCartItemById(Integer id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return false;
            }
            
            String username = authentication.getName();
            User user = userRepository.findByUsername(username);
            if (user == null) {
                return false;
            }
            
            CartItem cartItem = cartItemRepository.findById(id).orElse(null);
            if (cartItem == null || !cartItem.getCart().getUser().equals(user)) {
                return false;
            }
            
            cartItemRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean deleteCartItemsByIds(List<Integer> cartItemIds) {
        if (cartItemIds == null || cartItemIds.isEmpty()) {
            return false;
        }
        
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return false;
            }
            
            String username = authentication.getName();
            User user = userRepository.findByUsername(username);
            if (user == null) {
                return false;
            }
            
            Cart cart = cartRepository.findByUser(user);
            if (cart == null) {
                return false;
            }
            
            boolean allDeleted = true;
            for (Integer id : cartItemIds) {
                CartItem cartItem = cartItemRepository.findById(id).orElse(null);
                if (cartItem != null && cartItem.getCart().equals(cart)) {
                    cartItemRepository.deleteById(id);
                } else {
                    allDeleted = false;
                }
            }
            return allDeleted;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean saveCartItem(CartItem cartItem) {
        try {
            if (cartItem == null) {
                return false;
            }
            
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return false;
            }
            
            String username = authentication.getName();
            User user = userRepository.findByUsername(username);
            if (user == null) {
                return false;
            }
            
            if (cartItem.getCart() == null || !cartItem.getCart().getUser().equals(user)) {
                return false;
            }
            
            cartItemRepository.save(cartItem);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}