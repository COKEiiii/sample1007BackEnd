package sg.nus.iss.shoppingcart.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import sg.nus.iss.shoppingcart.interfacemethods.CartInterface;
import sg.nus.iss.shoppingcart.model.CartItem;
import sg.nus.iss.shoppingcart.model.DTO.CartItemDTO;
import sg.nus.iss.shoppingcart.model.Product;
import sg.nus.iss.shoppingcart.service.CartImplementation;
import sg.nus.iss.shoppingcart.validator.QuantityValidator;

import jakarta.servlet.http.HttpSession;

@RestController
public class CartController {

    private final CartInterface CartService;
    private final QuantityValidator quantityValidator;

    public CartController(CartImplementation cartServiceImpl, QuantityValidator quantityValidator) {
        this.CartService = cartServiceImpl;
        this.quantityValidator = quantityValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(quantityValidator);
    }

    @GetMapping("/Product")
    public ResponseEntity<List<Product>> getProductSearchPage() {
        try {
            return CartService.findAllProducts().isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
                    : ResponseEntity.ok(CartService.findAllProducts());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(value = "/Product/searching")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam("keyword") String k) {
        try {
            List<Product> result = CartService.SearchProductByName(k);
            return result.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
                    : ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(value = "/Product/details/{id}")
    public ResponseEntity<Product> displayProduct(@PathVariable("id") Integer id) {
        try {
            return CartService.findProductById(id) != null ? ResponseEntity.ok(CartService.findProductById(id))
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(value = "/Product/addCartItem")
    public ResponseEntity<String> addProductToCart(@RequestParam("productId") Integer productId,
                                                   @RequestParam("quantity") Integer quantity) {
        try {
            CartService.addProductToCart(productId, quantity);
            return ResponseEntity.ok("Product :" + productId + " added to cart successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to add product to cart: " + e.getMessage());
        }
    }

    @PostMapping("/CartItem")
    public ResponseEntity<List<CartItemDTO>> getCartItemSearchPage() {
        try {
            return CartService.findAllCartItems().isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
                    : ResponseEntity.ok(CartService.findAllCartItems());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(value = "/CartItem/searching")
    public ResponseEntity<List<CartItemDTO>> searchCartItem(@RequestParam("keyword") String k) {
        try {
            List<CartItemDTO> result = CartService.SearchCartItemByName(k);
            return result.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
                    : ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(value = "/CartItem/details/{id}")
    public ResponseEntity<CartItemDTO> displayCartItem(@PathVariable("id") Integer id) {
        try {
            return CartService.findCartItemById(id) != null ? ResponseEntity.ok(CartService.findCartItemById(id))
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping(value = "/CartItem/delete/{id}")
    public ResponseEntity<String> deleteCartItem(@PathVariable("id") Integer id) {
        try {
            boolean isDeleted = CartService.deleteCartItemById(id);
            return isDeleted ? ResponseEntity.ok("CartItem :" + id + " deleted successfully")
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body("CartItem not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to delete CartItem: " + e.getMessage());
        }
    }

    @PostMapping("/CartItem/deleteBatch")
    public ResponseEntity<String> deleteCartItems(@RequestParam("cartItemIds") List<Integer> cartItemIds) {
        try {
            boolean allDeleted = CartService.deleteCartItemsByIds(cartItemIds);
            return allDeleted ? ResponseEntity.ok("CartItems deleted successfully")
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body("One or more CartItems not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to delete CartItems: " + e.getMessage());
        }
    }

    @PostMapping("/CartItem/updateQuantity")
    public ResponseEntity<String> updateCartItemQuantity(@Valid @RequestBody CartItem cartItem, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid quantity");
        }
        try {
            return CartService.saveCartItem(cartItem) ? ResponseEntity.ok("CartItem updated successfully")
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body("CartItem not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to update CartItem: " + e.getMessage());
        }
    }
}




/*package sg.nus.iss.shoppingcart.controller;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import sg.nus.iss.shoppingcart.interfacemethods.CartInterface;
import sg.nus.iss.shoppingcart.model.CartItem;
import sg.nus.iss.shoppingcart.model.DTO.CartItemDTO;
import sg.nus.iss.shoppingcart.model.Product;
import sg.nus.iss.shoppingcart.service.CartImplementation;
import sg.nus.iss.shoppingcart.validator.QuantityValidator;

@RestController
public class CartController {
    // 定义日志对象（类级别）
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    private final CartInterface cartService;
    private final QuantityValidator quantityValidator;

    // 构造方法参数名修正（遵循驼峰命名）
    public CartController(CartImplementation cartServiceImpl, QuantityValidator quantityValidator) {
        this.cartService = cartServiceImpl;
        this.quantityValidator = quantityValidator;
        logger.info("CartController initialized with CartService: {}", cartServiceImpl.getClass().getSimpleName());
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(quantityValidator);
        logger.debug("Initialized QuantityValidator for request parameter validation");
    }

    @GetMapping("/Product")
    public ResponseEntity<List<Product>> getProductSearchPage() {
        logger.info("Received request to fetch all products");
        try {
            List<Product> products = cartService.findAllProducts();
            if (products.isEmpty()) {
                logger.warn("No products found in database");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            logger.info("Successfully fetched {} products", products.size());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            logger.error("Failed to fetch products", e); // 记录异常堆栈
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(value = "/Product/searching")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam("keyword") String keyword) {
        logger.info("Received request to search products with keyword: {}", keyword);
        try {
            List<Product> result = cartService.SearchProductByName(keyword);
            if (result.isEmpty()) {
                logger.warn("No products found for keyword: {}", keyword);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            logger.info("Found {} products matching keyword: {}", result.size(), keyword);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Failed to search products with keyword: {}", keyword, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(value = "/Product/details/{id}")
    public ResponseEntity<Product> displayProduct(@PathVariable("id") Integer productId) {
        logger.info("Received request to fetch product details for ID: {}", productId);
        try {
            Product product = cartService.findProductById(productId);
            if (product == null) {
                logger.warn("Product not found with ID: {}", productId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            logger.info("Successfully fetched product details for ID: {}", productId);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            logger.error("Failed to fetch product details for ID: {}", productId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/Product/addCartItem")
    public ResponseEntity<Map<String, Object>> addProductToCart(
            @RequestParam("productId") Integer productId,
            @RequestParam("quantity") Integer quantity) {

        // 记录请求参数（关键业务接口重点日志）
        logger.info("Received request to add product to cart - productId: {}, quantity: {}", productId, quantity);

        try {
            // 验证参数合法性（提前拦截无效参数）
            if (productId == null || productId <= 0) {
                String errorMsg = "Invalid productId: " + productId;
                logger.warn(errorMsg);
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", errorMsg);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            if (quantity == null || quantity <= 0) {
                String errorMsg = "Invalid quantity: " + quantity;
                logger.warn(errorMsg);
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", errorMsg);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            // 调用服务层（记录服务调用结果）
            boolean isSuccess = cartService.addProductToCart(productId, quantity);
            logger.debug("Service returned isSuccess: {} for productId: {}", isSuccess, productId);

            Map<String, Object> response = new HashMap<>();
            if (isSuccess) {
                response.put("success", true);
                response.put("message", "Product added to cart successfully");
                response.put("productId", productId);
                response.put("quantity", quantity);
                logger.info("Successfully added product to cart - productId: {}, quantity: {}", productId, quantity);
                return ResponseEntity.ok(response);
            } else {
                // 业务失败时，日志记录可能的原因（结合Service层日志排查）
                response.put("success", false);
                response.put("message", "Failed to add product (check service logs for details)");
                response.put("productId", productId);
                logger.warn("Business logic failed to add product to cart - productId: {}, quantity: {}", productId, quantity);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            // 记录系统异常（包含堆栈信息）
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "System error: " + e.getMessage());
            logger.error("System error when adding product to cart - productId: {}, quantity: {}", productId, quantity, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/CartItem")
    public ResponseEntity<List<CartItemDTO>> getCartItemSearchPage() {
        logger.info("Received request to fetch all cart items");
        try {
            List<CartItemDTO> cartItems = cartService.findAllCartItems();
            if (cartItems.isEmpty()) {
                logger.warn("No cart items found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            logger.info("Successfully fetched {} cart items", cartItems.size());
            return ResponseEntity.ok(cartItems);
        } catch (Exception e) {
            logger.error("Failed to fetch cart items", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(value = "/CartItem/searching")
    public ResponseEntity<List<CartItemDTO>> searchCartItem(@RequestParam("keyword") String keyword) {
        logger.info("Received request to search cart items with keyword: {}", keyword);
        try {
            List<CartItemDTO> result = cartService.SearchCartItemByName(keyword);
            if (result.isEmpty()) {
                logger.warn("No cart items found for keyword: {}", keyword);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            logger.info("Found {} cart items matching keyword: {}", result.size(), keyword);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Failed to search cart items with keyword: {}", keyword, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(value = "/CartItem/details/{id}")
    public ResponseEntity<CartItemDTO> displayCartItem(@PathVariable("id") Integer cartItemId) {
        logger.info("Received request to fetch cart item details for ID: {}", cartItemId);
        try {
            CartItemDTO cartItem = cartService.findCartItemById(cartItemId);
            if (cartItem == null) {
                logger.warn("Cart item not found with ID: {}", cartItemId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            logger.info("Successfully fetched cart item details for ID: {}", cartItemId);
            return ResponseEntity.ok(cartItem);
        } catch (Exception e) {
            logger.error("Failed to fetch cart item details for ID: {}", cartItemId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping(value = "/CartItem/delete/{id}")
    public ResponseEntity<String> deleteCartItem(@PathVariable("id") Integer cartItemId) {
        logger.info("Received request to delete cart item with ID: {}", cartItemId);
        try {
            boolean isDeleted = cartService.deleteCartItemById(cartItemId);
            if (isDeleted) {
                logger.info("Successfully deleted cart item with ID: {}", cartItemId);
                return ResponseEntity.ok("CartItem :" + cartItemId + " deleted successfully");
            } else {
                logger.warn("Cart item not found for deletion, ID: {}", cartItemId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CartItem not found");
            }
        } catch (Exception e) {
            logger.error("Failed to delete cart item with ID: {}", cartItemId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to delete CartItem: " + e.getMessage());
        }
    }

    @PostMapping("/CartItem/deleteBatch")
    public ResponseEntity<String> deleteCartItems(@RequestParam("cartItemIds") List<Integer> cartItemIds) {
        logger.info("Received request to delete batch cart items: {}", cartItemIds);
        try {
            boolean allDeleted = cartService.deleteCartItemsByIds(cartItemIds);
            if (allDeleted) {
                logger.info("Successfully deleted batch cart items: {}", cartItemIds);
                return ResponseEntity.ok("CartItems deleted successfully");
            } else {
                logger.warn("One or more cart items not found in batch deletion: {}", cartItemIds);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("One or more CartItems not found");
            }
        } catch (Exception e) {
            logger.error("Failed to delete batch cart items: {}", cartItemIds, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to delete CartItems: " + e.getMessage());
        }
    }

    @PostMapping("/CartItem/updateQuantity")
    public ResponseEntity<String> updateCartItemQuantity(@Valid @RequestBody CartItem cartItem, BindingResult result) {
        logger.info("Received request to update cart item quantity: {}", cartItem);
        if (result.hasErrors()) {
            String errorMsg = "Invalid quantity: " + result.getAllErrors().get(0).getDefaultMessage();
            logger.warn(errorMsg);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }
        try {
            boolean isUpdated = cartService.saveCartItem(cartItem);
            if (isUpdated) {
                logger.info("Successfully updated cart item quantity: {}", cartItem);
                return ResponseEntity.ok("CartItem updated successfully");
            } else {
                logger.warn("Cart item not found for update: {}", cartItem);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CartItem not found");
            }
        } catch (Exception e) {
            logger.error("Failed to update cart item quantity: {}", cartItem, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to update CartItem: " + e.getMessage());
        }
    }
}*/
