package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Bill;
import model.BillItem;
import model.Product;
import model.Stock;
import service.BillService;
import service.CustomerService;
import service.ProductService;
import service.StockService;

import java.io.IOException;
import java.util.*;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private ProductService productService;
    private BillService billService;
    private CustomerService customerService;

    @Override
    public void init() {
        productService = new ProductService();
        billService = new BillService();
        customerService = new CustomerService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("role") == null) {
            response.sendRedirect("login");
            return;
        }

        String address = request.getParameter("address");
        String paymentType = request.getParameter("paymentType");

        if (address == null || address.isBlank() || paymentType == null || paymentType.isBlank()) {
            request.setAttribute("error", "Please provide address and select a payment method.");
            request.getRequestDispatcher("/checkout.jsp").forward(request, response);
            return;
        }

        if (paymentType.equals("CardPayment")) {
            String cardNumber = request.getParameter("cardNumber");
            String cardExpiry = request.getParameter("cardExpiry");
            String cardCVV = request.getParameter("cardCVV");

            if (cardNumber == null || cardNumber.isBlank() ||
                    cardExpiry == null || cardExpiry.isBlank() ||
                    cardCVV == null || cardCVV.isBlank()) {

                request.setAttribute("error", "Card details are required for Online Payment.");
                request.getRequestDispatcher("/checkout.jsp").forward(request, response);
                return;
            }
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

        String discountType = "";
        double discountValue = 0;
        double discount = 0;
        double total = subTotal - discount;
        double receivedAmount = total;
        double changeReturn = 0;

        Integer customerId = null;
        String username = (String) session.getAttribute("username");
        if ("customer".equalsIgnoreCase((String) session.getAttribute("role"))) {
            customerId = customerService.getCustomerIdByUsername(username);
        }

        Bill bill = new Bill(customerId, totalQty, subTotal, discountType, discountValue,
                total, receivedAmount, changeReturn, paymentType, "Paid", items);

        int billId = billService.generateBill(bill, true);

        if (billId > 0) {
            session.removeAttribute("cart");

            // Update stock quantities
            StockService stockService = new StockService();
            for (BillItem item : items) {
                int remainingQty = item.getQuantity();
                List<Stock> stocks = stockService.getStocksByProductId(item.getProductId());

                // Sort by expiry date ascending (FIFO)
                stocks.sort(Comparator.comparing(Stock::getExpiryDate));

                for (Stock stock : stocks) {
                    if (remainingQty <= 0)
                        break;

                    int available = stock.getQuantity();
                    int toDeduct = Math.min(available, remainingQty);

                    if (toDeduct > 0) {
                        boolean success = stockService.reduceStockQuantity(stock.getId(), toDeduct);
                        if (!success) {
                            System.err.println("Failed to reduce stock ID: " + stock.getId());
                        }
                        remainingQty -= toDeduct;
                    }
                }

                if (remainingQty > 0) {
                    System.err.println("Warning: Not enough stock to fulfill item: " + item.getProductName());
                }
            }

            request.setAttribute("success", "Order placed successfully! Bill ID: " + billId);
        } else {
            request.setAttribute("error", "Failed to place order. Please try again.");
        }

        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }
}
