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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
@Service
@Transactional
public class CartImplementation implements CartInterface {
    public static final Logger logger = LoggerFactory.getLogger(CartImplementation.class);
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
        // 记录方法开始，包含关键参数
        logger.info("开始执行添加商品到购物车：productId={}, quantity={}", productId, quantity);

        try {
            // 1. 检查认证状态
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            logger.debug("当前认证信息：authentication={}", authentication);

            if (authentication == null || !authentication.isAuthenticated()) {
                logger.warn("添加失败：用户未认证（未登录或登录状态无效）");
                return false;
            }

            // 2. 获取用户名并查询用户
            String username = authentication.getName();
            logger.debug("当前登录用户名：username={}", username);

            User user = userRepository.findByUsername(username);
            if (user == null) {
                logger.warn("添加失败：未找到用户，username={}", username);
                return false;
            }
            logger.debug("查询到用户：userId={}, username={}", user.getId(), user.getUsername());

            // 3. 查询或创建用户购物车
            Cart cart = cartRepository.findByUser(user);
            if (cart == null) {
                logger.debug("用户购物车不存在，为用户创建新购物车：userId={}", user.getId());
                cart = new Cart();
                cart.setUser(user);
                cart = cartRepository.save(cart); // 保存并获取带ID的购物车
                logger.debug("创建新购物车成功：cartId={}", cart.getCartId());
            } else {
                logger.debug("查询到用户已有购物车：cartId={}", cart.getCartId());
            }

            // 4. 检查商品是否存在及数量是否有效
            Product product = productRepository.findById(productId).orElse(null); // 假设用productRepository查询
            if (product == null) {
                logger.warn("添加失败：商品不存在，productId={}", productId);
                return false;
            }
            logger.debug("查询到商品信息：productId={}, 商品名称={}, 单价={}",
                    productId, product.getName(), product.getPrice());

            if (quantity <= 0) {
                logger.warn("添加失败：数量无效（必须大于0），quantity={}", quantity);
                return false;
            }

            // 5. 检查商品库存（补充库存校验逻辑，这是常见失败点）
            if (product.getStock() < quantity) {
                logger.warn("添加失败：库存不足，productId={}, 需求数量={}, 实际库存={}",
                        productId, quantity, product.getStock());
                return false;
            }

            // 6. 处理购物车项（已存在则更新，否则新增）
            CartItem existingItem = cartItemRepository.findByCartAndProduct(cart, product);
            if (existingItem != null) {
                logger.debug("购物车中已存在该商品，更新数量：cartItemId={}, 原数量={}, 新增数量={}",
                        existingItem.getCartItemId(), existingItem.getQuantity(), quantity);
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                existingItem.setsubTotalPrice(product.getPrice() * existingItem.getQuantity());
                cartItemRepository.save(existingItem);
                logger.debug("更新购物车项成功：cartItemId={}, 新数量={}",
                        existingItem.getCartItemId(), existingItem.getQuantity());
            } else {
                logger.debug("购物车中不存在该商品，创建新购物车项：productId={}, 数量={}",
                        productId, quantity);
                CartItem cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);
                cartItem.setCart(cart);
                cartItem.setsubTotalPrice(product.getPrice() * quantity);
                cartItemRepository.save(cartItem);
                logger.debug("创建新购物车项成功：cartItemId={}", cartItem.getCartItemId());
            }

            // 7. 所有步骤成功
            logger.info("添加商品到购物车成功：productId={}, 数量={}, cartId={}",
                    productId, quantity, cart.getCartId());
            return true;

        } catch (Exception e) {
            // 记录异常详情（包含堆栈，关键排查信息）
            logger.error("添加商品到购物车时发生异常：productId={}, quantity={}",
                    productId, quantity, e);
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