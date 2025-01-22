package sg.nus.iss.shoppingcart;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import sg.nus.iss.shoppingcart.controller.CartController;
import sg.nus.iss.shoppingcart.enums.ProductStatus;
import sg.nus.iss.shoppingcart.interfacemethods.CartInterface;
import sg.nus.iss.shoppingcart.model.CartItem;
import sg.nus.iss.shoppingcart.model.Product;
import sg.nus.iss.shoppingcart.service.CartImplementation;

public class CartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartImplementation cartService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    // Test successful path for getting all products
    @Test
    public void testGetProductSearchPage_Success() throws Exception {
        Product product1 = new Product("Product1", "Description1", 10.0, 100, ProductStatus.AVAILABLE, "image1");
        Product product2 = new Product("Product2", "Description2", 20.0, 200, ProductStatus.AVAILABLE, "image2");
        when(cartService.findAllProducts()).thenReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/Product"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Product1"))
                .andExpect(jsonPath("$[1].name").value("Product2"));
    }

    // Test case when there are no products
    @Test
    public void testGetProductSearchPage_NoProducts() throws Exception {
        when(cartService.findAllProducts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/Product"))
                .andExpect(status().isNotFound());
    }

    // Test case when there is an exception while getting products
    @Test
    public void testGetProductSearchPage_InternalServerError() throws Exception {
        when(cartService.findAllProducts()).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/Product"))
                .andExpect(status().isInternalServerError());
    }

    // Test successful path for searching products
    @Test
    public void testSearchProduct_Success() throws Exception {
        Product product1 = new Product("Product1", "Description1", 10.0, 100, ProductStatus.AVAILABLE, "image1");
        when(cartService.SearchProductByName("Product1")).thenReturn(Arrays.asList(product1));

        mockMvc.perform(get("/Product/searching?keyword=Product1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product1"));
    }

    // Test case when there are no matching products
    @Test
    public void testSearchProduct_NoResults() throws Exception {
        when(cartService.SearchProductByName("NonExistent")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/Product/searching?keyword=NonExistent"))
                .andExpect(status().isNotFound());
    }

    // Test successful path for adding a product to the cart
    @Test
    public void testAddProductToCart_Success() throws Exception {
        // Use when(...).thenReturn(...) to simulate return value
        when(cartService.addProductToCart(1, 2)).thenReturn(true);

        mockMvc.perform(post("/Product/addCartItem")
                        .param("productId", "1")
                        .param("quantity", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product :1 added to cart successfully"));
    }

    // Test case when there is an exception while adding a product to the cart
    @Test
    public void testAddProductToCart_InternalServerError() throws Exception {
        doThrow(new RuntimeException("Add to cart error")).when(cartService).addProductToCart(anyInt(), anyInt());

        mockMvc.perform(post("/Product/addCartItem")
                        .param("productId", "1")
                        .param("quantity", "1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Unable to add product to cart: Add to cart error"));
    }

    // Test successful path for deleting a cart item
    @Test
    public void testDeleteCartItem_Success() throws Exception {
        when(cartService.deleteCartItemById(1)).thenReturn(true);

        mockMvc.perform(delete("/CartItem/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("CartItem :1 deleted successfully"));
    }

    // Test case for deleting a non-existent cart item
    @Test
    public void testDeleteCartItem_NotFound() throws Exception {
        when(cartService.deleteCartItemById(1)).thenReturn(false);

        mockMvc.perform(delete("/CartItem/delete/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("CartItem not found"));
    }

    // Test successful path for updating cart item quantity
    @Test
    public void testUpdateCartItemQuantity_Success() throws Exception {
        // Simulate a valid CartItem
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(2);

        // Set the behavior of cartService
        when(cartService.saveCartItem(any(CartItem.class))).thenReturn(true);

        // Send POST request
        mockMvc.perform(post("/CartItem/updateQuantity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1, \"quantity\":2}")) // Ensure this request matches the controller
                .andExpect(status().isOk()) // Expecting response status 200
                .andExpect(content().string("CartItem updated successfully")); // Expecting correct content
    }

    // Test case for updating a non-existent cart item
    @Test
    public void testUpdateCartItemQuantity_NotFound() throws Exception {
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(2);
        when(cartService.saveCartItem(cartItem)).thenReturn(false);

        mockMvc.perform(post("/CartItem/updateQuantity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1, \"quantity\":2}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid quantity"));
    }

}
