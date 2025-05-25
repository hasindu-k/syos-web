package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Bill;
import service.BillService;
import service.CustomerService;

import java.io.IOException;
import java.util.List;

@WebServlet("/my-orders")
public class MyOrdersServlet extends HttpServlet {

    private BillService billService;
    private CustomerService customerService;

    @Override
    public void init() {
        billService = new BillService();
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
        Integer customerId = customerService.getCustomerIdByUsername(username);

        if (customerId == null) {
            request.setAttribute("error", "Unable to retrieve your orders. Please try again.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }
        
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");
        String paymentType = request.getParameter("paymentType");
        String paymentStatus = request.getParameter("paymentStatus");

        List<Bill> orders = billService.getFilteredBillsByCustomer(customerId, fromDate, toDate, paymentType, paymentStatus);


//        List<Bill> orders = billService.getBillsByCustomerId(customerId);
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/my-orders.jsp").forward(request, response);
    }
}
