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

    /**
     * Redirects to the page displaying all products.
     *
     * @param response the HTTP response
     * @throws IOException if an I/O error occurs
     */
    @GetMapping("/")
    public void getSearchPage(HttpServletResponse response) throws IOException {
        response.sendRedirect("/all/product");
    }

    /**
     * Searches for products by keyword and search type.
     *
     * @param k the keyword to search for
     * @param t the search type (name or category)
     * @return a ResponseEntity containing the list of products matching the search criteria
     */
    @GetMapping(value="/all/product/searching")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam("keyword") String k, @RequestParam("searchtype") String t) {
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

    // /**
    //  * Adds a new product. Only accessible by admin users.
    //  *
    //  * @param product the product to add
    //  * @param result the binding result for validation
    //  * @return a ResponseEntity containing a success or error message
    //  */
    // @PreAuthorize("hasRole('ADMIN')")
    // @PostMapping(value = "/product/add")
    // public ResponseEntity<String> addProduct(@Valid @RequestBody Product product, BindingResult result) {
    //     if (result.hasErrors()) {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Product Details");
    //     }
    //     return productService.createProduct(product) != null ? ResponseEntity.ok("Product Added Successfully") : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to Add Product");
    // }

    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestPart Product product, @RequestPart("image") MultipartFile image) {
        try {
            productService.createProduct(product, image);
            return new ResponseEntity<>("Product added successfully", HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to add product: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Displays all products.
     *
     * @return a ResponseEntity containing the list of all products
     */
    @GetMapping("/all/products")
    public ResponseEntity<List<Product>> listAllProducts() {
        return productService.getAllProducts().isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(productService.getAllProducts());
    }

    /**
     * Updates an existing product.
     *
     * @param id the ID of the product to update
     * @param product the updated product details
     * @param response the HTTP response
     * @throws IOException if an I/O error occurs
     */
    @PostMapping(value = "/update/{id}")
    public void updateProduct(@PathVariable("id") Integer id, Product product, HttpServletResponse response) throws IOException {
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

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to delete
     * @param response the HTTP response
     * @throws IOException if an I/O error occurs
     */
    @DeleteMapping(value = "/delete/{id}")
    public void deleteProduct(@PathVariable("id") Integer id, HttpServletResponse response) throws IOException {
        productService.deleteProduct(id);
        response.sendRedirect("/all/products");
    }

    /**
     * Searches for products by various filters.
     *
     * @param keyword the keyword to search for
     * @param category the category to filter by
     * @param minPrice the minimum price to filter by
     * @param maxPrice the maximum price to filter by
     * @return a ResponseEntity containing the list of products matching the filters
     */
    @GetMapping(value = "/product/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam("keyword") String keyword, @RequestParam("category") String category,
                                                        @RequestParam("minPrice") double minPrice, @RequestParam("maxPrice") double maxPrice) {
        List<Product> products = productService.searchProductsByFilters(keyword, category, minPrice, maxPrice);
        return products.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(products);
    }

    /**
     * Retrieves paginated and sorted products.
     *
     * @param pageNo the page number to retrieve
     * @param sortField the field to sort by
     * @param sortDirection the direction to sort (asc or desc)
     * @return a ResponseEntity containing the list of paginated and sorted products
     */
    @RequestMapping("/products/page/{pageNo}")
    public ResponseEntity<List<Product>> findPaginated(@PathVariable("pageNo") int pageNo,
                                                       @RequestParam("sortField") String sortField,
                                                       @RequestParam("sortDir") String sortDirection) {
        int pageSize = 5;

        Page<Product> page = productService.findPaginated(pageNo, pageSize, sortField, sortDirection);
        List<Product> products = page.getContent();

        return products.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(products);
    }
}