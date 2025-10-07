package sg.nus.iss.shoppingcart.interfacemethods;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import sg.nus.iss.shoppingcart.model.Product;
import java.io.IOException;
import java.util.List;

public interface ProductInterface {
    List<Product> getAllProducts();

    Product createProduct(Product product);

    // 修改此方法签名以匹配实现
    Product createProduct(Product product, MultipartFile image) throws IOException;

    // 添加缺失的方法声明
    Product getProductById(Integer id); // 改为Integer类型以匹配实现
    Product findProductById(Integer id);
    List<Product> searchProductByName(String name);
    List<Product> searchProductByCategory(String category);
    void deleteProduct(Integer id);
    void updateProduct(Product product);
    List<Product> searchProductsByFilters(String keyword, String category, double minPrice, double maxPrice);
    Page<Product> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

    // 移除不匹配的方法
    // Product createProduct(String name, Double price, String description, Integer stock, Long categoryId, MultipartFile image) throws IOException;
    // Product getProductById(Long id); // 改为Integer类型
}