// src/service/AuthService.java
package service;

import java.util.List;

import dao.UserDao;
import dao.UserDaoInterface;
import model.User;

/**
 * Service class for Authentication.
 */
public class AuthService {
    private UserDaoInterface userDao;

    public AuthService() {
        userDao = new UserDao(); // UserDao acts as a proxy
    }

    /**
     * Authenticates a user.
     * @param username Username.
     * @param password Password.
     * @return Role if authenticated, else null.
     */
    public String authenticate(String username, String password) {
        return userDao.authenticate(username, password);
    }
    
    public boolean registerUser(String username, String password, String role) {
        try {
            userDao.addPerson(username, password, role, "staff");
            return true;
        } catch (Exception e) {
            // Log exception (optional)
            return false;
        }
    }
    
    public List<User> getAllUsers() {
        return new UserDao().getAllUsers();
    }
    
    public boolean updateUserRole(int userId, String newRole) {
        return userDao.updateUserRole(userId, newRole);
    }
    
    
}
