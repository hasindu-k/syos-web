package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Customer;
import service.CustomerService;

import java.io.IOException;

@WebServlet("/account")
public class AccountServlet extends HttpServlet {

    private CustomerService customerService;

    @Override
    public void init() {
        customerService = new CustomerService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"customer".equalsIgnoreCase((String) session.getAttribute("role"))) {
            response.sendRedirect("login");
            return;
        }

        String username = (String) session.getAttribute("username");
        Customer customer = customerService.getCustomerByUsername(username);

        if (customer == null) {
            request.setAttribute("error", "Unable to load account information.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        request.setAttribute("customer", customer);
        request.getRequestDispatcher("/account.jsp").forward(request, response);
    }
}
