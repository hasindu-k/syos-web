// src/dao/UserDao.java
package dao;

import model.User;
import util.DBConnection;

import java.sql.*;

/**
 * Proxy Pattern.
 * Acts as a proxy for user-related database operations with added logging.
 */
public class UserDao implements UserDaoInterface {
    private UserDaoInterface userDaoProxy;

    public UserDao() {
        this.userDaoProxy = new UserDaoProxy(this);
    }

    @Override
    public String authenticate(String username, String password) {
        return userDaoProxy.authenticate(username, password);
    }

    @Override
    public void addPerson(String username, String password, String role, String type) {
        userDaoProxy.addPerson(username, password, role, type);
    }

    @Override
    public void listPeopleByType(String type) {
        userDaoProxy.listPeopleByType(type);
    }

    // Direct database operations
    public String authenticateDirect(String username, String password) {
        String query = "SELECT role FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role");
                }
            }
        } catch (SQLException e) {
            System.out.println("Authentication Error: " + e.getMessage());
        }
        return null;
    }

    public void addPersonDirect(User user) {
        String query = "INSERT INTO users (username, password, role, type) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getType());
            stmt.executeUpdate();
            System.out.println("User added successfully: " + user.getUsername());
        } catch (SQLException e) {
            System.out.println("Add User Error: " + e.getMessage());
        }
    }

    public void listPeopleByTypeDirect(String type) {
        String query = "SELECT * FROM users WHERE type = ?";
        try (Connection conn = DBConnection.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, type);
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("=== " + type + " List ===");
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id") +
                            ", Username: " + rs.getString("username") +
                            ", Role: " + rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            System.out.println("List People Error: " + e.getMessage());
        }
    }
}
