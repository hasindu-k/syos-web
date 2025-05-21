// src/dao/UserDaoInterface.java
package dao;

/**
 * Interface for UserDao to implement Proxy Pattern.
 */
public interface UserDaoInterface {
    String authenticate(String username, String password);
    void addPerson(String username, String password, String role, String type);
    void listPeopleByType(String type);
    // Add other user-related methods as needed
}
