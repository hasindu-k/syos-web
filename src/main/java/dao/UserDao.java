// src/dao/UserDao.java
package dao;

import model.User;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

	@Override
	public List<User> getAllUsers() {
	    return userDaoProxy.getAllUsers(); // delegate to proxy
	}
	
	public List<User> getAllUsersDirect() {
	    List<User> users = new ArrayList<>();
	    String query = "SELECT * FROM users";
	    try (Connection conn = DBConnection.INSTANCE.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            User user = new User();
	            user.setId(rs.getInt("id"));
	            user.setUsername(rs.getString("username"));
	            user.setPassword(rs.getString("password")); // Optional: omit if not needed
	            user.setRole(rs.getString("role"));
	            user.setType(rs.getString("type"));
	            users.add(user);
	        }
	    } catch (SQLException e) {
	        System.out.println("Get All Users Error: " + e.getMessage());
	    }
	    return users;
	}
	
	public boolean updateUserRole(int userId, String newRole) {
	    String query = "UPDATE users SET role = ? WHERE id = ?";
	    try (Connection conn = DBConnection.INSTANCE.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setString(1, newRole);
	        stmt.setInt(2, userId);
	        return stmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        System.out.println("Update Role Error: " + e.getMessage());
	        return false;
	    }
	}

}
