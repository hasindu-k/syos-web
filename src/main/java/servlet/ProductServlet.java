package servlet;

import model.Category;
import model.Product;
import model.ProductView;
import service.CategoryService;
import service.ProductService;
import service.StockService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private ProductService productService;
    private CategoryService categoryService;
    private StockService stockService;

    @Override
    public void init() {
        productService = new ProductService();
        categoryService = new CategoryService();
        stockService = new StockService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get product list from service
        List<Product> products = productService.getAllProducts();
        List<Category> categories = categoryService.getAllCategories();
        Map<Integer, String> categoryMap = categories.stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        List<ProductView> viewModels = new ArrayList<>();
        for (Product product : products) {
            String categoryName = categoryMap.get(product.getCategoryId());
            int stockQuantity = stockService.getStockQuantityByProductId(product.getId());
            viewModels.add(new ProductView(product, categoryName, stockQuantity));
        }

        request.setAttribute("products", viewModels);

        // Forward to JSP view
        request.getRequestDispatcher("/products.jsp").forward(request, response);
    }
}
