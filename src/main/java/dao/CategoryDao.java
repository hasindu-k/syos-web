// src/dao/CategoryDao.java
package dao;

import model.Category;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Category operations.
 */
public class CategoryDao {
    /**
     * Adds a new category.
     * @param category Category object.
     * @return Generated Category ID or -1 if failed.
     */
    public int addCategory(Category category) {
        String query = "INSERT INTO categories (name, description) VALUES (?, ?)";
        try (Connection conn = DBConnection.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Creating category failed, no rows affected.");
                return -1;
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
                else {
                    System.out.println("Creating category failed, no ID obtained.");
                    return -1;
                }
            }
        } catch (SQLException e) {
            System.out.println("Add Category Error: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Retrieves all categories.
     * @return List of Category objects.
     */
    public List<Category> getAllCategories() {
        String query = "SELECT * FROM categories";
        List<Category> categories = new ArrayList<>();
        try (Connection conn = DBConnection.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Category category = new Category(
                        rs.getString("name"),
                        rs.getString("description")
                );
                category.setId(rs.getInt("id"));
                categories.add(category);
            }
        } catch (SQLException e) {
            System.out.println("Get All Categories Error: " + e.getMessage());
        }
        return categories;
    }

    /**
     * Updates an existing category.
     * @param category Category object with updated details.
     * @return True if successful, else False.
     */
    public boolean updateCategory(Category category) {
        String query = "UPDATE categories SET name = ?, description = ? WHERE id = ?";
        try (Connection conn = DBConnection.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setInt(3, category.getId());
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Update Category Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a category by ID.
     * @param categoryId ID of the category.
     * @return True if successful, else False.
     */
    public boolean deleteCategory(int categoryId) {
        String query = "DELETE FROM categories WHERE id = ?";
        try (Connection conn = DBConnection.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, categoryId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Delete Category Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a category by its ID.
     * @param id Category ID to search for.
     * @return Category if found, else null.
     */
    public Category getCategoryById(int id) {
        String query = "SELECT * FROM categories WHERE id = ?";
        try (Connection conn = DBConnection.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Category category = new Category(
                        rs.getString("name"),
                        rs.getString("description")
                    );
                    category.setId(rs.getInt("id"));
                    return category;
                }
            }
        } catch (SQLException e) {
            System.out.println("Get Category By ID Error: " + e.getMessage());
        }
        return null;
    }
}
