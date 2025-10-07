package sg.nus.iss.shoppingcart.interfacemethods;

import sg.nus.iss.shoppingcart.model.Category;
import sg.nus.iss.shoppingcart.utils.Response;

import java.util.List;
import java.util.Optional;

public interface CategoryInterface {

	List<Category> getAllCategorys();

	Category createCategory(Category category);

	Response<Void> deleteCategory(Long id);

	Category updateCategory(Long id, Category category);

}
