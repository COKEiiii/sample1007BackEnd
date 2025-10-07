package sg.nus.iss.shoppingcart.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sg.nus.iss.shoppingcart.interfacemethods.ProductInterface;
import sg.nus.iss.shoppingcart.model.Product;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductInterface productService;

    public ProductController(ProductInterface productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public void getSearchPage(HttpServletResponse response) throws IOException {
        response.sendRedirect("/all/product");
    }

    @GetMapping(value = "/all/product/searching")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam("keyword") String k,
            @RequestParam("searchtype") String t) {
        String name = "name";
        String category = "category";
        List<Product> result;

        if (t.equals(name)) {
            result = productService.searchProductByName(k);
            if (!result.isEmpty()) {
                return ResponseEntity.ok(result);
            }
        } else if (t.equals(category)) {
            result = productService.searchProductByCategory(t);
            if (!result.isEmpty()) {
                return ResponseEntity.ok(result);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestPart Product product, @RequestPart("image") MultipartFile image) {
        try {
            productService.createProduct(product, image);
            return new ResponseEntity<>("Product added successfully", HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to add product: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all/products")
    public ResponseEntity<List<Product>> listAllProducts() {
        return productService.getAllProducts().isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
                : ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping(value = "/update/{id}")
    public void updateProduct(@PathVariable("id") Integer id, Product product, HttpServletResponse response)
            throws IOException {
        Product existingProduct = productService.findProductById(id);
        if (existingProduct != null) {
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setStock(product.getStock());
            existingProduct.setStatus(product.getStatus());
            productService.updateProduct(existingProduct);
        }
        response.sendRedirect("/all/products");
    }

    @DeleteMapping(value = "/delete/{id}")
    public void deleteProduct(@PathVariable("id") Integer id, HttpServletResponse response) throws IOException {
        productService.deleteProduct(id);
        response.sendRedirect("/all/products");
    }

    @GetMapping(value = "/product/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam("keyword") String keyword,
            @RequestParam("category") String category,
            @RequestParam("minPrice") double minPrice, @RequestParam("maxPrice") double maxPrice) {
        List<Product> products = productService.searchProductsByFilters(keyword, category, minPrice, maxPrice);
        return products.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
                : ResponseEntity.ok(products);
    }

    @RequestMapping("/products/page/{pageNo}")
    public ResponseEntity<List<Product>> findPaginated(@PathVariable("pageNo") int pageNo,
            @RequestParam("sortField") String sortField,
            @RequestParam("sortDir") String sortDirection) {
        int pageSize = 5;

        Page<Product> page = productService.findPaginated(pageNo, pageSize, sortField, sortDirection);
        List<Product> products = page.getContent();

        return products.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
                : ResponseEntity.ok(products);
    }
}