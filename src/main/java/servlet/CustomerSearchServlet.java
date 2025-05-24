// src/servlet/CustomerSearchServlet.java
package servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Customer;
import service.CustomerService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/search-customers")
public class CustomerSearchServlet extends HttpServlet {
    private CustomerService customerService;

    @Override
    public void init() {
        customerService = new CustomerService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String term = request.getParameter("term");
        List<Customer> results = customerService.searchCustomersByName(term);

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(results));
        out.flush();
    }
}
