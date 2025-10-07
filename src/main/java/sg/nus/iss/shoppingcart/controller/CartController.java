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
/**
 * @ClassName CartController
 * @Description Controller for handling shopping cart related operations.
 * @Author YAO YIYANG
 * @StudentID A0294873L
 * @Date 2024/10/3
 * @Version 1.0
 */
@RestController
public class CartController {

    private final CartInterface CartService;
    private final QuantityValidator quantityValidator;

    /**
     * Constructor for CartController.
     *
     * @param cartServiceImpl the implementation of CartInterface
     * @param quantityValidator the validator for quantity
     */
    public CartController(CartImplementation cartServiceImpl, QuantityValidator quantityValidator) {
        this.CartService = cartServiceImpl;
        this.quantityValidator = quantityValidator;
    }

    /**
     * Initializes the data binder with custom validators.
     *
     * @param binder the WebDataBinder
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(quantityValidator);
    }

    /**
     * Displays a list of all products.
     *
     * @return a ResponseEntity containing the list of products or a NOT_FOUND status
     */
    @GetMapping("/Product")
    public ResponseEntity<List<Product>> getProductSearchPage() {
        try {
            return CartService.findAllProducts().isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(CartService.findAllProducts());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Performs a fuzzy search for products by name or category.
     *
     * @param k the keyword to search for
     * @return a ResponseEntity containing the list of products or a NOT_FOUND status
     */
    @GetMapping(value="/Product/searching")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam("keyword") String k) {
        try {
            List<Product> result = CartService.SearchProductByName(k);
            return result.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Shows product details.
     *
     * @param id the ID of the product
     * @return a ResponseEntity containing the product or a NOT_FOUND status
     */
    @GetMapping(value ="/Product/details/{id}")
    public ResponseEntity<Product> displayProduct(@PathVariable("id") Integer id) {
        try {
            return CartService.findProductById(id) != null ? ResponseEntity.ok(CartService.findProductById(id)) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Adds a product to the cart.
     *
     * @param productId the ID of the product
     * @param quantity the quantity of the product
     * @return a ResponseEntity containing a success message
     */
    @PostMapping(value = "/Product/addCartItem")
    public ResponseEntity<String> addProductToCart(@RequestParam("productId") Integer productId, @RequestParam("quantity") Integer quantity) {
        try {
            CartService.addProductToCart(productId, quantity);
            return ResponseEntity.ok("Product :" + productId + " added to cart successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to add product to cart: " + e.getMessage());
        }
    }

    /**
     * Displays all shopping cart items.
     *
     * @return a ResponseEntity containing the list of cart items or a NOT_FOUND status
     */
    @PostMapping("/CartItem")
    public ResponseEntity<List<CartItemDTO>> getCartItemSearchPage() {
        try {
            return CartService.findAllCartItems().isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(CartService.findAllCartItems());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Performs a fuzzy search for cart items by name or category.
     *
     * @param k the keyword to search for
     * @return a ResponseEntity containing the list of cart items or a NOT_FOUND status
     */
    @GetMapping(value="/CartItem/searching")
    public ResponseEntity<List<CartItemDTO>> searchCartItem(@RequestParam("keyword") String k) {
        try {
            List<CartItemDTO> result = CartService.SearchCartItemByName(k);
            return result.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Shows cart item details.
     *
     * @param id the ID of the cart item
     * @return a ResponseEntity containing the cart item or a NOT_FOUND status
     */
    @GetMapping(value ="/CartItem/details/{id}")
    public ResponseEntity<CartItemDTO> displayCartItem(@PathVariable("id") Integer id) {
        try {
            return CartService.findCartItemById(id) != null ? ResponseEntity.ok(CartService.findCartItemById(id)) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Deletes a cart item.
     *
     * @param id the ID of the cart item
     * @return a ResponseEntity containing a success or error message
     */
    @DeleteMapping(value = "/CartItem/delete/{id}")
    public ResponseEntity<String> deleteCartItem(@PathVariable("id") Integer id) {
        try {
            boolean isDeleted = CartService.deleteCartItemById(id);
            return isDeleted ? ResponseEntity.ok("CartItem :" + id + " deleted successfully") : ResponseEntity.status(HttpStatus.NOT_FOUND).body("CartItem not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to delete CartItem: " + e.getMessage());
        }
    }

    /**
     * Batch deletes shopping cart items.
     *
     * @param cartItemIds the list of cart item IDs
     * @return a ResponseEntity containing a success or error message
     */
    @PostMapping("/CartItem/deleteBatch")
    public ResponseEntity<String> deleteCartItems(@RequestParam("cartItemIds") List<Integer> cartItemIds) {
        try {
            boolean allDeleted = CartService.deleteCartItemsByIds(cartItemIds);
            return allDeleted ? ResponseEntity.ok("CartItems deleted successfully") : ResponseEntity.status(HttpStatus.NOT_FOUND).body("One or more CartItems not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to delete CartItems: " + e.getMessage());
        }
    }

    /**
     * Edits the quantity of a cart item and saves it.
     *
     * @param cartItem the cart item to update
     * @param result the binding result for validation
     * @return a ResponseEntity containing a success or error message
     */
    @PostMapping("/CartItem/updateQuantity")
    public ResponseEntity<String> updateCartItemQuantity(@Valid @RequestBody CartItem cartItem, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid quantity");
        }
        try {
            return CartService.saveCartItem(cartItem) ? ResponseEntity.ok("CartItem updated successfully") : ResponseEntity.status(HttpStatus.NOT_FOUND).body("CartItem not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to update CartItem: " + e.getMessage());
        }
    }
}
