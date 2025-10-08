package sg.nus.iss.shoppingcart.interfacemethods;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import sg.nus.iss.shoppingcart.model.Product;
import java.io.IOException;
import java.util.List;

public interface ProductInterface {
    List<Product> getAllProducts();

    Product createProduct(Product product);

    Product createProduct(Product product, MultipartFile image) throws IOException;

    Product getProductById(Integer id);

    Product findProductById(Integer id);

    List<Product> searchProductByName(String name);

    List<Product> searchProductByCategory(String category);

    void deleteProduct(Integer id);

    void updateProduct(Product product);

    List<Product> searchProductsByFilters(String keyword, String category, double minPrice, double maxPrice);

    Page<Product> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

}