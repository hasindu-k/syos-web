package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Customer;
import service.AuthService;
import service.CustomerService;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private AuthService authService;
    private CustomerService customerService;

    @Override
    public void init() {
        authService = new AuthService();
        customerService = new CustomerService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Collect form data
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        try {
            // Register user account
            boolean userCreated = authService.registerUser(username, password, role);

            if (!userCreated) {
                throw new IllegalArgumentException("Registration failed. Username may already exist.");
            }

            // If role is customer, create customer record
            if ("customer".equalsIgnoreCase(role)) {
            	customerService.registerCustomer(fullName, email, phone);

            }

            request.setAttribute("success", "Registration successful. You can now login.");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
        }

        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
}
