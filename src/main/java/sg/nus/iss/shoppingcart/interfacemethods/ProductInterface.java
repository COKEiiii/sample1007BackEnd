package sg.nus.iss.shoppingcart.interfacemethods;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import sg.nus.iss.shoppingcart.model.Product;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName ProductInterface
 * @Description Interface for product operations, providing methods to manage products.
 * @Author [Your Name]
 * @Date [Current Date]
 * @Version 1.0
 */
public interface ProductInterface {

    List<Product> getAllProducts();

    Product createProduct(String name, Double price, String description, Integer stock, Long categoryId, MultipartFile image) throws IOException;

    Product getProductById(Long id);
}