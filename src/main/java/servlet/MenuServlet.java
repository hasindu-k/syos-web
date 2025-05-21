package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/menu")
public class MenuServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get session and check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("role") == null) {
            response.sendRedirect("login");
            return;
        }

        String role = (String) session.getAttribute("role");

        // Forward to the appropriate menu JSP
        switch (role.toLowerCase()) {
            case "admin":
                request.getRequestDispatcher("/admin-menu.jsp").forward(request, response);
                break;
            case "cashier":
                request.getRequestDispatcher("/cashier-menu.jsp").forward(request, response);
                break;
            default:
                request.setAttribute("error", "Unknown user role: " + role);
                request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
