package sg.nus.iss.shoppingcart;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sg.nus.iss.shoppingcart.enums.ProductStatus;
import sg.nus.iss.shoppingcart.model.Category;
import sg.nus.iss.shoppingcart.model.Product;
import static org.junit.jupiter.api.Assertions.*;

import sg.nus.iss.shoppingcart.repository.ProductRepository;
import sg.nus.iss.shoppingcart.service.CategoryImplementation;
import sg.nus.iss.shoppingcart.service.ProductImplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryImplementation categoryImplementation;

    @Autowired
    private ProductImplementation productImplementation;

    @Test
    public void testAddProduct() {
        Product product = new Product("Test Product", "This is a test description", 10.0, 100, ProductStatus.AVAILABLE,
                "http://example.com/image.jpg ");
        List<Category> temp = categoryImplementation.getAllCategorys();
        Category category = temp.get(1);
        product.setCategory(category);
        Product addProduct = productImplementation.createProduct(product);
        assertNotNull(addProduct);
        assertEquals("Test Product", addProduct.getName());
        assertEquals("This is a test description", addProduct.getDescription());
    }

    @Test
    public void testUpdateProduct() {
        Product product = new Product("Test Product", "Initial description", 10.0, 100, ProductStatus.AVAILABLE,
                "http://example.com/image.jpg ");
        Product savedProduct = productImplementation.createProduct(product);
        savedProduct.setName("Updated Product");
        savedProduct.setDescription("Updated description");
        savedProduct.setPrice(15.0);
        savedProduct.setStock(150);
        productImplementation.updateProduct(savedProduct);
        Product updatedProduct = productImplementation.findProductById(savedProduct.getId());
        assertNotNull(updatedProduct);
        assertEquals("Updated Product", updatedProduct.getName());
        assertEquals("Updated description", updatedProduct.getDescription());
        assertEquals(15.0, updatedProduct.getPrice());
        assertEquals(150, updatedProduct.getStock());
    }

    @Test
    public void testDeleteProduct() {
        Product product = new Product("Test Product", "Description for delete", 10.0, 100, ProductStatus.AVAILABLE,
                "http://example.com/image.jpg ");
        Product deleteProduct = productImplementation.createProduct(product);
        productImplementation.deleteProduct(deleteProduct.getId());
        Optional<Product> deletedProduct = productRepository.findById(deleteProduct.getId());
        assertTrue(deletedProduct.isEmpty());
    }

    @Test
    public void testSearchProducts() {
        Product product = new Product("Searchable Product", "Category1", 20.0, 50, ProductStatus.AVAILABLE,
                "http://example.com/image.jpg ");
        productImplementation.createProduct(product);
        String keyword = "Searchable";
        String category = "Category1";
        double minPrice = 10.0;
        double maxPrice = 50.0;
        List<Product> result = productImplementation.searchProductsByFilters(keyword, category, minPrice, maxPrice);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Searchable Product", result.get(0).getName());
    }
}