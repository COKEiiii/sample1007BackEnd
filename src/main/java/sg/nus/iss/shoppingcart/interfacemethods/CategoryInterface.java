package sg.nus.iss.shoppingcart.interfacemethods;

import sg.nus.iss.shoppingcart.model.Category;
import sg.nus.iss.shoppingcart.utils.Response;

import java.util.List;
import java.util.Optional;

/**
 * @ClassName CategoryInterface
 * @Description Interface for category operations, providing methods to manage category.
 * @Author YAO YIYANG
 * @StudentID A0294873L
 * @Date 2024/10/8
 * @Version 1.0
 */
public interface CategoryInterface {

	    /**
	     * Retrieves all Categories.
	     *
	     * @return a list of all Categories
	     */
	    List<Category> getAllCategorys();

	    /**
	     * Creates a new Category.
	     *
	     * @param Category the Category to create
	     * @return the created Category
	     */
	    Category createCategory(Category category);

	    /**
	     * Deletes a Category by its ID.
	     *
	     * @param id the ID of the Category to delete
	     */
		Response<Void> deleteCategory(Long id);

	    /**
	     * Updates an existing Category.
	     *
	     * @param
	     */
		Category updateCategory(Long id, Category category);
	
}
