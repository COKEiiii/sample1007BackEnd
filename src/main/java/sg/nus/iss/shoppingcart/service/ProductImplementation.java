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

    // 添加getProductById方法以匹配接口
    @Override
    public Product getProductById(Integer id) {
        return findProductById(id);
    }
    /**
     * Finds a product by its ID.
     *
     * @param id the ID of the product
     * @return the product with the specified ID
     * @throws RuntimeException if the product is not found
     */
    @Override
    public Product findProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    /**
     * Retrieves all products.
     *
     * @return a list of all products
     */
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Searches for products by name.
     *
     * @param name the name of the product
     * @return a list of products matching the name
     */
    @Override
    public List<Product> searchProductByName(String name) {
        return productRepository.SearchProductByName(name);
    }

    /**
     * Searches for products by category.
     *
     * @param category the category of the product
     * @return a list of products matching the category
     */
    @Override
    public List<Product> searchProductByCategory(String category) {
        return productRepository.SearchProductByCategory(category);
    }

    /**
     * Creates a new product.
     *
     * @param product the product to create
     * @return the created product
     */
    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Creates a new product.
     *
     * @param product the product to create, including the image of product
     * @return the created product
     */
    @Override
    public Product createProduct(Product product, MultipartFile image) {
        // saving the image into resources/images
        String folderPath = "src/main/resources/static/images/";

        // using time stamp to create a unique folder to save image
        String imageFolderPath = folderPath + System.currentTimeMillis() + "/";

        // determining the folder whether existed, if not exists, create it
        File folder = new File(imageFolderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // 保存图片
        try {
            String imagePath = imageFolderPath + image.getOriginalFilename();
            Files.copy(image.getInputStream(), Paths.get(imagePath)); // image.getInputStream() may throw IOException

            // setting product's url
            product.setImageUrl("/images/" + System.currentTimeMillis() + "/" + image.getOriginalFilename());
        } catch (IOException e) {
            // handling the error
            throw new RuntimeException("Can't save image", e);
        }

        // saving the product to database
        return productRepository.save(product);
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to delete
     * @throws RuntimeException if the product is not found
     */
    @Override
    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    /**
     * Updates an existing product.
     *
     * @param product the product with updated information
     */
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

    /**
     * Searches for products by various filters.
     *
     * @param keyword the keyword to search for
     * @param category the category of the product
     * @param minPrice the minimum price of the product
     * @param maxPrice the maximum price of the product
     * @return a list of products matching the filters
     */
    @Override
    public List<Product> searchProductsByFilters(String keyword, String category, double minPrice, double maxPrice) {
        return productRepository.SearchProductsByFilters(keyword, category, minPrice, maxPrice);
    }

    /**
     * Finds paginated products.
     *
     * @param pageNo the page number
     * @param pageSize the size of the page
     * @param sortField the field to sort by
     * @param sortDirection the direction to sort (asc/desc)
     * @return a page of products
     */
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