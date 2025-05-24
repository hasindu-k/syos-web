package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Category;
import model.Product;
import model.ProductView;
import service.CategoryService;
import service.ProductService;
import service.StockService;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {
    private ProductService productService;
    private CategoryService categoryService;
    private StockService stockService;

    @Override
    public void init() throws ServletException {
        productService = new ProductService();
        categoryService = new CategoryService();
        stockService = new StockService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        // if (session == null || session.getAttribute("role") == null ||
        // !session.getAttribute("role").equals("Admin")) {
        // response.sendRedirect(request.getContextPath() + "/login.jsp");
        // return;
        // }

        String role = null;
        if (session != null) {
            role = (String) session.getAttribute("role");
        }
        request.setAttribute("role", role);

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            request.getRequestDispatcher("/admin-menu.jsp").forward(request, response);
            return;
        }

        switch (pathInfo) {
            case "/products":
                handleProducts(request, response);
                break;
            case "/categories":
                handleCategories(request, response);
                break;
            case "/categories/get":
                handleGetCategory(request, response);
                break;
            case "/products/get":
                handleGetProduct(request, response);
                break;
            case "/users":
                handleUsers(request, response);
                break;
            case "/reports":
                handleReports(request, response);
                break;
            case "/logout":
                handleLogout(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        switch (pathInfo) {
            case "/products/add":
                handleAddProduct(request, response);
                break;
            case "/products/update":
                handleUpdateProduct(request, response);
                break;
            case "/products/delete":
                handleDeleteProduct(request, response);
                break;
            case "/categories/add":
                handleAddCategory(request, response);
                break;
            case "/categories/update":
                handleUpdateCategory(request, response);
                break;
            case "/categories/delete":
                handleDeleteCategory(request, response);
                break;
            case "/users/updateRole":
                handleUpdateUserRole(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/admin-products.jsp").forward(request, response);
    }

    private void handleCategories(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Category> categories = categoryService.getAllCategories();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/admin-categories.jsp").forward(request, response);
    }

    private void handleGetCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing category ID");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            Category category = categoryService.getCategoryById(id);

            response.setContentType("application/json");
            if (category != null) {
                String json = String.format("{\"id\": %d, \"name\": \"%s\", \"description\": \"%s\"}",
                        category.getId(),
                        escapeJson(category.getName()),
                        escapeJson(category.getDescription()));
                response.getWriter().write(json);
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Category not found\"}");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid category ID format");
        }
    }

    private void handleGetProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idParam = request.getParameter("id");
        response.setContentType("application/json");

        if (idParam == null || idParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Missing product ID\"}");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            Product product = productService.getProductById(id);

            if (product != null) {
                String json = String.format(
                        "{ \"success\": true, \"id\": %d, \"name\": \"%s\", \"price\": %.2f, \"unitId\": %d, \"categoryId\": %d, "
                                +
                                "\"status\": \"%s\", \"warehouseId\": %d, \"note\": \"%s\", \"stockAlert\": %d, \"supplierId\": %d }",
                        product.getId(),
                        escapeJson(product.getName()),
                        product.getPrice(),
                        product.getUnitId(),
                        product.getCategoryId(),
                        escapeJson(product.getStatus()),
                        product.getWarehouseId(),
                        escapeJson(product.getNote()),
                        product.getStockAlert(),
                        product.getSupplierId());
                response.getWriter().write(json);
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Product not found\"}");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid product ID format\"}");
        }
    }

    // Helper to safely escape JSON strings
    private String escapeJson(String str) {
        return str == null ? "" : str.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
    }

    private void handleUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        service.AuthService authService = new service.AuthService();
        List<model.User> users = authService.getAllUsers();

        request.setAttribute("users", users);
        request.getRequestDispatcher("/admin-users.jsp").forward(request, response);
    }

    private void handleReports(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String type = request.getParameter("type"); // e.g., "totalSale", "reorderLevels"
        String date = request.getParameter("date"); // optional for some reports

        if (type == null || type.isEmpty()) {
            request.getRequestDispatcher("/admin-reports.jsp").forward(request, response);
            return;
        }

        strategy.ReportStrategy strategy = null;

        switch (type != null ? type : "") {
            case "totalSale":
                if (date == null || date.isEmpty()) {
                    date = java.time.LocalDate.now().toString();
                }
                strategy = new strategy.TotalSaleReportStrategy(date);
                break;
            case "reshelved":
                if (date == null || date.isEmpty()) {
                    date = java.time.LocalDate.now().toString();
                }
                strategy = new strategy.ReshelvedItemsReportStrategy(date);
                break;
            case "reorderLevels":
                strategy = new strategy.ReorderLevelsReportStrategy();
                break;
            case "stockBatch":
                strategy = new strategy.StockBatchWiseReportStrategy();
                break;
            default:
                request.setAttribute("error", "Invalid or missing report type");
                request.getRequestDispatcher("/admin-reports.jsp").forward(request, response);
                return;
        }

        service.ReportService reportService = new service.ReportService();
        List<Map<String, Object>> reportData = strategy.generateReport();

        request.setAttribute("reportData", reportData);
        request.setAttribute("reportTitle", strategy.getReportName());
        request.setAttribute("reportType", type);
        request.setAttribute("reportDate", date);

        request.getRequestDispatcher("/admin-reports.jsp").forward(request, response);
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }

    private void handleAddProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");

        // Unit, Category, and Status are assumed to be always provided
        int unitId = Integer.parseInt(request.getParameter("unitId"));
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        String status = request.getParameter("status");

        // Handle nullable warehouseId
        String warehouseIdParam = request.getParameter("warehouseId");
        int warehouseId = (warehouseIdParam != null && !warehouseIdParam.isEmpty()) ? Integer.parseInt(warehouseIdParam)
                : 0;

        // Handle nullable supplierId
        String supplierIdParam = request.getParameter("supplierId");
        int supplierId = (supplierIdParam != null && !supplierIdParam.isEmpty()) ? Integer.parseInt(supplierIdParam)
                : 0;

        // Handle nullable note
        String note = request.getParameter("note");
        if (note == null) {
            note = "";
        }

        // Stock Alert and Price are required
        int stockAlert = Integer.parseInt(request.getParameter("stockAlert"));
        double price = Double.parseDouble(request.getParameter("price"));

        int productId = productService.addProduct(name, unitId, categoryId, status,
                warehouseId, note, stockAlert, supplierId, price);

        response.setContentType("application/json");
        response.getWriter().write("{\"success\": " + (productId > 0) + ", \"id\": " + productId + "}");
    }

    private void handleUpdateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");

        // Unit, Category, and Status are assumed to be always provided
        int unitId = Integer.parseInt(request.getParameter("unitId"));
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        String status = request.getParameter("status");

        // Handle nullable warehouseId
        String warehouseIdParam = request.getParameter("warehouseId");
        int warehouseId = (warehouseIdParam != null && !warehouseIdParam.isEmpty()) ? Integer.parseInt(warehouseIdParam)
                : 0;

        // Handle nullable supplierId
        String supplierIdParam = request.getParameter("supplierId");
        int supplierId = (supplierIdParam != null && !supplierIdParam.isEmpty()) ? Integer.parseInt(supplierIdParam)
                : 0;

        // Handle nullable note
        String note = request.getParameter("note");
        if (note == null) {
            note = "";
        }

        // Stock Alert and Price are required
        int stockAlert = Integer.parseInt(request.getParameter("stockAlert"));
        double price = Double.parseDouble(request.getParameter("price"));

        boolean updated = productService.updateProduct(id, name, unitId, categoryId,
                status, warehouseId, note, stockAlert, supplierId, price);

        response.setContentType("application/json");
        response.getWriter().write("{\"success\": " + updated + "}");
    }

    private void handleDeleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean deleted = productService.deleteProduct(id);

        response.setContentType("application/json");
        response.getWriter().write("{\"success\": " + deleted + "}");
    }

    private void handleAddCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        int categoryId = categoryService.addCategory(name, description);

        response.setContentType("application/json");
        response.getWriter().write("{\"success\": " + (categoryId > 0) + ", \"id\": " + categoryId + "}");
    }

    private void handleUpdateCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        boolean updated = categoryService.updateCategory(id, name, description);

        response.setContentType("application/json");
        response.getWriter().write("{\"success\": " + updated + "}");
    }

    private void handleDeleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean deleted = categoryService.deleteCategory(id);

        response.setContentType("application/json");
        response.getWriter().write("{\"success\": " + deleted + "}");
    }

    private void handleUpdateUserRole(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        String newRole = request.getParameter("newRole");

        service.AuthService authService = new service.AuthService();
        boolean success = authService.updateUserRole(userId, newRole);

        if (!success) {
            request.setAttribute("error", "Failed to update role.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}