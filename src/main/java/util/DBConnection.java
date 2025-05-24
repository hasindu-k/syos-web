// src/util/DBConnection.java
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton Pattern.
 * Manages a single database connection instance.
 */
public enum DBConnection {
    INSTANCE;

    private static final String URL = "jdbc:mysql://localhost:3306/syos";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection connection;

    DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database Connected");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Failed to connect the database.");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            System.out.println("Failed to reconnect the database.");
            e.printStackTrace();
        }
        return connection;
    }
}
