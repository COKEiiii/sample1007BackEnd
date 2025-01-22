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
@AutoConfigureMockMvc // Enables and configures MockMvc in Spring context
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;  // Injecting MockMvc

    @MockBean
    private CategoryInterface categoryService;

    @BeforeEach
    public void setup() {
        // No need for MockMvcBuilders.standaloneSetup, Spring automatically configures MockMvc with full context
    }

    @Test
    @WithMockUser(roles = "ADMIN") // Test with an ADMIN role user
    public void testAddCategory_ValidCategory() throws Exception {
        // Given
        Category category = new Category();
        category.setName("Electronics");

        when(categoryService.createCategory(any(Category.class))).thenReturn(category);

        // When & Then
        mockMvc.perform(post("/category/add")
                        .contentType("application/json")
                        .content("{ \"name\": \"Electronics\" }"))
                .andExpect(status().isOk())  // Expecting 200 OK
                .andExpect(content().string("Category Added Successfully"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAddCategory_InvalidCategory() throws Exception {
        // When & Then
        mockMvc.perform(post("/category/add")
                        .contentType("application/json")
                        .content("{ \"name\": \"\" }"))  // Invalid category with empty name
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Invalid Category Details"));
    }

    @Test
    public void testListAllCategories_Success() throws Exception {
        // Given
        Category category1 = new Category();
        category1.setName("Electronics");
        Category category2 = new Category();
        category2.setName("Books");

        List<Category> categories = Arrays.asList(category1, category2);

        when(categoryService.getAllCategorys()).thenReturn(categories);

        // When & Then
        mockMvc.perform(get("/all/categories"))
                .andExpect(status().isOk())  // Expecting 200 OK
                .andExpect(jsonPath("$.length()").value(2))  // Expecting two categories
                .andExpect(jsonPath("$[0].name").value("Electronics"))
                .andExpect(jsonPath("$[1].name").value("Books"));
    }

    @Test
    public void testListAllCategories_NotFound() throws Exception {
        // Given
        when(categoryService.getAllCategorys()).thenReturn(List.of());  // Empty list

        // When & Then
        mockMvc.perform(get("/all/categories"))
                .andExpect(status().isNotFound());  // Expecting 404 Not Found
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateCategory_Success() throws Exception {
        // Given
        Category updatedCategory = new Category();
        updatedCategory.setName("Updated Category");

        // Mockito.doNothing().when(categoryService).updateCategory(any(Category.class));

        // When & Then
        mockMvc.perform(post("/category/update/1")
                        .param("id", "1")
                        .param("name", "Updated Category"))
                .andExpect(status().is3xxRedirection())  // Expecting redirect to /all/categories
                .andExpect(redirectedUrl("/all/categories"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteCategory_Success() throws Exception {
        // Given
        Mockito.doNothing().when(categoryService).deleteCategory(anyLong());

        // When & Then
        mockMvc.perform(delete("/category/delete/1"))
                .andExpect(status().is3xxRedirection())  // Expecting redirect to /all/categories
                .andExpect(redirectedUrl("/all/categories"));
    }
}
