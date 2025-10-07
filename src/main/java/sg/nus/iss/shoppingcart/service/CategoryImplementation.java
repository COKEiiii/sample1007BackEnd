package sg.nus.iss.shoppingcart.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import sg.nus.iss.shoppingcart.enums.ResponseStatus;
import sg.nus.iss.shoppingcart.interfacemethods.CategoryInterface;
import sg.nus.iss.shoppingcart.model.Category;
import sg.nus.iss.shoppingcart.repository.CategoryRepository;
import sg.nus.iss.shoppingcart.utils.Response;

@Service
@Transactional
public class CategoryImplementation implements CategoryInterface {

    private final CategoryRepository categoryRepository;

    public CategoryImplementation(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategorys() {
        return categoryRepository.findAll();
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Response<Void> deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            return new Response<>(ResponseStatus.NOT_FOUND, "Cannot find user");
        }
        categoryRepository.deleteById(id);
        return new Response<>(ResponseStatus.SUCCESS, "User deleted successfully");
    }

    @Override
    public Category updateCategory(Long id, Category category) {

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());

        return categoryRepository.save(existingCategory);
    }
}
