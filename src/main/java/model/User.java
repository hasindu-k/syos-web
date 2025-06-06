// src/model/User.java
package model;

/**
 * Represents a User in the system.
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String role; // Admin, Manager, Cashier
    private String type; // Staff, Customer, Supplier
    
    public User() {
        
    }

    public User(String username, String password, String role, String type) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.type = type;
    }

    // Getters and Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
