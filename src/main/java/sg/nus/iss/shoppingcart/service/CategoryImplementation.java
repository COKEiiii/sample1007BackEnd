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

/**
 * @ClassName CategoryImplemetation
 * @Description Implementation of the CategoryInterface, providing services related to the shopping cart.
 * @Author YAO YIYANG
 * @StudentID A0294873L
 * @Date 2024/10/8
 * @Version 1.0
 */

/**
 * Service implementation for handling category-related operations.
 */
@Service
@Transactional
public class CategoryImplementation implements CategoryInterface {

    private final CategoryRepository categoryRepository;

    /**
     * Constructor for CategoryImplementation.
     *
     * @param categoryRepository the category repository
     */
    public CategoryImplementation(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Retrieves all categorys.
     *
     * @return a list of all categorys
     */
    @Override
    public List<Category> getAllCategorys() {
        return categoryRepository.findAll();
    }

    /**
     * Creates a new category.
     *
     * @param category the category to create
     * @return the created category
     */
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
        // 从数据库中查找现有类别
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        // 更新类别信息
        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());

        // 保存更新后的类别
        return categoryRepository.save(existingCategory);
    }
}
