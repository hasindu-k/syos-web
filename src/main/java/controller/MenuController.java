// src/controller/MenuController.java
package controller;

import controller.BillController;
import controller.CategoryController;
import controller.ProductController;
import controller.StockController;
import service.ReportService;
import strategy.*;

import java.util.Scanner;
import service.shelvesService;

/**
 * Menu Controller implementing Command Pattern and Template Method.
 */
public class MenuController {

    private String role;
    private Scanner scanner;

    public MenuController(String role) {
        this.role = role;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the main menu based on role.
     */
    public void showMenu() {
        while (true) {
            System.out.println("\n=== Main Menu ===");
            if (role.equalsIgnoreCase("Admin")) {
                System.out.println("1. Manage Products");
                System.out.println("2. Manage Categories");
                System.out.println("3. Manage Users");
                System.out.println("4. View Reports");
                System.out.println("5. System Settings");
                System.out.println("6. Exit");
            } else if (role.equalsIgnoreCase("Manager")) {
                System.out.println("1. Manage Stocks");
                System.out.println("2. View Products");
                System.out.println("3. View Categories");
                System.out.println("4. Generate Reports");
                System.out.println("5. Reshelve Items");
                System.out.println("6. Exit");
            } else if (role.equalsIgnoreCase("Cashier")) {
                System.out.println("1. Generate Bill");
                System.out.println("2. View Products");
                System.out.println("3. Check Stock");
                System.out.println("4. View Today's Bills");
                System.out.println("5. Exit");
            } else {
                System.out.println("Unknown role. Exiting.");
                break;
            }
            System.out.print("Enter your choice: ");
            String input = scanner.nextLine();
            int choice = -1;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            handleSelection(choice);
        }
    }

    /**
     * Handles menu selection using if-else.
     *
     * @param choice User choice.
     */
    private void handleSelection(int choice) {
        if (role.equalsIgnoreCase("Admin")) {
            switch (choice) {
                case 1:
                    new ProductController().run();
                    break;
                case 2:
                    new CategoryController().run();
                    break;
                case 3:
                    manageUsers();
                    break;
                case 4:
                    generateReports();
                    break;
                case 5:
                    showSystemSettings();
                    break;
                case 6:
                    System.out.println("Exiting POS System. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please select again.");
            }
        } else if (role.equalsIgnoreCase("Manager")) {
            switch (choice) {
                case 1:
                    new StockController().run();
                    break;
                case 2:
                    new ProductController().showViewOptions();
                    break;
                case 3:
                    new CategoryController().run();
                    break;
                case 4:
                    generateReports();
                    break;
                case 5:
                    restockShelves();
                    break;
                case 6:
                    System.out.println("Exiting POS System. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please select again.");
            }
        } else if (role.equalsIgnoreCase("Cashier")) {
            switch (choice) {
                case 1:
                    new BillController().generateBill();
                    break;
                case 2:
                    new ProductController().showViewOptions();
                    break;
                case 3:
                    new StockController().showViewOptions();
                    break;
                case 4:
                    viewTodayBills();
                    break;
                case 5:
                    System.out.println("Exiting POS System. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please select again.");
            }
        }

        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }

    /**
     * Generates reports using Strategy Pattern.
     */
    private void generateReports() {
        ReportService reportService = new ReportService();
        System.out.println("\n=== Generate Reports ===");
        System.out.println("1. Total Sale Report");
        System.out.println("2. Reshelved Items Report");
        System.out.println("3. Reorder Levels Report");
        System.out.println("4. Stock Batch-Wise Report");
        System.out.println("5. Bill Report");
        System.out.println("6. Combined Report");
        System.out.println("7. Back to Main Menu");
        System.out.print("Enter your choice: ");
        String input = scanner.nextLine();
        int reportChoice = -1;
        try {
            reportChoice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return;
        }

        switch (reportChoice) {
            case 1:
                System.out.print("Enter date (YYYY-MM-DD): ");
                String date1 = scanner.nextLine();
                ReportStrategy totalSale = new TotalSaleReportStrategy(date1);
                reportService.generateReport(totalSale);
                break;
            case 2:
                System.out.print("Enter date (YYYY-MM-DD): ");
                String date2 = scanner.nextLine();
                ReportStrategy reshelved = new ReshelvedItemsReportStrategy(date2);
                reportService.generateReport(reshelved);
                break;
            case 3:
                ReportStrategy reorder = new ReorderLevelsReportStrategy();
                reportService.generateReport(reorder);
                break;
            case 4:
                ReportStrategy stockBatch = new StockBatchWiseReportStrategy();
                reportService.generateReport(stockBatch);
                break;
            case 5:
                ReportStrategy billReport = new BillReportStrategy();
                reportService.generateReport(billReport);
                break;
            case 6:
                System.out.print("Enter date (YYYY-MM-DD) for Total Sale & Reshelved Items: ");
                String combinedDate = scanner.nextLine();
                ReportStrategy combinedTotalSale = new TotalSaleReportStrategy(combinedDate);
                ReportStrategy combinedReshelved = new ReshelvedItemsReportStrategy(combinedDate);
                ReportStrategy combinedReorder = new ReorderLevelsReportStrategy();
                ReportStrategy combinedStockBatch = new StockBatchWiseReportStrategy();
                ReportStrategy combinedBillReport = new BillReportStrategy();
                reportService.generateReport(combinedTotalSale);
                reportService.generateReport(combinedReshelved);
                reportService.generateReport(combinedReorder);
                reportService.generateReport(combinedStockBatch);
                reportService.generateReport(combinedBillReport);
                break;
            case 7:
                // Back to main menu
                break;
            default:
                System.out.println("Invalid choice. Please select again.");
        }
    }

    private void manageUsers() {
        System.out.println("=== User Management ===");
        System.out.println("Feature coming soon...");
    }

    private void showSystemSettings() {
        System.out.println("=== System Settings ===");
        System.out.println("Feature coming soon...");
    }

    private void restockShelves() {
        System.out.println("=== Restocking Shelves ===");
        shelvesService shlsrvic = new shelvesService();
        shlsrvic.reshelf();
    }

    private void viewBills() {
        System.out.println("=== View Bills ===");
        BillController billCtrl = new BillController();
        billCtrl.viewBills();
    }

    private void viewTodayBills() {
        System.out.println("\n=== Today's Bills ===");
        BillController billCtrl = new BillController();
        billCtrl.viewTodayBills();
    }
}
