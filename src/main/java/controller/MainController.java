// src/controller/MainController.java
package controller;

import controller.MenuController;
import service.AuthService;
import util.MenuNavigator;

/**
 * Main Controller to manage authentication and navigate to menus.
 */
public class MainController {
    private AuthService authService;

    public MainController() {
        this.authService = new AuthService();
    }

    /**
     * Starts the POS system.
     */
    public void start() {
        System.out.println("Welcome to Synex Outlet Store POS System");
        boolean authenticated = false;
        String role = null;

        while (!authenticated) {
            System.out.print("Username: ");
            String username = MenuNavigator.getInput();
            System.out.print("Password: ");
            String password = MenuNavigator.getInput();

            role = authService.authenticate(username, password);
            if (role != null) {
                System.out.println("Authentication successful! Role: " + role);
                authenticated = true;
            } else {
                System.out.println("Invalid credentials. Please try again.");
            }
        }

        // Navigate to appropriate menu based on role
        MenuController menuController = new MenuController(role);
        menuController.showMenu();
    }
}
