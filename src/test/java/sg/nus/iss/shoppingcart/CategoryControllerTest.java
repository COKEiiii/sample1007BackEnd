package sg.nus.iss.shoppingcart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import sg.nus.iss.shoppingcart.model.Category;
import sg.nus.iss.shoppingcart.interfacemethods.CategoryInterface;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryInterface categoryService;

    @BeforeEach
    public void setup() {
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAddCategory_ValidCategory() throws Exception {
        Category category = new Category();
        category.setName("Electronics");

        when(categoryService.createCategory(any(Category.class))).thenReturn(category);

        mockMvc.perform(post("/category/add")
                .contentType("application/json")
                .content("{ \"name\": \"Electronics\" }"))
                .andExpect(status().isOk()) // Expecting 200 OK
                .andExpect(content().string("Category Added Successfully"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAddCategory_InvalidCategory() throws Exception {
        mockMvc.perform(post("/category/add")
                .contentType("application/json")
                .content("{ \"name\": \"\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid Category Details"));
    }

    @Test
    public void testListAllCategories_Success() throws Exception {

        Category category1 = new Category();
        category1.setName("Electronics");
        Category category2 = new Category();
        category2.setName("Books");

        List<Category> categories = Arrays.asList(category1, category2);

        when(categoryService.getAllCategorys()).thenReturn(categories);

        mockMvc.perform(get("/all/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Electronics"))
                .andExpect(jsonPath("$[1].name").value("Books"));
    }

    @Test
    public void testListAllCategories_NotFound() throws Exception {

        when(categoryService.getAllCategorys()).thenReturn(List.of());
        mockMvc.perform(get("/all/categories"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateCategory_Success() throws Exception {

        Category updatedCategory = new Category();
        updatedCategory.setName("Updated Category");

        mockMvc.perform(post("/category/update/1")
                .param("id", "1")
                .param("name", "Updated Category"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/all/categories"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteCategory_Success() throws Exception {

        Mockito.doNothing().when(categoryService).deleteCategory(anyLong());

        mockMvc.perform(delete("/category/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/all/categories"));
    }
}
