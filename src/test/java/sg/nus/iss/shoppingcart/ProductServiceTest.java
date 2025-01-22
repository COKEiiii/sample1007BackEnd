package sg.nus.iss.shoppingcart;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sg.nus.iss.shoppingcart.enums.ProductStatus;
import sg.nus.iss.shoppingcart.model.Category;
import sg.nus.iss.shoppingcart.model.Product;
import static org.junit.jupiter.api.Assertions.*;  // Import JUnit 5 assertions


import sg.nus.iss.shoppingcart.repository.ProductRepository;
import sg.nus.iss.shoppingcart.service.CategoryImplementation;
import sg.nus.iss.shoppingcart.service.ProductImplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ProductServiceTest {

    @Autowired //// Autowire this to test product service
    private ProductRepository productRepository; // Inject the ProductRepository for use in the tests

    @Autowired
    private CategoryImplementation categoryImplementation;

    @Autowired
    private ProductImplementation productImplementation; //Inject the ProductImplementation (Service layer)

    @Test
        public void testAddProduct() {
            //Arrange: Prepare data
        Product product = new Product("Test Product", "This is a test description", 10.0, 100, ProductStatus.AVAILABLE, "http://example.com/image.jpg");
        List<Category> temp = categoryImplementation.getAllCategorys();
        Category category = temp.get(1);
        product.setCategory(category);

        //  Act: Call the service to add the product
        Product addProduct = productImplementation.createProduct(product); // Directly use the returned product

        // Assert: Verify that the product is saved successfully
        assertNotNull(addProduct); // Assert that the saved product is not null
        assertEquals("Test Product", addProduct.getName()); // verify the name
        assertEquals("This is a test description", addProduct.getDescription()); //// verify the description
    }

    @Test
    public void testUpdateProduct() {
        // Arrange: Create and save a new product first
        Product product = new Product("Test Product", "Initial description", 10.0, 100, ProductStatus.AVAILABLE, "http://example.com/image.jpg");
        Product savedProduct = productImplementation.createProduct(product);

        // Make changes to the product
        savedProduct.setName("Updated Product");
        savedProduct.setDescription("Updated description");
        savedProduct.setPrice(15.0); // Update price
        savedProduct.setStock(150); // Update stock

        // Act: Call the service to update the product
        productImplementation.updateProduct(savedProduct); // No need to capture the return if it doesn't return

        // Assert: Verify that the changes are saved
        Product updatedProduct = productImplementation.findProductById(savedProduct.getId());

        // Assert the updated fields
        assertNotNull(updatedProduct); // check name update
        assertEquals("Updated Product", updatedProduct.getName());  // Check name update
        assertEquals("Updated description", updatedProduct.getDescription());  // Check description update
        assertEquals(15.0, updatedProduct.getPrice());  // Check price update
        assertEquals(150, updatedProduct.getStock());  // Check stock update
    }


    @Test
    public void testDeleteProduct() {

        //Arrange data: create and save
        Product product = new Product("Test Product", "Description for delete", 10.0, 100, ProductStatus.AVAILABLE, "http://example.com/image.jpg");
        Product deleteProduct = productImplementation.createProduct(product);

        //Act: Call the service to delete the product
        productImplementation.deleteProduct(deleteProduct.getId());

        // Assert: Verify that the product is delete successfully
        Optional<Product> deletedProduct = productRepository.findById(deleteProduct.getId());
       assertTrue(deletedProduct.isEmpty()); // Assert that the product no longer exists in the database (since deleteProduct doesn't return anything).

    }

    @Test
    public void testSearchProducts() {

        //Arrange: Prepare data
        Product product = new Product("Searchable Product", "Category1", 20.0, 50, ProductStatus.AVAILABLE, "http://example.com/image.jpg");
        productImplementation.createProduct(product);

        //Act: Call the service to search the product
        String keyword = "Searchable";
        String category = "Category1";
        double minPrice = 10.0;
        double maxPrice = 50.0;

        List<Product> result = productImplementation.searchProductsByFilters(keyword, category, minPrice, maxPrice);

        // Assert: Verify that the product is searched successfully
        assertNotNull(result);// Check that result is not null
        assertFalse(result.isEmpty()); //ensure that result is not empty
        assertEquals(1, result.size()); // Verify that exactly one product is returned
        assertEquals("Searchable Product", result.get(0).getName()); //// Check that the product name matches

    }



}
