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

    @Test
    public void testGetProductSearchPage_NoProducts() throws Exception {
        when(cartService.findAllProducts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/Product"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetProductSearchPage_InternalServerError() throws Exception {
        when(cartService.findAllProducts()).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/Product"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testSearchProduct_Success() throws Exception {
        Product product1 = new Product("Product1", "Description1", 10.0, 100, ProductStatus.AVAILABLE, "image1");
        when(cartService.SearchProductByName("Product1")).thenReturn(Arrays.asList(product1));

        mockMvc.perform(get("/Product/searching?keyword=Product1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product1"));
    }

    @Test
    public void testSearchProduct_NoResults() throws Exception {
        when(cartService.SearchProductByName("NonExistent")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/Product/searching?keyword=NonExistent"))
                .andExpect(status().isNotFound());
    }

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

    @Test
    public void testAddProductToCart_InternalServerError() throws Exception {
        doThrow(new RuntimeException("Add to cart error")).when(cartService).addProductToCart(anyInt(), anyInt());

        mockMvc.perform(post("/Product/addCartItem")
                .param("productId", "1")
                .param("quantity", "1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Unable to add product to cart: Add to cart error"));
    }

    @Test
    public void testDeleteCartItem_Success() throws Exception {
        when(cartService.deleteCartItemById(1)).thenReturn(true);

        mockMvc.perform(delete("/CartItem/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("CartItem :1 deleted successfully"));
    }

    @Test
    public void testDeleteCartItem_NotFound() throws Exception {
        when(cartService.deleteCartItemById(1)).thenReturn(false);

        mockMvc.perform(delete("/CartItem/delete/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("CartItem not found"));
    }

    @Test
    public void testUpdateCartItemQuantity_Success() throws Exception {
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(2);
        when(cartService.saveCartItem(any(CartItem.class))).thenReturn(true);

        mockMvc.perform(post("/CartItem/updateQuantity")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1, \"quantity\":2}"))
                .andExpect(status().isOk())
                .andExpect(content().string("CartItem updated successfully"));
    }

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
