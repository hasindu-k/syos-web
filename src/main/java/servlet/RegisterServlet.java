package servlet;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import service.AuthService;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private AuthService authService;

    @Override
    public void init() {
    	authService = new AuthService(); // Ensure this class exists and supports user creation
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/auth-pages/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        boolean success = authService.registerUser(username, password, role);

        if (success) {
            request.setAttribute("success", "User registered successfully. You can now login.");
        } else {
            request.setAttribute("error", "Registration failed. Username may already exist.");
        }

        request.getRequestDispatcher("/auth-pages/register.jsp").forward(request, response);
    }
}
