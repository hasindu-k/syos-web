package servlet.test;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import model.Stock;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.StockService;
import servlet.StockServlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class StockServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher dispatcher;
    @Mock
    private StockService stockService;

    private StockServlet servlet;
    private SimpleDateFormat dateFormat;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        servlet = new StockServlet();
        servlet.setStockService(stockService);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    @Test
    public void testDoGet_AllStocks() throws ServletException, IOException {
        List<Stock> expectedStocks = Arrays.asList(
                new Stock(1, 1, 10, new Date()),
                new Stock(2, 1, 20, new Date()));
        when(stockService.getAllStocks()).thenReturn(expectedStocks);

        servlet.doGet(request, response);

        verify(request).setAttribute("stocks", expectedStocks);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_LowStock() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("low");
        List<Stock> expectedStocks = Arrays.asList(
                new Stock(1, 1, 5, new Date()),
                new Stock(2, 1, 3, new Date()));
        when(stockService.getLowStockItems()).thenReturn(expectedStocks);

        servlet.doGet(request, response);

        verify(request).setAttribute("stocks", expectedStocks);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_ByProductId() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("product");
        when(request.getParameter("productId")).thenReturn("1");
        List<Stock> expectedStocks = Arrays.asList(
                new Stock(1, 1, 10, new Date()),
                new Stock(1, 2, 15, new Date()));
        when(stockService.getStocksByProductId(1)).thenReturn(expectedStocks);

        servlet.doGet(request, response);

        verify(request).setAttribute("stocks", expectedStocks);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_InvalidProductId() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("product");
        when(request.getParameter("productId")).thenReturn("invalid");

        servlet.doGet(request, response);

        verify(request).setAttribute("error", "Invalid product ID format.");
        verify(request).setAttribute("stocks", Collections.emptyList());
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_MissingProductId() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("product");
        when(request.getParameter("productId")).thenReturn(null);

        servlet.doGet(request, response);

        verify(request).setAttribute("error", "Product ID is required.");
        verify(request).setAttribute("stocks", Collections.emptyList());
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_ServiceException() throws ServletException, IOException {
        when(stockService.getAllStocks()).thenThrow(new RuntimeException("Database error"));

        servlet.doGet(request, response);

        verify(request).setAttribute("error", "Error retrieving stocks: Database error");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_SuccessfulAdd() throws ServletException, IOException {
        when(request.getParameter("productId")).thenReturn("1");
        when(request.getParameter("quantity")).thenReturn("10");
        when(request.getParameter("warehouseId")).thenReturn("1");
        when(request.getParameter("expiryDate")).thenReturn("2024-12-31");
        when(stockService.addStock(anyInt(), anyInt(), anyInt(), any(Date.class))).thenReturn(1);
        when(request.getContextPath()).thenReturn("");

        servlet.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoPost_InvalidDate() throws ServletException, IOException {
        when(request.getParameter("productId")).thenReturn("1");
        when(request.getParameter("quantity")).thenReturn("10");
        when(request.getParameter("warehouseId")).thenReturn("1");
        when(request.getParameter("expiryDate")).thenReturn("invalid-date");
        when(request.getContextPath()).thenReturn("");

        servlet.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoPost_InvalidNumber() throws ServletException, IOException {
        when(request.getParameter("productId")).thenReturn("invalid");
        when(request.getParameter("quantity")).thenReturn("10");
        when(request.getParameter("warehouseId")).thenReturn("1");
        when(request.getParameter("expiryDate")).thenReturn("2024-12-31");
        when(request.getContextPath()).thenReturn("");

        servlet.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoPost_AddStockFailed() throws ServletException, IOException {
        when(request.getParameter("productId")).thenReturn("1");
        when(request.getParameter("quantity")).thenReturn("10");
        when(request.getParameter("warehouseId")).thenReturn("1");
        when(request.getParameter("expiryDate")).thenReturn("2024-12-31");
        when(stockService.addStock(anyInt(), anyInt(), anyInt(), any(Date.class))).thenReturn(-1);
        when(request.getContextPath()).thenReturn("");

        servlet.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }
}