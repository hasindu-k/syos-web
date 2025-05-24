// src/dao/StockDao.java
package dao;

import model.Stock;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object for Stock operations.
 */
public class StockDao {
    /**
     * Adds a new stock entry.
     * @param stock Stock object.
     * @return Generated Stock ID or -1 if failed.
     */
    public int addStock(Stock stock) {
    	String query = "INSERT INTO stocks (products_id, quantity, warehouse_id, expiry_date, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

        	stmt.setInt(1, stock.getProductId());
        	stmt.setInt(2, stock.getQuantity());
        	stmt.setInt(3, stock.getWarehouseId());
        	stmt.setDate(4, new java.sql.Date(stock.getExpiryDate().getTime()));
        	stmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
        	stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Creating stock failed, no rows affected.");
                return -1;
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
                else {
                    System.out.println("Creating stock failed, no ID obtained.");
                    return -1;
                }
            }
        } catch (SQLException e) {
            System.out.println("Add Stock Error: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Retrieves all stocks with product details.
     * @return List of Stock objects.
     */
    public List<Stock> getAllStocksWithProductDetails() {
        String query = "SELECT * FROM stocks ORDER BY expiry_date ASC, created_at ASC";
        List<Stock> stocks = new ArrayList<>();
        try (Connection conn = DBConnection.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
            	Stock stock = new Stock(
            		    rs.getInt("products_id"),
            		    rs.getInt("quantity"),
            		    rs.getInt("warehouse_id"),
            		    rs.getDate("expiry_date")
            		);

                stock.setId(rs.getInt("id"));
                stocks.add(stock);
            }
        } catch (SQLException e) {
            System.out.println("Get All Stocks Error: " + e.getMessage());
        }
        return stocks;
    }

    /**
     * Reduces stock quantity.
     * @param stockId ID of the stock.
     * @param quantity Quantity to reduce.
     * @return True if successful, else False.
     */
    public boolean reduceStock(int stockId, int quantity) {
        String query = "UPDATE stocks SET quantity = quantity - ? WHERE id = ?";
        try (Connection conn = DBConnection.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, quantity);
            stmt.setInt(2, stockId);
            stmt.setInt(3, quantity);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Reduce Stock Error: " + e.getMessage());
            return false;
        }
    }
    

    /**
     * Retrieves stocks for a specific product ID.
     * @param productId The ID of the product to get stocks for
     * @return List of Stock objects for the given product ID
     */
    public List<Stock> getStocksByProductId(int productId) {
        String query = "SELECT * FROM stocks WHERE products_id = ? ORDER BY expiry_date ASC, created_at ASC";
        List<Stock> stocks = new ArrayList<>();
        try (Connection conn = DBConnection.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                	Stock stock = new Stock(
                		    rs.getInt("products_id"),
                		    rs.getInt("quantity"),
                		    rs.getInt("warehouse_id"),
                		    rs.getDate("expiry_date")
                		);

                    stock.setId(rs.getInt("id"));
                    stocks.add(stock);
                }
            }
        } catch (SQLException e) {
            System.out.println("Get Stocks By Product ID Error: " + e.getMessage());
        }
        return stocks;
    }
}
