package servlet.test;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Category;
import model.Product;
import model.ProductView;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import servlet.ProductServlet;
import service.CategoryService;
import service.ProductService;
import service.StockService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher dispatcher;
    @Mock
    private ProductService mockProductService;
    @Mock
    private CategoryService mockCategoryService;
    @Mock
    private StockService mockStockService;

    private ProductServlet servlet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        servlet = new ProductServlet();
        servlet.setProductService(mockProductService);
        servlet.setCategoryService(mockCategoryService);
        servlet.setStockService(mockStockService);
    }

    @Test
    public void testDoGet_WithProducts() throws ServletException, IOException {
        // Setup test data
        List<Product> products = new ArrayList<>();
        Product product1 = new Product("Product 1", 1, 1, "active", 1, "", 10, 1, 10.0);
        product1.setId(1);
        product1.setDescription("Description 1");
        Product product2 = new Product("Product 2", 1, 2, "active", 1, "", 10, 1, 20.0);
        product2.setId(2);
        product2.setDescription("Description 2");
        products.add(product1);
        products.add(product2);

        List<Category> categories = new ArrayList<>();
        Category category1 = new Category("Category 1", "Description 1");
        category1.setId(1);
        Category category2 = new Category("Category 2", "Description 2");
        category2.setId(2);
        categories.add(category1);
        categories.add(category2);

        when(mockProductService.getAllProducts()).thenReturn(products);
        when(mockCategoryService.getAllCategories()).thenReturn(categories);
        when(mockStockService.getStockQuantityByProductId(1)).thenReturn(5);
        when(mockStockService.getStockQuantityByProductId(2)).thenReturn(10);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        // Execute
        servlet.doGet(request, response);

        // Verify
        verify(request).setAttribute(eq("products"), anyList());
        verify(dispatcher).forward(eq(request), eq(response));

        // Verify the product list contains the correct data
        verify(request).setAttribute(eq("products"), argThat(productList -> {
            List<ProductView> views = (List<ProductView>) productList;
            return views.size() == 2 &&
                    views.get(0).getProduct().getName().equals("Product 1") &&
                    views.get(0).getCategoryName().equals("Category 1") &&
                    views.get(0).getStockQuantity() == 5 &&
                    views.get(1).getProduct().getName().equals("Product 2") &&
                    views.get(1).getCategoryName().equals("Category 2") &&
                    views.get(1).getStockQuantity() == 10;
        }));
    }

    @Test
    public void testDoGet_EmptyProducts() throws ServletException, IOException {
        // Setup test data
        List<Product> products = new ArrayList<>();
        List<Category> categories = new ArrayList<>();

        when(mockProductService.getAllProducts()).thenReturn(products);
        when(mockCategoryService.getAllCategories()).thenReturn(categories);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        // Execute
        servlet.doGet(request, response);

        // Verify
        verify(request).setAttribute(eq("products"), anyList());
        verify(dispatcher).forward(eq(request), eq(response));

        // Verify the product list is empty
        verify(request).setAttribute(eq("products"), argThat(productList -> {
            List<ProductView> views = (List<ProductView>) productList;
            return views.isEmpty();
        }));
    }

    @Test
    public void testDoGet_WithMissingCategory() throws ServletException, IOException {
        // Setup test data
        List<Product> products = new ArrayList<>();
        Product product = new Product("Product 1", 1, 1, "active", 1, "", 10, 1, 10.0);
        product.setId(1);
        product.setDescription("Description 1");
        products.add(product);

        List<Category> categories = new ArrayList<>();
        Category category = new Category("Category 2", "Description 2");
        category.setId(2);
        categories.add(category);

        when(mockProductService.getAllProducts()).thenReturn(products);
        when(mockCategoryService.getAllCategories()).thenReturn(categories);
        when(mockStockService.getStockQuantityByProductId(1)).thenReturn(5);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        // Execute
        servlet.doGet(request, response);

        // Verify
        verify(request).setAttribute(eq("products"), anyList());
        verify(dispatcher).forward(eq(request), eq(response));

        // Verify the product list contains the product with null category
        verify(request).setAttribute(eq("products"), argThat(productList -> {
            List<ProductView> views = (List<ProductView>) productList;
            return views.size() == 1 &&
                    views.get(0).getProduct().getName().equals("Product 1") &&
                    views.get(0).getCategoryName() == null &&
                    views.get(0).getStockQuantity() == 5;
        }));
    }
}