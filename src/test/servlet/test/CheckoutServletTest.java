package servlet.test;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import model.Bill;
import model.BillItem;
import model.Product;
import model.Stock;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.BillService;
import service.CustomerService;
import service.ProductService;
import service.StockService;
import servlet.CheckoutServlet;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CheckoutServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private RequestDispatcher dispatcher;
    @Mock
    private BillService billService;
    @Mock
    private CustomerService customerService;
    @Mock
    private ProductService productService;
    @Mock
    private StockService stockService;

    private CheckoutServlet servlet;
    private Map<Integer, AtomicInteger> cart;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        servlet = new CheckoutServlet();
        servlet.setProductService(productService);
        servlet.setCustomerService(customerService);
        servlet.setBillService(billService);

        cart = new HashMap<>();
        when(request.getSession(false)).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    @Test
    public void testNoSession() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(null);
        servlet.doPost(request, response);
        verify(response).sendRedirect("login");
    }

    @Test
    public void testNoRole() throws ServletException, IOException {
        when(session.getAttribute("role")).thenReturn(null);
        servlet.doPost(request, response);
        verify(response).sendRedirect("login");
    }

    @Test
    public void testMissingParameters() throws ServletException, IOException {
        when(session.getAttribute("role")).thenReturn("customer");
        when(request.getParameter("address")).thenReturn(null);
        when(request.getParameter("paymentType")).thenReturn(null);

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Please provide address and select a payment method.");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testCardPaymentMissingDetails() throws ServletException, IOException {
        when(session.getAttribute("role")).thenReturn("customer");
        when(request.getParameter("address")).thenReturn("123 Main St");
        when(request.getParameter("paymentType")).thenReturn("CardPayment");
        when(request.getParameter("cardNumber")).thenReturn(null);
        when(request.getParameter("cardExpiry")).thenReturn(null);
        when(request.getParameter("cardCVV")).thenReturn(null);

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Card details are required for Online Payment.");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testEmptyCart() throws ServletException, IOException {
        when(session.getAttribute("role")).thenReturn("customer");
        when(request.getParameter("address")).thenReturn("123 Main St");
        when(request.getParameter("paymentType")).thenReturn("Cash");
        when(session.getAttribute("cart")).thenReturn(new HashMap<>());

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Your cart is empty.");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testSuccessfulCheckout() throws ServletException, IOException {
        when(session.getAttribute("role")).thenReturn("customer");
        when(session.getAttribute("username")).thenReturn("testuser");
        when(request.getParameter("address")).thenReturn("123 Main St");
        when(request.getParameter("paymentType")).thenReturn("Cash");

        cart.put(1, new AtomicInteger(2));
        when(session.getAttribute("cart")).thenReturn(cart);

        Product product = new Product("Product 1", 1, 1, "Available", 1, "", 5, 1, 20.0);
        product.setId(1);
        when(productService.getProductById(1)).thenReturn(product);

        when(customerService.getCustomerIdByUsername("testuser")).thenReturn(1);
        when(billService.generateBill(any(Bill.class), eq(true))).thenReturn(1);

        List<Stock> stocks = new ArrayList<>();
        stocks.add(new Stock(1, 1, 10, new Date()));
        when(stockService.getStocksByProductId(1)).thenReturn(stocks);
        when(stockService.reduceStockQuantity(anyInt(), anyInt())).thenReturn(true);

        servlet.doPost(request, response);

        verify(session).removeAttribute("cart");
        verify(request).setAttribute("success", "Order placed successfully! Bill ID: 1");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testFailedBillGeneration() throws ServletException, IOException {
        when(session.getAttribute("role")).thenReturn("customer");
        when(session.getAttribute("username")).thenReturn("testuser");
        when(request.getParameter("address")).thenReturn("123 Main St");
        when(request.getParameter("paymentType")).thenReturn("Cash");

        cart.put(1, new AtomicInteger(2));
        when(session.getAttribute("cart")).thenReturn(cart);

        Product product = new Product("Product 1", 1, 1, "Available", 1, "", 5, 1, 20.0);
        product.setId(1);
        when(productService.getProductById(1)).thenReturn(product);

        when(customerService.getCustomerIdByUsername("testuser")).thenReturn(1);
        when(billService.generateBill(any(Bill.class), eq(true))).thenReturn(-1);

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Failed to place order. Please try again.");
        verify(dispatcher).forward(request, response);
    }
}
