// src/dao/CustomerDao.java
package dao;

import model.Customer;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Customer operations.
 */
public class CustomerDao {
    /**
     * Adds a new customer.
     * 
     * @param customer Customer object.
     * @return Generated Customer ID or -1 if failed.
     */
    public int addCustomer(Customer customer) {
        String query = "INSERT INTO customers (name, email, phone) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.INSTANCE.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmail());
            stmt.setString(3, customer.getPhone());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Creating customer failed, no rows affected.");
                return -1;
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    System.out.println("Creating customer failed, no ID obtained.");
                    return -1;
                }
            }
        } catch (SQLException e) {
            System.out.println("Add Customer Error: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Retrieves all customers.
     * 
     * @return List of Customer objects.
     */
    public List<Customer> getAllCustomers() {
        String query = "SELECT * FROM customers";
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = DBConnection.INSTANCE.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"));
                customer.setId(rs.getInt("id"));
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.out.println("Get All Customers Error: " + e.getMessage());
        }
        return customers;
    }

    /**
     * Retrieves a customer by ID.
     * 
     * @param customerId ID of the customer.
     * @return Customer object if found, else null.
     */
    public Customer getCustomerById(int customerId) {
        String query = "SELECT * FROM customers WHERE id = ?";
        try (Connection conn = DBConnection.INSTANCE.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Customer customer = new Customer(
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"));
                    customer.setId(rs.getInt("id"));
                    return customer;
                }
            }
        } catch (SQLException e) {
            System.out.println("Get Customer By ID Error: " + e.getMessage());
        }
        return null;
    }

    public boolean emailExists(String email) {
        String query = "SELECT 1 FROM customers WHERE email = ?";
        try (Connection conn = DBConnection.INSTANCE.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Check Email Error: " + e.getMessage());
            return false;
        }
    }

    public Integer getCustomerIdByUsername(String username) {
        String query = """
            SELECT c.id 
            FROM customers c
            JOIN users u ON c.user_id = u.id
            WHERE u.username = ?
            """;

        try (Connection conn = DBConnection.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public List<Customer> searchByName(String namePart) {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE name LIKE ?";
        try (PreparedStatement stmt = DBConnection.INSTANCE.getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + namePart + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Customer c = new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"));
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
