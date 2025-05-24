// src/dao/ReportDao.java
package dao;

import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object for generating reports.
 */
public class ReportDao {
    /**
     * Retrieves total sales for a given day.
     * @param date Date in YYYY-MM-DD format.
     * @return List of maps containing report data.
     */
    public List<Map<String, Object>> getTotalSaleForDay(String date) {
        String query = "SELECT p.id AS product_Code, p.name AS product_name, " +
                "SUM(si.quantity) AS total_quantity, " +
                "SUM(si.price * si.quantity) AS total_revenue " +
                "FROM bill_items si " +
                "JOIN products p ON si.product_id = p.id " +
                "JOIN bills b ON si.bill_id = b.id " +
                "WHERE DATE(b.bill_date) = ? " +
                "GROUP BY p.id, p.name";
        return executeQueryWithDate(query, date);
    }
    /**
     * Retrieves reshelved items for a given day.
     * @param date Date in YYYY-MM-DD format.
     * @return List of maps containing report data.
     */
    public List<Map<String, Object>> getReshelvedItems(String date) {
        String query = "SELECT products.id AS `Product code`,products.`name` AS "
                + "`Product Name`, shelves.quantity AS `Remaining Quantity`,"
                + "(50 - shelves.quantity) AS `Needed Quantity`  FROM shelves"
                + " INNER JOIN stocks ON stocks.id = shelves.stocks_id INNER "
                + "JOIN products ON products.id = stocks.products_id WHERE shelves.quantity < 50";
        return executeQuery(query);
    }

    /**
     * Retrieves products that are below reorder levels.
     * @return List of maps containing report data.
     */
    public List<Map<String, Object>> getReorderLevels() {
        String query = "SELECT p.id AS product_id, p.name AS product_name, " +
                "SUM(s.quantity) AS current_stock " +
                "FROM stocks s " +
                "JOIN products p ON s.products_id = p.id " +
                "GROUP BY p.id, p.name " +
                "HAVING current_stock < 50";
        return executeQuery(query);
    }

    /**
     * Retrieves stock batch-wise report.
     * @return List of maps containing report data.
     */
    public List<Map<String, Object>> getStockBatchWise() {
        String query = "SELECT s.id AS batch_id, p.id AS product_id, p.name AS product_name, " +
                "s.quantity, s.cost, s.price,s.expiry_date " +
                "FROM stocks s " +
                "JOIN products p ON s.products_id = p.id " +
                "ORDER BY s.expiry_date ASC, s.created_at ASC";
        return executeQuery(query);
    }

    /**
     * Retrieves bill report.
     * @return List of maps containing report data.
     */
    public List<Map<String, Object>> getBillReport() {
        String query = "SELECT b.id AS bill_id, c.name AS customer_name, " +
                "SUM(bi.quantity) AS total_items, " +
                "SUM(bi.price * bi.quantity) AS total_amount, " +
                "b.bill_date AS transaction_date " +
                "FROM bills b " +
                "LEFT JOIN customers c ON b.customer_id = c.id " +
                "JOIN bill_items bi ON b.id = bi.bill_id " +
                "GROUP BY b.id, c.name, b.bill_date";
        return executeQuery(query);
    }

    /**
     * Executes a query with a date parameter.
     * @param query SQL query.
     * @param date Date parameter.
     * @return List of maps containing query results.
     */
   private List<Map<String, Object>> executeQueryWithDate(String query, String date) {
    List<Map<String, Object>> results = new ArrayList<>();
    try (Connection conn = DBConnection.INSTANCE.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, date);
        try (ResultSet rs = stmt.executeQuery()) {
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            // Add column names as the first row
            Map<String, Object> headerRow = new LinkedHashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                headerRow.put(meta.getColumnLabel(i), meta.getColumnLabel(i)); // Column names as keys and values
            }
            results.add(headerRow);

            // Add actual data rows
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(meta.getColumnLabel(i), rs.getObject(i));
                }
                results.add(row);
            }
        }
    } catch (SQLException e) {
        System.out.println("Report Query Error: " + e.getMessage());
    }
    return results;
}


    /**
     * Executes a query without parameters.
     * @param query SQL query.
     * @return List of maps containing query results.
     */
   private List<Map<String, Object>> executeQuery(String query) {
    List<Map<String, Object>> results = new ArrayList<>();
    try (Connection conn = DBConnection.INSTANCE.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();

        // Add column names as the first row
        Map<String, Object> headerRow = new LinkedHashMap<>();
        for (int i = 1; i <= columnCount; i++) {
            headerRow.put(meta.getColumnLabel(i), meta.getColumnLabel(i)); // Store column names as both key and value
        }
        results.add(headerRow);

        // Add actual data rows
        while (rs.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(meta.getColumnLabel(i), rs.getObject(i));
            }
            results.add(row);
        }
    } catch (SQLException e) {
        System.out.println("Report Query Error: " + e.getMessage());
    }
    return results;
}

}
