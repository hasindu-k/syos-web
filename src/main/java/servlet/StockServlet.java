package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Stock;
import service.StockService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet("/stocks")
public class StockServlet extends HttpServlet {
    private StockService stockService;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void init() {
        stockService = new StockService();
    }

    // Handles viewing logic
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String message = request.getParameter("message");
        String error = request.getParameter("error");

        if (message != null) request.setAttribute("message", message);
        if (error != null) request.setAttribute("error", error);

        List<Stock> stocks;

        try {
            if ("low".equalsIgnoreCase(action)) {
                stocks = stockService.getLowStockItems();
            } else if ("product".equalsIgnoreCase(action)) {
                String productIdParam = request.getParameter("productId");
                if (productIdParam == null || productIdParam.isEmpty()) {
                    request.setAttribute("error", "Product ID is required.");
                    stocks = Collections.emptyList();
                } else {
                    try {
                        int productId = Integer.parseInt(productIdParam);
                        stocks = stockService.getStocksByProductId(productId);
                    } catch (NumberFormatException e) {
                        request.setAttribute("error", "Invalid product ID format.");
                        stocks = Collections.emptyList();
                    }
                }
            } else {
                stocks = stockService.getAllStocks();
            }

            request.setAttribute("stocks", stocks);
        } catch (Exception e) {
            request.setAttribute("error", "Error retrieving stocks: " + e.getMessage());
        }

        request.getRequestDispatcher("/stocks.jsp").forward(request, response);
    }

    // Handles adding new stock
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            int warehouseId = Integer.parseInt(request.getParameter("warehouseId"));
            String expiryDateStr = request.getParameter("expiryDate");

            Date expiryDate;
            try {
                expiryDate = dateFormat.parse(expiryDateStr);
            } catch (ParseException e) {
                response.sendRedirect(request.getContextPath() + "/stocks?error=" +
                        java.net.URLEncoder.encode("Invalid date format. Use yyyy-MM-dd.", "UTF-8"));
                return;
            }

            int stockId = stockService.addStock(productId, quantity, warehouseId, expiryDate);

            if (stockId > 0) {
                response.sendRedirect(request.getContextPath() + "/stocks?message=" +
                        java.net.URLEncoder.encode("Stock added successfully.", "UTF-8"));
            } else {
                response.sendRedirect(request.getContextPath() + "/stocks?error=" +
                        java.net.URLEncoder.encode("Failed to add stock.", "UTF-8"));
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/stocks?error=" +
                    java.net.URLEncoder.encode("Invalid number format: " + e.getMessage(), "UTF-8"));
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/stocks?error=" +
                    java.net.URLEncoder.encode("Error adding stock: " + e.getMessage(), "UTF-8"));
        }
    }
}
