// src/controller/BillController.java
package controller;

import dao.ShelfDao;
import dao.StockDao;
import model.Bill;
import model.BillItem;
import service.BillService;
import service.CustomerService;
import service.ProductService;
import util.MenuNavigator;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import service.shelvesService;
import util.DBConnection;

/**
 * Controller for managing bills.
 */
public class BillController extends BaseController {

    private BillService billService;
    private CustomerService customerService;
    private ProductService productService;
    private StockDao stockDao;

    public BillController() {
        this.billService = new BillService();
        this.customerService = new CustomerService();
        this.productService = new ProductService();
        this.stockDao = new StockDao();
    }

    @Override
    protected void showHeader() {
        System.out.println("\\n=== Bill Management ===");
    }

    @Override
    protected void showOptions() {
        System.out.println("1. Generate Bill");
        System.out.println("2. Reprint Bill");
        System.out.println("3. View Bills");
        System.out.println("4. View Today's Bills");
        System.out.println("5. Back to Main Menu");
    }

    @Override
    protected int getUserChoice() {
        System.out.print("Enter your choice: ");
        String input = MenuNavigator.getInput();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    protected boolean handleChoice(int choice) {
        if (choice == 1) {
            generateBill();
        } else if (choice == 2) {
            reprintBill();
        } else if (choice == 3) {
            viewBills();
        } else if (choice == 4) {
            viewTodayBills();
        } else if (choice == 5) {
            return true; // Exit controller
        } else {
            System.out.println("Invalid choice. Please select again.");
        }
        return false;
    }

    void generateBill() {
        System.out.println("--- Generate Bill ---");
        System.out.print("Select Customer ID (Enter 1 for walk-in): ");
        int customerId = parseIntInput();
        if (customerId == 0) {
            customerId = -1; // Indicate walk-in
        }

        List<BillItem> items = new ArrayList<>();
        double subTotal = 0.0;
        int totalQty = 0;

        while (true) {
            System.out.print("Enter Product ID to add (or enter 0 to finish): ");
            int productId = parseIntInput();
            if (productId == 0) {
                break;
            }

            System.out.print("Enter Quantity: ");
            int quantity = parseIntInput();
            if (quantity > 40) {
                System.out.println("You cannot buy more than 40 items at once");
                System.out.print("Enter Quantity: ");
                quantity = parseIntInput();

            }
            String productName = "Product" + productId; // Placeholder

            try {
                ResultSet productNameRS = DBConnection.INSTANCE.getConnection().createStatement().executeQuery("SELECT `name` FROM `products`"
                        + " WHERE `id` = '" + productId + "'  ");
                if (productNameRS.next()) {
                    productName = productNameRS.getString("name");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            // Fetch product details
            // Here, assuming productService can fetch product by ID
            // and retrieve its name and price
            // For simplicity, we'll skip actual fetching
            double price = 10.0; // Placeholder price
            int availableQty = 0;
            try {
                ResultSet prdPrice = DBConnection.INSTANCE.getConnection().createStatement().executeQuery("SELECT * \n"
                        + "FROM shelves \n"
                        + "INNER JOIN stocks ON shelves.stocks_id = stocks.id \n"
                        + "INNER JOIN products ON products.id = stocks.products_id \n"
                        + "WHERE products.id = '" + productId + "' \n"
                        + "ORDER BY stocks.expiry_date ASC \n"
                        + "LIMIT 1 ");
                while (prdPrice.next()) {
                    price = prdPrice.getDouble("price");
                    availableQty = prdPrice.getInt("shelves.quantity");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            if (quantity > availableQty) {
                System.out.println("Entered quantity is more than the available quantity. Please choose less than " + availableQty);
                System.out.print("Enter Quantity: ");
                quantity = parseIntInput();
            }

            BillItem item = new BillItem(productId, productName, quantity, price);
            items.add(item);
            totalQty += quantity;
            subTotal += item.getTotalPrice();
        }

        // Discounts and Taxes (Simplified)
        System.out.print("Enter Discount Type (FIXED/PERCENTAGE or leave blank for none): ");
        String discountType = MenuNavigator.getInput();
        double discountValue = 0.0;
        if (!discountType.isEmpty()) {
            System.out.print("Enter Discount Value: ");
            discountValue = parseDoubleInput();
        }

//        System.out.print("Enter Tax Type (FIXED/PERCENTAGE or leave blank for none): ");
//        String taxType = MenuNavigator.getInput();
//        double taxValue = 0.0;
//        if (!taxType.isEmpty()) {
//            System.out.print("Enter Tax Value: ");
//            taxValue = parseDoubleInput();
//        }
        // Calculate totals
        double discount = discountType.equalsIgnoreCase("PERCENTAGE") ? (subTotal * discountValue / 100) : discountValue;
//        double tax = taxType.equalsIgnoreCase("PERCENTAGE") ? ((subTotal - discount) * taxValue / 100) : taxValue;
//        double total = subTotal - discount + tax;
        double total = subTotal - discount;

        // Payment
        System.out.print("Enter Received Amount: ");
        double receivedAmount = parseDoubleInput();
        double changeReturn = receivedAmount - total;

        // Create and save bill
        Bill bill = new Bill(customerId != -1 ? customerId : null, totalQty, subTotal,
                discountType, discountValue,
                total, receivedAmount, changeReturn, "Cash", "Paid", items);

        int billId = billService.generateBill(bill, true);
        if (billId > 0) {
//            billService.printBill(billId); 
            shelvesService shelfService = new shelvesService();
            int shelfSuccess = shelfService.updateShelves(billId);
            int saleSuccess = shelfService.updateSales(billId);
            if (shelfSuccess == 1 && saleSuccess == 1) {
                System.out.println("Bill generated successfully with ID: " + billId);
            } else {
                System.out.println("Failed to update Database");

            }
//            updateShelves(billId);
        } else {
            System.out.println("Failed to generate bill.");
        }
    }

    private void updateShelves(int billID) {
        try {
            Statement statement = DBConnection.INSTANCE.getConnection().createStatement();
            ResultSet billSet = statement.executeQuery("SELECT * FROM `bill_items` WHERE `bill_id` = '" + billID + "' ");
            while (billSet.next()) {
                String item_id = billSet.getString("product_id");
                String qty = billSet.getString("quantity");
                ResultSet itemSet = statement.executeQuery("SELECT shelves.stocks_id "
                        + "FROM shelves INNER JOIN stocks ON stocks.id = shelves.stocks_id "
                        + "INNER JOIN products ON products.id = stocks.products_id WHERE products.id = '" + item_id + "';");
                if (itemSet.next()) {
                    String stock_id = itemSet.getString("stocks_id");
                    statement.executeUpdate("UPDATE `shelves`"
                            + " SET `quantity` = quantity-'" + qty + "' WHERE `stocks_id` = '" + stock_id + "' ");
                }

                ResultSet billDetailRs = statement.executeQuery("SELECT "
                        + "customer_id,total_qty,total,payment_type,payment_status,bill_date"
                        + " FROM bills WHERE bills.id = '" + billID + "' ");

                if (billDetailRs.next()) {
                    String customer_id = billDetailRs.getString("customer_id");
                    String total_qty = billDetailRs.getString("total_qty");
                    String total_amount = billDetailRs.getString("total");
                    String payment_type = billDetailRs.getString("payment_type");
                    String payment_status = billDetailRs.getString("payment_status");
                    String sale_date = billDetailRs.getString("bill_date");

                    statement.executeUpdate("INSERT INTO `sales` (customer_id,total_qty,total_amount,"
                            + "payment_type,payment_status,sale_date,bills_id) VALUES ('" + customer_id + "',"
                            + "'" + total_qty + "','" + total_amount + "','" + payment_type + "','" + payment_status + "','" + sale_date + "','" + billID + "')");
                }

//                ResultSet itemSet2 = DBConnection.INSTANCE.getConnection().createStatement().executeQuery("SELECT * "
//                        + "FROM shelves INNER JOIN stocks ON stocks.id = shelves.stocks_id "
//                        + "INNER JOIN products ON products.id = stocks.products_id WHERE products.id = '" + item_id + "';");
//                if (itemSet2.next()) {
//                    String stock_id = itemSet2.getString("shelves.stocks_id");
//                    int remainingQty = itemSet2.getInt("shelves.quantity");
//                    int inStockQty = itemSet2.getInt("stocks.quantity");
//                    int neededQty = 50 - remainingQty;
//                    int stockAlert = itemSet2.getInt("stock_alert");
//                    int sumQty = remainingQty + inStockQty;
//                    Statement statement = DBConnection.INSTANCE.getConnection().createStatement();
//                    if (remainingQty < neededQty) {
//                        if (sumQty < stockAlert) {
////                            ResultSet shelfItem = DBConnection.INSTANCE.getConnection().createStatement().executeQuery("SELECT * FROM "
////                                    + "shelves WHERE stocks_id = '" + stock_id + "'");
//                            statement.executeUpdate("UPDATE stocks SET quantity = '" + remainingQty + "' WHERE stocks.id = '" + stock_id + "'");
//                            ResultSet newStockSet = statement.executeQuery("""
//                                                                           SELECT * 
//                                                                           FROM stocks
//                                                                           INNER JOIN products ON products.id = stocks.products_id 
//                                                                           WHERE products.id = '1' 
//                                                                           AND stocks.quantity > '""" + stockAlert + "' \n"
//                                    + "ORDER BY stocks.expiry_date ASC \n"
//                                    + "LIMIT 1;");
//                            if (newStockSet.next()) {
//                                String newStockId = newStockSet.getString("stocks.id");
//                                int newQty = newStockSet.getInt("quantity");
//                                if (newQty > 50) {
//                                    newQty = 50;
//                                }
//                                statement.executeUpdate("UPDATE shelves SET `quantity` = '" + newQty + "',"
//                                        + " `stocks_id` = '" + newStockId + "' WHERE stocks_id = '" + stock_id + "'  ");
//
//                                statement.executeUpdate("UPDATE stocks SET `quantity` = `quantity` - '" + newQty + "',"
//                                        + " WHERE id = '" + newStockId + "'  ");
//                                System.out.println("Reshelving Completed");
//
//                            }
//                        } else if (sumQty < neededQty) {
//                            ResultSet newStockSet = statement.executeQuery("SELECT * FROM `stocks` WHERE `id` = '" + stock_id + "'");
//                            statement.executeUpdate("UPDATE stocks SET quantity = '" + 0 + "' WHERE stocks.id = '" + stock_id + "'");
//                            if (newStockSet.next()) {
//                                String newQty = newStockSet.getString("quantity");
//                                statement.executeUpdate("UPDATE shelves SET `quantity` = '" + newQty + "',"
//                                        + " WHERE stocks_id = '" + stock_id + "'  ");
//
//                                statement.executeUpdate("UPDATE stocks SET `quantity` = `quantity` - '" + newQty + "',"
//                                        + " WHERE id = '" + stock_id + "'  ");
//                                System.out.println("Reshelving Completed");
//
//                            }
//                        }
//
//                    }
//
//                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void reprintBill() {
        System.out.println("--- Reprint Bill ---");
        System.out.print("Enter Bill ID to reprint: ");
        int billId = parseIntInput();
        Bill bill = billService.getBillById(billId);
        if (bill != null) {
            billService.printBill(billId);
        } else {
            System.out.println("Bill not found with ID: " + billId);
        }
    }

    public void viewBills() {
        List<Bill> bills = billService.getAllBills();
        if (bills.isEmpty()) {
            System.out.println("No bills found.");
            return;
        }
        displayBills(bills);
    }

    public void viewTodayBills() {
        List<Bill> bills = billService.getTodayBills();
        if (bills.isEmpty()) {
            System.out.println("No bills found for today.");
            return;
        }
        displayBills(bills);
    }

    private void displayBills(List<Bill> bills) {
        for (Bill bill : bills) {
            System.out.println("\nBill ID: " + bill.getId());
            String customerName = bill.getCustomerId() != null
                    ? customerService.getCustomerById(bill.getCustomerId()).getName()
                    : "Walk-in Customer";
            System.out.println("Customer: " + customerName);
            System.out.println("Date: " + bill.getBillDate());
            System.out.println("Total Amount: " + bill.getTotal());
            System.out.println("Items:");
            for (BillItem item : bill.getItems()) {
                System.out.println("  - " + item.getProductName()
                        + " (Qty: " + item.getQuantity()
                        + ", Price: " + item.getPrice()
                        + ", Total: " + (item.getQuantity() * item.getPrice()) + ")");
            }
            System.out.println("----------------------------------------");
        }
    }

    /**
     * Retrieves bill details by ID.
     *
     * @param billId ID of the bill.
     * @return Bill object or null if not found.
     */
    public Bill getBillById(int billId) {
        return billService.billDao.getBillById(billId);
    }

    /**
     * Parses integer input with basic validation.
     *
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
     *
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
