package controller;

import model.Product;
import service.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private ProductService productService;

    @Override
    public void init() {
        productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get product list from service
        List<Product> products = productService.getAllProducts();

        // Set products as request attribute
        request.setAttribute("products", products);

        // Forward to JSP view
        request.getRequestDispatcher("/products.jsp").forward(request, response);
    }
}
