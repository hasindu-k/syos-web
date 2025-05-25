// src/service/BillService.java
package service;

import dao.BillDao;
import model.Bill;
import model.BillItem;
import observer.StockAlertService;
import observer.StockSubject;
import observer.Subject;
import java.util.List;
import java.sql.ResultSet;
import util.DBConnection;
/**
 * Service class for Bill operations.
 */
public class BillService {
    public BillDao billDao;
    public Subject stockAlertService;

    public BillService() {
        this.billDao = new BillDao();
        this.stockAlertService = new StockSubject();
        // Register the stock alert service as an observer
        ((StockSubject) stockAlertService).registerObserver(new StockAlertService());
    }
    /**
     * Retrieves a bill by its ID.
     * @param billId ID of the bill.
     * @return Bill object if found, else null.
     */
    public Bill getBillById(int billId) {
        return billDao.getBillById(billId);
    }

    /**
     * Generates a bill and notifies observers.
     * @param bill Bill object.
     * @param printFlag If true, prints the bill.
     * @return Bill ID if successful, else -1.
     */
    public int generateBill(Bill bill, boolean printFlag) {
        int billId = billDao.createBill(bill);
        if (billId > 0) {
            boolean itemsAdded = billDao.addBillItems(billId, bill.getItems());
            if (itemsAdded) {
                stockAlertService.update("Bill ID " + billId + " generated.");
                if (printFlag) {
                    printBill(billId);
                }
            } else {
                System.out.println("Error: Failed to add bill items.");
                return -1;
            }
        }
        return billId;
    }

    /**
     * Prints a bill.
     * @param billId ID of the bill to print.
     */
    public void printBill(int billId) {
        Bill bill = billDao.getBillById(billId);
//        try {
//            ResultSet billSet = DBConnection.INSTANCE.getConnection().createStatement().executeQuery("SELECT * FROM ");
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
        if (bill != null) {
            System.out.println("========= SYOS POS BILL =========");
            System.out.println("Bill ID: " + billId);
            System.out.println("Date: " + bill.getBillDate());
            System.out.println("Customer ID: " + (bill.getCustomerId() != null ? bill.getCustomerId() : "Walk-In"));
            System.out.println("----------------------------------");
            System.out.println("Product \t Qty \t Price \t Total");
            for (BillItem item : bill.getItems()) {
                System.out.println(item.getProductName() + " \t " + item.getQuantity() + " \t " +
                        item.getPrice() + " \t " + item.getTotalPrice());
            }
            System.out.println("----------------------------------");
            System.out.println("Subtotal: " + bill.getSubTotal());
            System.out.println("Discount: " + bill.getDiscountValue() + " (" + bill.getDiscountType() + ")");
//            System.out.println("Tax: " + bill.getTaxValue() + " (" + bill.getTaxType() + ")");
            System.out.println("Total: " + bill.getTotal());
            System.out.println("Received: " + bill.getReceivedAmount());
            System.out.println("Change: " + bill.getChangeReturn());
            System.out.println("Payment Type: " + bill.getPaymentType());
            System.out.println("Payment Status: " + bill.getPaymentStatus());
            System.out.println("==================================");
        } else {
            System.out.println("Bill not found with ID: " + billId);
        }
    }

    /**
     * Retrieves all bills from the database.
     * @return List of all bills.
     */
    public List<Bill> getAllBills() {
        return billDao.getAllBills();
    }

    /**
     * Retrieves all bills from today.
     * @return List of today's bills.
     */
    public List<Bill> getTodayBills() {
        return billDao.getTodayBills();
    }
	
    public List<Bill> getBillsByCustomerId(int customerId) {
        return billDao.getBillsByCustomerId(customerId);
    }

    public List<Bill> getFilteredBillsByCustomer(int customerId, String fromDate, String toDate, String paymentType, String paymentStatus) {
        return billDao.getFilteredBillsByCustomer(customerId, fromDate, toDate, paymentType, paymentStatus);
    }

    
}
