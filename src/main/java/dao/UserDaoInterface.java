// src/dao/UserDaoInterface.java
package dao;

import java.util.List;

import model.User;

/**
 * Interface for UserDao to implement Proxy Pattern.
 */
public interface UserDaoInterface {
    String authenticate(String username, String password);
    void addPerson(String username, String password, String role, String type);
    void listPeopleByType(String type);
    List<User> getAllUsers();
    boolean updateUserRole(int userId, String newRole);
    // Add other user-related methods as needed
}
