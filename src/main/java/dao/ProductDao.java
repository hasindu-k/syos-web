// src/dao/ProductDao.java
package dao;

import model.Product;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Product operations.
 */
public class ProductDao {

    /**
     * Adds a new product.
     *
     * @param product Product object.
     * @return Generated Product ID or -1 if failed.
     */
    public int addProduct(Product product) {
        String query = "INSERT INTO products (name, unit_id, category_id, supplier_id, status, warehouse_id, note, stock_alert, price) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.INSTANCE.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, product.getName());
            stmt.setInt(2, product.getUnitId());
            stmt.setInt(3, product.getCategoryId());
            stmt.setInt(4, product.getSupplierId());
            stmt.setString(5, product.getStatus());
            stmt.setInt(6, product.getWarehouseId());
            stmt.setString(7, product.getNote());
            stmt.setInt(8, product.getStockAlert());
            stmt.setDouble(9, product.getPrice());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Creating product failed, no rows affected.");
                return -1;
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    System.out.println("Creating product failed, no ID obtained.");
                    return -1;
                }
            }
        } catch (SQLException e) {
            System.out.println("Add Product Error: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Retrieves all products.
     *
     * @return List of Product objects.
     */
    public List<Product> getAllProducts() {
        String query = "SELECT * FROM products";
        List<Product> products = new ArrayList<>();
        try (Connection conn = DBConnection.INSTANCE.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getString("name"),
                        rs.getInt("unit_id"),
                        rs.getInt("category_id"),
                        rs.getString("status"),
                        rs.getInt("warehouse_id"),
                        rs.getString("note"),
                        rs.getInt("stock_alert"),
                        rs.getInt("supplier_id"),
                        rs.getDouble("price"));
                product.setId(rs.getInt("id"));
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Get All Products Error: " + e.getMessage());
        }
        return products;
    }

    /**
     * Retrieves a product by ID.
     *
     * @param productId ID of the product.
     * @return Product object if found, else null.
     */
    public Product getProductById(int productId) {
        String query = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = DBConnection.INSTANCE.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Product product = new Product(
                            rs.getString("name"),
                            rs.getInt("unit_id"),
                            rs.getInt("category_id"),
                            rs.getString("status"),
                            rs.getInt("warehouse_id"),
                            rs.getString("note"),
                            rs.getInt("stock_alert"),
                            rs.getInt("supplier_id"),
                            rs.getDouble("price"));
                    product.setId(rs.getInt("id"));
                    return product;
                }
            }
        } catch (SQLException e) {
            System.out.println("Get Product By ID Error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Updates an existing product.
     *
     * @param product Product object with updated details.
     * @return True if successful, else False.
     */
    public boolean updateProduct(Product product) {
        String query = "UPDATE products SET name = ?, unit_id = ?, category_id = ?, supplier_id = ?, "
                + "status = ?, warehouse_id = ?, note = ?, stock_alert = ?, price = ? WHERE id = ?";
        try (Connection conn = DBConnection.INSTANCE.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, product.getName());
            stmt.setInt(2, product.getUnitId());
            stmt.setInt(3, product.getCategoryId());
            stmt.setInt(4, product.getSupplierId());
            stmt.setString(5, product.getStatus());
            stmt.setInt(6, product.getWarehouseId());
            stmt.setString(7, product.getNote());
            stmt.setInt(8, product.getStockAlert());
            stmt.setDouble(9, product.getPrice());
            stmt.setInt(10,product.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Update Product Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a product by ID.
     *
     * @param productId ID of the product.
     * @return True if successful, else False.
     */
    public boolean deleteProduct(int productId) {
        String query = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DBConnection.INSTANCE.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Delete Product Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Searches for products by ID (exact match).
     *
     * @param query The search query (product ID as string)
     * @return List of matching products
     */
    public List<Product> searchProducts(String query) {
        String sqlQuery = "SELECT * FROM products WHERE id = ?";
        List<Product> products = new ArrayList<>();

        try (Connection conn = DBConnection.INSTANCE.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sqlQuery)) {

            stmt.setString(1, query);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                            rs.getString("name"),
                            rs.getInt("unit_id"),
                            rs.getInt("category_id"),
                            rs.getString("status"),
                            rs.getInt("warehouse_id"),
                            rs.getString("note"),
                            rs.getInt("stock_alert"),
                            rs.getInt("supplier_id"),
                            rs.getDouble("price"));
                    product.setId(rs.getInt("id"));
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            System.out.println("Search Products Error: " + e.getMessage());
        }
        return products;
    }
}
