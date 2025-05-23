// src/dao/UserDaoProxy.java
package dao;

import java.util.List;

import model.User;

/**
 * Proxy Pattern.
 * Adds additional functionalities like logging without modifying the original UserDao.
 */
public class UserDaoProxy implements UserDaoInterface {
    private UserDao userDao;

    public UserDaoProxy(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public String authenticate(String username, String password) {
        System.out.println("Authenticating user: " + username);
        String role = userDao.authenticateDirect(username, password);
        if (role != null) {
            System.out.println("Authentication successful for user: " + username);
        } else {
            System.out.println("Authentication failed for user: " + username);
        }
        return role;
    }

    @Override
    public void addPerson(String username, String password, String role, String type) {
        System.out.println("Adding user: " + username);
        userDao.addPersonDirect(new model.User(username, password, role, type));
        System.out.println("User added: " + username);
    }

    @Override
    public void listPeopleByType(String type) {
        System.out.println("Listing people by type: " + type);
        userDao.listPeopleByTypeDirect(type);
    }

	@Override
	public List<User> getAllUsers() {
	    System.out.println("Proxy: Fetching all users...");
	    return userDao.getAllUsersDirect();
	}

	@Override
	public boolean updateUserRole(int userId, String newRole) {
		System.out.println("Updating User role...");
		return userDao.updateUserRole( userId,  newRole);
	}
}
