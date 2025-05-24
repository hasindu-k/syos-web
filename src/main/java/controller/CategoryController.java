// src/controller/CategoryController.java
package controller;

import model.Category;
import service.CategoryService;
import util.MenuNavigator;

import java.util.List;

/**
 * Controller for managing categories.
 */
public class CategoryController extends BaseController {
    private CategoryService categoryService;

    public CategoryController() {
        this.categoryService = new CategoryService();
    }

    @Override
    protected void showHeader() {
        System.out.println("\\n=== Category Management ===");
    }

    @Override
    protected void showOptions() {
        System.out.println("1. Add New Category");
        System.out.println("2. View All Categories");
        System.out.println("3. Update Category");
        System.out.println("4. Delete Category");
        System.out.println("5. Back to Main Menu");
    }

    @Override
    protected int getUserChoice() {
        System.out.print("Enter your choice: ");
        String input = MenuNavigator.getInput();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1; // Invalid choice
        }
    }

    @Override
    protected boolean handleChoice(int choice) {
        if (choice == 1) {
            addCategory();
        } else if (choice == 2) {
            viewAllCategories();
        } else if (choice == 3) {
            updateCategory();
        } else if (choice == 4) {
            deleteCategory();
        } else if (choice == 5) {
            return true; // Exit controller
        } else {
            System.out.println("Invalid choice. Please select again.");
        }
        return false; // Continue running
    }

    private void addCategory() {
        System.out.println("--- Add New Category ---");
        System.out.print("Category Name: ");
        String name = MenuNavigator.getInput();

        System.out.print("Category Description: ");
        String description = MenuNavigator.getInput();

        int categoryId = categoryService.addCategory(name, description);
        if (categoryId > 0) {
            System.out.println("Category added successfully with ID: " + categoryId);
        } else {
            System.out.println("Failed to add category.");
        }
    }

    private void viewAllCategories() {
        System.out.println("--- All Categories ---");
        List<Category> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            System.out.println("No categories available.");
            return;
        }
        for (Category c : categories) {
            System.out.println("ID: " + c.getId() + ", Name: " + c.getName() +
                    ", Description: " + c.getDescription());
        }
    }

    private void updateCategory() {
        System.out.println("--- Update Category ---");
        System.out.print("Enter Category ID to update: ");
        int id = parseIntInput();

        System.out.print("New Category Name: ");
        String name = MenuNavigator.getInput();

        System.out.print("New Category Description: ");
        String description = MenuNavigator.getInput();

        boolean updated = categoryService.updateCategory(id, name, description);
        if (updated) {
            System.out.println("Category updated successfully.");
        } else {
            System.out.println("Failed to update category.");
        }
    }

    private void deleteCategory() {
        System.out.println("--- Delete Category ---");
        System.out.print("Enter Category ID to delete: ");
        int id = parseIntInput();
        boolean deleted = categoryService.deleteCategory(id);
        if (deleted) {
            System.out.println("Category deleted successfully.");
        } else {
            System.out.println("Failed to delete category.");
        }
    }

    /**
     * Parses integer input with basic validation.
     * @return Parsed integer or -1 if invalid.
     */
    private int parseIntInput() {
        String input = MenuNavigator.getInput();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Setting to -1.");
            return -1;
        }
    }

    public void manageCategories() {
    }
}
