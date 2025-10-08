package sg.nus.iss.shoppingcart.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sg.nus.iss.shoppingcart.interfacemethods.ProductInterface;
import sg.nus.iss.shoppingcart.model.Product;
import sg.nus.iss.shoppingcart.repository.ProductRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
@Transactional
public class ProductImplementation implements ProductInterface {
    private final ProductRepository productRepository;

    public ProductImplementation(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product getProductById(Integer id) {
        return findProductById(id);
    }

    @Override
    public Product findProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> searchProductByName(String name) {
        return productRepository.SearchProductByName(name);
    }

    @Override
    public List<Product> searchProductByCategory(String category) {
        return productRepository.SearchProductByCategory(category);
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product createProduct(Product product, MultipartFile image) {

        String folderPath = "src/main/resources/static/images/";

        String imageFolderPath = folderPath + System.currentTimeMillis() + "/";

        File folder = new File(imageFolderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try {
            String imagePath = imageFolderPath + image.getOriginalFilename();
            Files.copy(image.getInputStream(), Paths.get(imagePath));
            product.setImageUrl("/images/" + System.currentTimeMillis() + "/" + image.getOriginalFilename());
        } catch (IOException e) {
            throw new RuntimeException("Can't save image", e);
        }

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public void updateProduct(Product product) {
        Product existingProduct = findProductById(product.getId());

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        existingProduct.setStatus(product.getStatus());
        existingProduct.setCreatedAt(product.getCreatedAt());

        productRepository.save(existingProduct);
    }

    @Override
    public List<Product> searchProductsByFilters(String keyword, String category, double minPrice, double maxPrice) {
        return productRepository.SearchProductsByFilters(keyword, category, minPrice, maxPrice);
    }

    @Override
    public Page<Product> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = Sort.by(sortField);
        if ("desc".equalsIgnoreCase(sortDirection)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return productRepository.findAll(pageable);
    }
}