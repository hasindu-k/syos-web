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
        if (customerIdParam != null && !customerIdParam.isEmpty()) {
            customerId = Integer.parseInt(customerIdParam);
        }

        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");

        List<BillItem> items = new ArrayList<>();
        double subTotal = 0;
        int totalQty = 0;

        for (int i = 0; i < productIds.length; i++) {
            int pid = Integer.parseInt(productIds[i]);
            int qty = Integer.parseInt(quantities[i]);

            model.Product product = productService.getProductById(pid);
            if (product == null) continue;

            double price = product.getPrice();
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

        // Handle discount and payment
        String discountType = request.getParameter("discountType"); // "PERCENTAGE"
        String discountValueParam = request.getParameter("discountValue");
        String receivedAmountParam = request.getParameter("receivedAmount");

        double discountValue = 0.0; // percentage value (e.g., 10)
        double discountAmount = 0.0;
        double receivedAmount = subTotal;

        if (discountValueParam != null && !discountValueParam.isEmpty()) {
            discountValue = Double.parseDouble(discountValueParam);
            discountAmount = subTotal * (discountValue / 100.0); // convert to absolute discount
        }

        double total = subTotal - discountAmount;

        if (receivedAmountParam != null && !receivedAmountParam.isEmpty()) {
            receivedAmount = Double.parseDouble(receivedAmountParam);
        }

        double changeReturn = receivedAmount - total;

        // Pass original % value to controller, since it stores discountType + discountValue
        int billId = billController.generateBillWeb(customerId, items, discountType, discountValue, receivedAmount);

        if (billId > 0) {
            model.Bill bill = billController.getBillById(billId);
            request.setAttribute("billId", billId);
            request.setAttribute("bill", bill);
            request.setAttribute("changeReturn", changeReturn);
            request.setAttribute("customerName", (bill.getCustomerId() != null)
                    ? customerService.getCustomerById(bill.getCustomerId()).getName()
                    : "Walk-in Customer");
            request.getRequestDispatcher("/billing-success.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Failed to generate bill.");
            doGet(request, response);
        }
    }

}
