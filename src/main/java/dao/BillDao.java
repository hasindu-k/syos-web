// src/dao/BillDao.java
package dao;

import model.Bill;
import model.BillItem;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Bill operations.
 */
public class BillDao {
    /**
     * Creates a new bill in the database.
     * 
     * @param bill Bill object containing bill details.
     * @return Generated Bill ID or -1 if failed.
     */
    public int createBill(Bill bill) {
        String query = "INSERT INTO bills (customer_id, total_qty, sub_total, discount_type, discount_value, " +
                "total, received_amount, change_return, payment_type, payment_status, bill_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.INSTANCE.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            if (bill.getCustomerId() != null) {
                stmt.setInt(1, bill.getCustomerId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setInt(2, bill.getTotalQty());
            stmt.setDouble(3, bill.getSubTotal());
            stmt.setString(4, bill.getDiscountType());
            stmt.setDouble(5, bill.getDiscountValue());
            stmt.setDouble(6, bill.getTotal());
            stmt.setDouble(7, bill.getReceivedAmount());
            stmt.setDouble(8, bill.getChangeReturn());
            stmt.setString(9, bill.getPaymentType());
            stmt.setString(10, bill.getPaymentStatus());
            stmt.setTimestamp(11, new Timestamp(bill.getBillDate().getTime()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Creating bill failed, no rows affected.");
                return -1;
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    System.out.println("Creating bill failed, no ID obtained.");
                    return -1;
                }
            }
        } catch (SQLException e) {
            System.out.println("Bill Creation Error: " + e.getMessage());
            return -1;

        }
    }

    /**
     * Adds items to a bill.
     * 
     * @param billId ID of the bill.
     * @param items  List of BillItem objects.
     * @return True if successful, else False.
     */
    public boolean addBillItems(int billId, List<BillItem> items) {
        String query = "INSERT INTO bill_items (bill_id, product_id, quantity, price, total) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.INSTANCE.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            for (BillItem item : items) {
                stmt.setInt(1, billId);
                stmt.setInt(2, item.getProductId());
                // stmt.setString(3, item.getProductName());
                stmt.setInt(3, item.getQuantity());
                stmt.setDouble(4, item.getPrice());
                stmt.setDouble(5, item.getTotalPrice());
                stmt.addBatch();
            }
            stmt.executeBatch();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            // System.out.println("Adding Bill Items Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a bill by its ID.
     * 
     * @param billId ID of the bill.
     * @return Bill object if found, else null.
     */
    public Bill getBillById(int billId) {
        String billQuery = "SELECT * FROM bills WHERE id = ?";
        String itemsQuery = "SELECT * FROM bill_items INNER JOIN products ON"
                + " products.id = bill_items.product_id WHERE bill_id = ?";
        Bill bill = null;

        try (Connection conn = DBConnection.INSTANCE.getConnection();
                PreparedStatement billStmt = conn.prepareStatement(billQuery);
                PreparedStatement itemsStmt = conn.prepareStatement(itemsQuery)) {

            // Fetch bill details
            billStmt.setInt(1, billId);
            try (ResultSet billRs = billStmt.executeQuery()) {
                if (billRs.next()) {
                    bill = new Bill(
                            billRs.getInt("customer_id") != 0 ? billRs.getInt("customer_id") : null,
                            billRs.getInt("total_qty"),
                            billRs.getDouble("sub_total"),
                            billRs.getString("discount_type"),
                            billRs.getDouble("discount_value"),
                            // billRs.getString("tax_type"),
                            // billRs.getDouble("tax_value"),
                            billRs.getDouble("total"),
                            billRs.getDouble("received_amount"),
                            billRs.getDouble("change_return"),
                            billRs.getString("payment_type"),
                            billRs.getString("payment_status"),
                            new ArrayList<>());
                } else {
                    System.out.println("Bill not found with ID: " + billId);
                    return null;
                }
            }

            // Fetch bill items
            itemsStmt.setInt(1, billId);
            try (ResultSet itemsRs = itemsStmt.executeQuery()) {
                while (itemsRs.next()) {
                    BillItem item = new BillItem(
                            itemsRs.getInt("product_id"),
                            itemsRs.getString("name"),
                            itemsRs.getInt("quantity"),
                            itemsRs.getDouble("price"));
                    bill.getItems().add(item);
                }
            }

        } catch (SQLException e) {
            System.out.println("Retrieving Bill Error: " + e.getMessage());
        }

        return bill;
    }

    /**
     * Retrieves all bills from the database.
     * 
     * @return List of all bills.
     */
    public List<Bill> getAllBills() {
        String billQuery = "SELECT * FROM bills ORDER BY bill_date DESC";
        String itemsQuery = "SELECT * FROM bill_items WHERE bill_id = ?";
        List<Bill> bills = new ArrayList<>();

        try (Connection conn = DBConnection.INSTANCE.getConnection();
                PreparedStatement billStmt = conn.prepareStatement(billQuery)) {

            // Fetch all bills
            try (ResultSet billRs = billStmt.executeQuery()) {
                while (billRs.next()) {
                    Bill bill = new Bill(
                            billRs.getInt("customer_id") != 0 ? billRs.getInt("customer_id") : null,
                            billRs.getInt("total_qty"),
                            billRs.getDouble("sub_total"),
                            billRs.getString("discount_type"),
                            billRs.getDouble("discount_value"),
                            // billRs.getString("tax_type"),
                            // billRs.getDouble("tax_value"),
                            billRs.getDouble("total"),
                            billRs.getDouble("received_amount"),
                            billRs.getDouble("change_return"),
                            billRs.getString("payment_type"),
                            billRs.getString("payment_status"),
                            new ArrayList<>());
                    bill.setBillDate(billRs.getTimestamp("bill_date"));

                    // Fetch items for each bill
                    try (PreparedStatement itemsStmt = conn.prepareStatement(itemsQuery)) {
                        itemsStmt.setInt(1, billRs.getInt("id"));
                        try (ResultSet itemsRs = itemsStmt.executeQuery()) {
                            while (itemsRs.next()) {
                                BillItem item = new BillItem(
                                        itemsRs.getInt("products_id"),
                                        itemsRs.getString("product_name"),
                                        itemsRs.getInt("quantity"),
                                        itemsRs.getDouble("price"));
                                bill.getItems().add(item);
                            }
                        }
                    }
                    bills.add(bill);
                }
            }
        } catch (SQLException e) {
            System.out.println("Retrieving All Bills Error: " + e.getMessage());
        }

        return bills;
    }

    /**
     * Retrieves all bills from today.
     * 
     * @return List of today's bills.
     */
    public List<Bill> getTodayBills() {
        String billQuery = "SELECT * FROM bills WHERE DATE(bill_date) = CURRENT_DATE ORDER BY bill_date DESC";
        String itemsQuery = "SELECT * FROM bill_items INNER JOIN products ON "
                + "products.id = bill_items.product_id WHERE bill_id = ?";
        List<Bill> bills = new ArrayList<>();

        try (Connection conn = DBConnection.INSTANCE.getConnection();
                PreparedStatement billStmt = conn.prepareStatement(billQuery)) {

            // Fetch today's bills
            try (ResultSet billRs = billStmt.executeQuery()) {
                while (billRs.next()) {
                    Bill bill = new Bill(
                            billRs.getInt("customer_id") != 0 ? billRs.getInt("customer_id") : null,
                            billRs.getInt("total_qty"),
                            billRs.getDouble("sub_total"),
                            billRs.getString("discount_type"),
                            billRs.getDouble("discount_value"),
                            // billRs.getString("tax_type"),
                            // billRs.getDouble("tax_value"),
                            billRs.getDouble("total"),
                            billRs.getDouble("received_amount"),
                            billRs.getDouble("change_return"),
                            billRs.getString("payment_type"),
                            billRs.getString("payment_status"),
                            new ArrayList<>());
                    bill.setBillDate(billRs.getTimestamp("bill_date"));

                    // Fetch items for each bill
                    try (PreparedStatement itemsStmt = conn.prepareStatement(itemsQuery)) {
                        itemsStmt.setInt(1, billRs.getInt("id"));
                        try (ResultSet itemsRs = itemsStmt.executeQuery()) {
                            while (itemsRs.next()) {
                                BillItem item = new BillItem(
                                        itemsRs.getInt("product_id"),
                                        itemsRs.getString("products.name"),
                                        itemsRs.getInt("bill_items.quantity"),
                                        itemsRs.getDouble("bill_items.price"));
                                bill.getItems().add(item);
                            }
                        }
                    }
                    bills.add(bill);
                }
            }
        } catch (SQLException e) {
            System.out.println("Retrieving Today's Bills Error: " + e.getMessage());
        }

        return bills;
    }
    
    public List<Bill> getBillsByCustomerId(int customerId) {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills WHERE customer_id = ? ORDER BY bill_date DESC";

        try (PreparedStatement stmt = DBConnection.INSTANCE.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Bill bill = new Bill();
                bill.setId(rs.getInt("id"));
                bill.setBillDate(rs.getDate("bill_date"));
                bill.setTotalQty(rs.getInt("total_qty"));
                bill.setTotal(rs.getDouble("total"));
                bill.setPaymentType(rs.getString("payment_type"));
                bill.setPaymentStatus(rs.getString("payment_status"));
                bills.add(bill);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bills;
    }

    public List<Bill> getFilteredBillsByCustomer(int customerId, String fromDate, String toDate, String paymentType, String paymentStatus) {
        List<Bill> bills = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM bills WHERE customer_id = ?");
        List<Object> params = new ArrayList<>();
        params.add(customerId);

        if (fromDate != null && !fromDate.isBlank()) {
            query.append(" AND bill_date >= ?");
            params.add(fromDate);
        }
        if (toDate != null && !toDate.isBlank()) {
            query.append(" AND bill_date <= ?");
            params.add(toDate);
        }
        if (paymentType != null && !paymentType.isBlank()) {
            query.append(" AND payment_type = ?");
            params.add(paymentType);
        }
        if (paymentStatus != null && !paymentStatus.isBlank()) {
            query.append(" AND payment_status = ?");
            params.add(paymentStatus);
        }

        query.append(" ORDER BY bill_date DESC");

        try (PreparedStatement stmt = DBConnection.INSTANCE.getConnection().prepareStatement(query.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Bill bill = new Bill();
                bill.setId(rs.getInt("id"));
                bill.setBillDate(rs.getDate("bill_date"));
                bill.setTotalQty(rs.getInt("total_qty"));
                bill.setTotal(rs.getDouble("total"));
                bill.setPaymentType(rs.getString("payment_type"));
                bill.setPaymentStatus(rs.getString("payment_status"));
                bills.add(bill);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bills;
    }

}
