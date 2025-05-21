// src/controller/StockController.java
package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import model.Stock;
import service.StockService;
import util.MenuNavigator;

/**
 * Controller for managing stocks.
 */
public class StockController extends BaseController {
    private StockService stockService;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public StockController() {
        this.stockService = new StockService();
    }

    @Override
    protected void showHeader() {
        System.out.println("\\n=== Stock Management ===");
    }

    @Override
    protected void showOptions() {
        System.out.println("1. Add Stock");
        System.out.println("2. View All Stocks");
        System.out.println("3. Back to Main Menu");
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
            addStock();
        } else if (choice == 2) {
            viewAllStocks();
        } else if (choice == 3) {
            return true; // Exit controller
        } else {
            System.out.println("Invalid choice. Please select again.");
        }
        return false;
    }

    private void addStock() {
        System.out.println("--- Add New Stock ---");
        System.out.print("Product ID: ");
        int productId = parseIntInput();

        System.out.print("Quantity: ");
        int quantity = parseIntInput();

        System.out.print("Cost Price: ");
        double costPrice = parseDoubleInput();

        System.out.print("Selling Price: ");
        double sellingPrice = parseDoubleInput();

        System.out.print("Tax Percentage: ");
        double tax = parseDoubleInput();

        System.out.print("Warehouse ID: ");
        int warehouseId = parseIntInput();

        System.out.print("Expiry Date (YYYY-MM-DD): ");
        String expiryDateStr = MenuNavigator.getInput();
        Date expiryDate = null;
        try {
            expiryDate = dateFormat.parse(expiryDateStr);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Setting expiry date to today.");
            expiryDate = new Date();
        }

        int stockId = stockService.addStock(productId, quantity, costPrice,
                sellingPrice, tax, warehouseId, expiryDate);
        if (stockId > 0) {
            System.out.println("Stock added successfully with ID: " + stockId);
        } else {
            System.out.println("Failed to add stock.");
        }
    }

    private void viewAllStocks() {
        System.out.println("--- All Stocks ---");
        List<Stock> stocks = stockService.getAllStocks();
        if (stocks.isEmpty()) {
            System.out.println("No stocks available.");
            return;
        }
        for (Stock s : stocks) {
            System.out.println("ID: " + s.getId() + ", Product ID: " + s.getProductId() +
                    ", Quantity: " + s.getQuantity() + ", Expiry Date: " + s.getExpiryDate());
        }
    }

    public void showViewOptions() {
        System.out.println("\n=== Stock Viewer ===");
        System.out.println("1. View All Stocks");
        System.out.println("2. Check Product Stock");
        System.out.println("3. View Low Stock Items");
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
                    viewAllStocks();
                    break;
                case 2:
                    checkProductStock();
                    break;
                case 3:
                    viewLowStockItems();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void checkProductStock() {
        System.out.print("Enter product ID to check stock: ");
        String input = MenuNavigator.getInput();
        try {
            int productId = Integer.parseInt(input);
            List<Stock> stocks = stockService.getStocksByProductId(productId);
            if (stocks.isEmpty()) {
                System.out.println("No stock found for this product.");
                return;
            }
            System.out.println("\nStock Details for Product ID: " + productId);
            displayStocks(stocks);
        } catch (NumberFormatException e) {
            System.out.println("Invalid product ID. Please enter a number.");
        }
    }

    private void viewLowStockItems() {
        List<Stock> lowStocks = stockService.getLowStockItems();
        if (lowStocks.isEmpty()) {
            System.out.println("No low stock items found.");
            return;
        }
        System.out.println("\nLow Stock Items:");
        displayStocks(lowStocks);
    }

    private void displayStocks(List<Stock> stocks) {
        for (Stock stock : stocks) {
            System.out.println("Stock ID: " + stock.getId() +
                    ", Product ID: " + stock.getProductId() +
                    ", Quantity: " + stock.getQuantity() +
                    ", Batch: " + stock.getBatch() +
                    ", Expiry: " + (stock.getExpiryDate() != null ? dateFormat.format(stock.getExpiryDate()) : "N/A"));
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

    /**
     * Parses double input with basic validation.
     * @return Parsed double or 0.0 if invalid.
     */
    private double parseDoubleInput() {
        String input = MenuNavigator.getInput();
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Setting to 0.0.");
            return 0.0;
        }
    }
}
