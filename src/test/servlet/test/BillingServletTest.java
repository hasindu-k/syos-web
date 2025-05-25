package servlet.test;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import model.Bill;
import model.BillItem;
import model.Customer;
import model.Product;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import controller.BillController;
import service.CustomerService;
import service.ProductService;
import servlet.BillingServlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class BillingServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher dispatcher;
    @Mock
    private CustomerService customerService;
    @Mock
    private ProductService productService;
    @Mock
    private BillController billController;

    private BillingServlet servlet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        servlet = new BillingServlet();
        servlet.setBillController(billController);
        servlet.setCustomerService(customerService);
        servlet.setProductService(productService);
        
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer(1, "John Doe", "john@example.com", "1234567890"));
        when(customerService.getAllCustomers()).thenReturn(customers);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("customers"), any(List.class));

        verify(request).getRequestDispatcher("/billing.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_ValidBill() throws ServletException, IOException {
        // Setup request parameters
        when(request.getParameter("customerId")).thenReturn("1");
        when(request.getParameterValues("productId")).thenReturn(new String[] { "1", "2" });
        when(request.getParameterValues("quantity")).thenReturn(new String[] { "2", "1" });
        when(request.getParameter("discountType")).thenReturn("PERCENTAGE");
        when(request.getParameter("discountValue")).thenReturn("10");
        when(request.getParameter("receivedAmount")).thenReturn("100");

        // Mock product data
        Product product1 = new Product(
        	    "Product 1",     // name
        	    1,               // unitId
        	    1,               // categoryId
        	    "Available",     // status
        	    1,               // warehouseId
        	    "",              // note
        	    5,               // stockAlert
        	    1,               // supplierId
        	    20.0             // price
        	);
        	product1.setId(1);

        	Product product2 = new Product(
        	    "Product 2",
        	    1,
        	    1,
        	    "Available",
        	    1,
        	    "",
        	    5,
        	    1,
        	    30.0
        	);
        	product2.setId(2);
        	
        when(productService.getProductById(1)).thenReturn(product1);
        when(productService.getProductById(2)).thenReturn(product2);

        // Mock bill generation
        when(billController.generateBillWeb(anyInt(), anyList(), anyString(), anyDouble(), anyDouble()))
                .thenReturn(1);

        Bill mockBill = new Bill(
        	    1,              // customerId
        	    3,              // totalQty
        	    90.0,           // subTotal
        	    "PERCENTAGE",   // discountType
        	    10.0,           // discountValue
        	    81.0,           // total
        	    100.0,          // receivedAmount
        	    19.0,           // changeReturn
        	    "Cash",         // paymentType
        	    "Paid",         // paymentStatus
        	    Collections.emptyList() // items
        	);
        mockBill.setId(1);
        when(billController.getBillById(1)).thenReturn(mockBill);

        // Mock customer data
        Customer customer = new Customer(1, "John Doe", "john@example.com", "1234567890");
        when(customerService.getCustomerById(1)).thenReturn(customer);

        servlet.doPost(request, response);

        verify(request).setAttribute("billId", 1);
        verify(request).setAttribute("bill", mockBill);
        verify(request).setAttribute("customerName", "John Doe");
        verify(request).getRequestDispatcher("/billing-success.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_EmptyItems() throws ServletException, IOException {
        when(request.getParameterValues("productId")).thenReturn(new String[] {});
        when(request.getParameterValues("quantity")).thenReturn(new String[] {});

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "No valid products selected.");
        verify(request).getRequestDispatcher("/billing.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_InvalidProductId() throws ServletException, IOException {
        when(request.getParameterValues("productId")).thenReturn(new String[] { "999" });
        when(request.getParameterValues("quantity")).thenReturn(new String[] { "1" });
        when(productService.getProductById(999)).thenReturn(null);

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "No valid products selected.");
        verify(request).getRequestDispatcher("/billing.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_BillGenerationFailed() throws ServletException, IOException {
        // Setup request parameters
        when(request.getParameter("customerId")).thenReturn("1");
        when(request.getParameterValues("productId")).thenReturn(new String[] { "1" });
        when(request.getParameterValues("quantity")).thenReturn(new String[] { "1" });
        when(request.getParameter("discountType")).thenReturn("PERCENTAGE");
        when(request.getParameter("discountValue")).thenReturn("0");
        when(request.getParameter("receivedAmount")).thenReturn("20");

        // Mock product data
        Product product = new Product(
        	    "Product 1",     // name
        	    1,               // unitId
        	    1,               // categoryId
        	    "Available",     // status
        	    1,               // warehouseId
        	    "",              // note
        	    5,               // stockAlert
        	    1,               // supplierId
        	    20.0             // price
        	);
        product.setId(1);


        when(productService.getProductById(1)).thenReturn(product);

        // Mock failed bill generation
        when(billController.generateBillWeb(anyInt(), anyList(), anyString(), anyDouble(), anyDouble()))
                .thenReturn(-1);

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Failed to generate bill.");
        verify(request).getRequestDispatcher("/billing.jsp");
        verify(dispatcher).forward(request, response);
    }
}