package servlet;

import controller.BillController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.BillItem;
import model.Customer;
import service.CustomerService;
import service.ProductService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/billing")
public class BillingServlet extends HttpServlet {

    private BillController billController;

    @Override
    public void init() {
        billController = new BillController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Customer> customers = new CustomerService().getAllCustomers();
        request.setAttribute("customers", customers);
        request.getRequestDispatcher("/billing.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CustomerService customerService = new CustomerService();
        ProductService productService = new ProductService();

        String customerIdParam = request.getParameter("customerId");
        int customerId = -1;

        customerId = Integer.parseInt(customerIdParam);

        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");

        List<BillItem> items = new ArrayList<>();
        double subTotal = 0;
        int totalQty = 0;

        for (int i = 0; i < productIds.length; i++) {
            int pid = Integer.parseInt(productIds[i]);
            int qty = Integer.parseInt(quantities[i]);

            // Get real product
            model.Product product = productService.getProductById(pid);
            if (product == null)
                continue;

            double price = product.getPrice(); // assumes price is set
            String name = product.getName();

            items.add(new BillItem(pid, name, qty, price));
            subTotal += qty * price;
            totalQty += qty;
        }

        if (items.isEmpty()) {
            request.setAttribute("error", "No valid products selected.");
            doGet(request, response);
            return;
        }

        // 3. Submit to controller
        BillController billController = new BillController();
        int billId = billController.generateBillWeb(customerId, items, "", 0.0, subTotal);

        if (billId > 0) {
            response.sendRedirect("billing-success.jsp?billId=" + billId);
        } else {
            request.setAttribute("error", "Failed to generate bill.");
            response.sendRedirect("billing-error.jsp");
            doGet(request, response);
        }
    }

}
