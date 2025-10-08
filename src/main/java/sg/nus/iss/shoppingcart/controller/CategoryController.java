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

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryInterface categoryService;

    public CategoryController(CategoryImplementation categoryInterfaceImpl) {
        this.categoryService = categoryInterfaceImpl;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCategory(@RequestBody Category category, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Category Details");
        }
        return categoryService.createCategory(category) != null ? ResponseEntity.ok("Category Added Successfully")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to Add Category");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Category>> listAllCategorys() {
        return categoryService.getAllCategorys().isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
                : ResponseEntity.ok(categoryService.getAllCategorys());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable("id") Long id, @RequestBody Category category) {
        return categoryService.updateCategory(id, category) != null ? ResponseEntity.ok("Category edited Successfully")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to Add Category");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response<Void>> deleteCategory(@PathVariable("id") Long id) {
        try {
            Response<Void> response = categoryService.deleteCategory(id);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR, "Failed Delete：" + e.getMessage(), null));
        }
    }
}
