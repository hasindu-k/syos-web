// src/service/CategoryService.java
package service;

import dao.CategoryDao;
import model.Category;

import java.util.List;

/**
 * Service class for Category operations.
 */
public class CategoryService {
    private CategoryDao categoryDao;

    public CategoryService() {
        this.categoryDao = new CategoryDao();
    }

    /**
     * Adds a new category.
     * @param name Category name.
     * @param description Category description.
     * @return Category ID if successful, else -1.
     */
    public int addCategory(String name, String description) {
        Category category = new Category(name, description);
        return categoryDao.addCategory(category);
    }

    /**
     * Retrieves all categories.
     * @return List of categories.
     */
    public List<Category> getAllCategories() {
        return categoryDao.getAllCategories();
    }

    /**
     * Updates an existing category.
     * @param id Category ID.
     * @param name New name.
     * @param description New description.
     * @return True if successful, else False.
     */
    public boolean updateCategory(int id, String name, String description) {
        Category category = new Category(name, description);
        category.setId(id);
        return categoryDao.updateCategory(category);
    }

    /**
     * Deletes a category by ID.
     * @param id Category ID.
     * @return True if successful, else False.
     */
    public boolean deleteCategory(int id) {
        return categoryDao.deleteCategory(id);
    }

    /**
     * Retrieves a category by its ID.
     * @param id Category ID to search for.
     * @return Category if found, else null.
     */
    public Category getCategoryById(int id) {
        return categoryDao.getCategoryById(id);
    }
}
