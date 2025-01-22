package sg.nus.iss.shoppingcart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import sg.nus.iss.shoppingcart.enums.ResponseStatus;
import sg.nus.iss.shoppingcart.interfacemethods.CategoryInterface;
import sg.nus.iss.shoppingcart.model.Category;
import sg.nus.iss.shoppingcart.service.CategoryImplementation;
import sg.nus.iss.shoppingcart.utils.Response;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName CategoryController
 * @Description Controller for handling category-related operations.
 * @Author YAO YIYANG
 * @StudentID A0294873L
 * @Date 2024/10/3
 * @Version 1.0
 */


/**
 * @ClassName CategoryController
 * @Description Controller for handling category-related operations.
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryInterface categoryService;

    /**
     * Constructor for CategoryController.
     *
     * @param categoryInterfaceImpl the implementation of CategoryInterface
     */
    public CategoryController(CategoryImplementation categoryInterfaceImpl) {
        this.categoryService = categoryInterfaceImpl;
    }

    /**
     * Adds a new category. Only accessible by admin users.
     *
     * @param category the category to add
     * @param result the binding result for validation
     * @return a ResponseEntity containing a success or error message
     */
    // @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<String> addCategory(@RequestBody Category category, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Category Details");
        }
        return categoryService.createCategory(category) != null ? ResponseEntity.ok("Category Added Successfully") : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to Add Category");
    }

    /**
     * Displays all categories.
     *
     * @return a ResponseEntity containing the list of all categories
     */
    @GetMapping("/all")
    public ResponseEntity<List<Category>> listAllCategorys() {
        return categoryService.getAllCategorys().isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(categoryService.getAllCategorys());
    }

    /**
     * Updates an existing category.
     *
     * @param id the ID of the category to update
     * @param category the updated category detail
     * @throws IOException if an I/O error occurs
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable("id") Long id, @RequestBody Category category) {
        return categoryService.updateCategory(id, category) != null ? ResponseEntity.ok("Category edited Successfully") : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to Add Category");
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id the ID of the category to delete
     * @return
     * @throws IOException if an I/O error occurs
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response<Void>> deleteCategory(@PathVariable("id") Long id) {
        try {
            Response<Void> response = categoryService.deleteCategory(id);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR, "Failed Delete：" + e.getMessage(), null));
        }
    }
}
