// src/controller/ProductController.java
package controller;

import model.Product;
import model.Category;
import service.CategoryService;
import service.ProductService;
import util.MenuNavigator;

import java.util.List;

/**
 * Controller for managing products.
 * Utilizes Template Method and Command Patterns.
 */
public class ProductController extends BaseController {
    private ProductService productService;
    private CategoryService categoryService;

    public ProductController() {
        this.productService = new ProductService();
        this.categoryService = new CategoryService();
    }

    @Override
    protected void showHeader() {
        System.out.println("\\n=== Product Management ===");
    }

    @Override
    protected void showOptions() {
        System.out.println("1. Add New Product");
        System.out.println("2. View All Products");
        System.out.println("3. Update Product");
        System.out.println("4. Delete Product");
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
            addProduct();
        } else if (choice == 2) {
            viewAllProducts();
        } else if (choice == 3) {
            updateProduct();
        } else if (choice == 4) {
            deleteProduct();
        } else if (choice == 5) {
            return true; // Exit controller
        } else {
            System.out.println("Invalid choice. Please select again.");
        }
        return false; // Continue running
    }

    public void addProduct() {
        System.out.println("--- Add New Product ---");
        System.out.print("Product Name: ");
        String name = MenuNavigator.getInput();

        System.out.print("Unit ID: ");
        int unitId = parseIntInput();

        System.out.print("Category ID: ");
        int categoryId = parseIntInput();

        System.out.print("Status: ");
        String status = MenuNavigator.getInput();

        System.out.print("Warehouse ID: ");
        int warehouseId = parseIntInput();

        System.out.print("Note: ");
        String note = MenuNavigator.getInput();

        System.out.print("Stock Alert Level: ");
        int stockAlert = parseIntInput();

        System.out.print("Supplier ID: ");
        int supplierId = parseIntInput();
        
        System.out.print("Price: ");
        int price = parseIntInput();

        int productId = productService.addProduct(name, unitId, categoryId, status,
                warehouseId, note, stockAlert, supplierId, price);
        if (productId > 0) {
            System.out.println("Product added successfully with ID: " + productId);
        } else {
            System.out.println("Failed to add product.");
        }
    }

    private void viewAllProducts() {
        System.out.println("--- All Products ---");
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }
        for (Product p : products) {
            System.out.println("ID: " + p.getId() + ", Name: " + p.getName() +
                    ", Category ID: " + p.getCategoryId() + ", Stock Alert: " + p.getStockAlert());
        }
    }

    private void updateProduct() {
        System.out.println("--- Update Product ---");
        System.out.print("Enter Product ID to update: ");
        int id = parseIntInput();

        System.out.print("New Product Name: ");
        String name = MenuNavigator.getInput();

        System.out.print("New Unit ID: ");
        int unitId = parseIntInput();

        System.out.print("New Category ID: ");
        int categoryId = parseIntInput();

        System.out.print("New Status: ");
        String status = MenuNavigator.getInput();

        System.out.print("New Warehouse ID: ");
        int warehouseId = parseIntInput();

        System.out.print("New Note: ");
        String note = MenuNavigator.getInput();

        System.out.print("New Stock Alert Level: ");
        int stockAlert = parseIntInput();

        System.out.print("New Supplier ID: ");
        int supplierId = parseIntInput();
        
        System.out.print("Price: ");
        int price = parseIntInput();

        boolean updated = productService.updateProduct(id, name, unitId, categoryId,
                status, warehouseId, note, stockAlert, supplierId, price);
        if (updated) {
            System.out.println("Product updated successfully.");
        } else {
            System.out.println("Failed to update product.");
        }
    }

    private void deleteProduct() {
        System.out.println("--- Delete Product ---");
        System.out.print("Enter Product ID to delete: ");
        int id = parseIntInput();
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            System.out.println("Product deleted successfully.");
        } else {
            System.out.println("Failed to delete product.");
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

    public void showViewOptions() {
        System.out.println("\n=== Product Viewer ===");
        System.out.println("1. View All Products");
        System.out.println("2. Search Product");
        System.out.println("3. View Product Details");
        System.out.println("4. Back to Main Menu");
        
        while (true) {
            System.out.print("Enter your choice: ");
            String input = MenuNavigator.getInput();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    viewAllProducts();
                    break;
                case 2:
                    searchProduct();
                    break;
                case 3:
                    viewProductDetails();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void searchProduct() {
        System.out.print("Enter product code to search: ");
        String query = MenuNavigator.getInput();
        List<Product> products = productService.searchProducts(query);
        if (products.isEmpty()) {
            System.out.println("No products found matching your search.");
            return;
        }
        System.out.println("\nSearch Results:");
        displayProducts(products);
    }

    private void viewProductDetails() {
        System.out.print("Enter product ID to view details: ");
        String input = MenuNavigator.getInput();
        try {
            int productId = Integer.parseInt(input);
            Product product = productService.getProductById(productId);
            if (product != null) {
                System.out.println("\nProduct Details:");
                System.out.println("ID: " + product.getId());
                System.out.println("Name: " + product.getName());
                System.out.println("Code: " + product.getCode());
                System.out.println("Price: " + product.getPrice());
                Category category = categoryService.getCategoryById(product.getCategoryId());
                System.out.println("Category: " + (category != null ? category.getName() : "Unknown"));
                System.out.println("Description: " + product.getDescription());
            } else {
                System.out.println("Product not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid product ID. Please enter a number.");
        }
    }

    private void displayProducts(List<Product> products) {
        for (Product p : products) {
            System.out.println("ID: " + p.getId() + ", Name: " + p.getName() +
                    ", Category ID: " + p.getCategoryId() + ", Stock Alert: " + p.getStockAlert());
        }
    }

    public void manageProducts() {
    }
}
