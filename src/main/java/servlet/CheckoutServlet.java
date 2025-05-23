package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Bill;
import model.BillItem;
import model.Product;
import service.BillService;
import service.ProductService;
import service.shelvesService;

import java.io.IOException;
import java.util.*;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private ProductService productService;
    private BillService billService;

    @Override
    public void init() {
        productService = new ProductService();
        billService = new BillService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("role") == null) {
            response.sendRedirect("login");
            return;
        }

        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            request.setAttribute("error", "Your cart is empty.");
            request.getRequestDispatcher("/cart.jsp").forward(request, response);
            return;
        }

        List<BillItem> items = new ArrayList<>();
        int totalQty = 0;
        double subTotal = 0.0;

        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();
            Product product = productService.getProductById(productId);
            double price = product.getPrice();
            double lineTotal = price * quantity;

            items.add(new BillItem(productId, product.getName(), quantity, price));
            totalQty += quantity;
            subTotal += lineTotal;
        }

        // Basic discount and payment settings (can be extended)
        String discountType = "";  // e.g., "FIXED", "PERCENTAGE"
        double discountValue = 0;
        double discount = 0;
        double total = subTotal - discount;
        double receivedAmount = total;
        double changeReturn = 0;

        // Retrieve customer info from session (optional)
        Integer customerId = null;
        String username = (String) session.getAttribute("username");
        if ("customer".equalsIgnoreCase((String) session.getAttribute("role"))) {
            // Map username to customerId if needed
            // You may enhance this with real customer lookup
            customerId = 1; // temporary placeholder (e.g., fetch by username)
        }

        Bill bill = new Bill(customerId, totalQty, subTotal, discountType, discountValue,
                total, receivedAmount, changeReturn, "Cash", "Paid", items);

        int billId = billService.generateBill(bill, true);

        if (billId > 0) {
            // Clear cart
            session.removeAttribute("cart");

            // Optional: update shelves/sales
            shelvesService shelfService = new shelvesService();
            shelfService.updateShelves(billId);
            shelfService.updateSales(billId);

            request.setAttribute("success", "Order placed successfully! Bill ID: " + billId);
        } else {
            request.setAttribute("error", "Failed to place order. Please try again.");
        }

        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }
}
