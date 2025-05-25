package servlet.test;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import servlet.CartServlet;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CartServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;

    private CartServlet servlet;
    private ConcurrentHashMap<Integer, AtomicInteger> cart;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        servlet = new CartServlet();
        cart = new ConcurrentHashMap<>();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("cart")).thenReturn(cart);
        doAnswer(invocation -> {
            cart = (ConcurrentHashMap<Integer, AtomicInteger>) invocation.getArgument(1);
            return null;
        }).when(session).setAttribute(eq("cart"), any(Map.class));
    }

    @Test
    public void testAddToEmptyCart() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("productId")).thenReturn("1");

        servlet.doPost(request, response);

        assertEquals("Cart should contain one item", 1, cart.size());
        assertEquals("Item quantity should be 1", 1, cart.get(1).get());
        verify(session).setAttribute("cart", cart);
        verify(response).sendRedirect("products");
    }

    @Test
    public void testAddToExistingItem() throws ServletException, IOException {
        cart.put(1, new AtomicInteger(1));
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("productId")).thenReturn("1");

        servlet.doPost(request, response);

        assertEquals("Cart should contain one item", 1, cart.size());
        assertEquals("Item quantity should be 2", 2, cart.get(1).get());
        verify(session).setAttribute("cart", cart);
        verify(response).sendRedirect("products");
    }

    @Test
    public void testRemoveFromCart() throws ServletException, IOException {
        cart.put(1, new AtomicInteger(2));
        when(request.getParameter("action")).thenReturn("remove");
        when(request.getParameter("productId")).thenReturn("1");

        servlet.doPost(request, response);

        assertEquals("Cart should contain one item", 1, cart.size());
        assertEquals("Item quantity should be 1", 1, cart.get(1).get());
        verify(session).setAttribute("cart", cart);
        verify(response).sendRedirect("products");
    }

    @Test
    public void testRemoveLastItem() throws ServletException, IOException {
        cart.put(1, new AtomicInteger(1));
        when(request.getParameter("action")).thenReturn("remove");
        when(request.getParameter("productId")).thenReturn("1");

        servlet.doPost(request, response);

        assertTrue("Cart should be empty", cart.isEmpty());
        verify(session).setAttribute("cart", cart);
        verify(response).sendRedirect("products");
    }

    @Test
    public void testRemoveAll() throws ServletException, IOException {
        cart.put(1, new AtomicInteger(5));
        when(request.getParameter("action")).thenReturn("removeAll");
        when(request.getParameter("productId")).thenReturn("1");

        servlet.doPost(request, response);

        assertTrue("Cart should be empty", cart.isEmpty());
        verify(session).setAttribute("cart", cart);
        verify(response).sendRedirect("products");
    }

    @Test
    public void testClearCart() throws ServletException, IOException {
        cart.put(1, new AtomicInteger(5));
        cart.put(2, new AtomicInteger(3));
        when(request.getParameter("action")).thenReturn("clear");

        servlet.doPost(request, response);

        assertTrue("Cart should be empty", cart.isEmpty());
        verify(session).setAttribute("cart", cart);
        verify(response).sendRedirect("products");
    }

    @Test
    public void testInvalidProductId() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("productId")).thenReturn("invalid");

        servlet.doPost(request, response);

        assertTrue("Cart should be empty", cart.isEmpty());
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID.");
        verify(session, never()).setAttribute(eq("cart"), any(Map.class));
    }

    // @Test
    // public void testNewSession() throws ServletException, IOException {
    // // Create a new cart instance for this test
    // ConcurrentHashMap<Integer, AtomicInteger> newCart = new
    // ConcurrentHashMap<>();
    //
    // when(session.getAttribute("cart")).thenReturn(null);
    // when(request.getParameter("action")).thenReturn("add");
    // when(request.getParameter("productId")).thenReturn("1");
    //
    // // Capture the cart that gets set in the session
    // doAnswer(invocation -> {
    // Object value = invocation.getArgument(1);
    // if (value instanceof ConcurrentHashMap) {
    // newCart.putAll((ConcurrentHashMap<Integer, AtomicInteger>) value);
    // }
    // return null;
    // }).when(session).setAttribute(eq("cart"), any());
    //
    // servlet.doPost(request, response);
    //
    // assertEquals("Cart should contain one item", 1, newCart.size());
    // assertEquals("Item quantity should be 1", 1, newCart.get(1).get());
    // verify(session).setAttribute("cart", newCart);
    // verify(response).sendRedirect("products");
    // }

    // @Test
    // public void testNullAction() throws ServletException, IOException {
    // // Create a new cart instance for this test
    // ConcurrentHashMap<Integer, AtomicInteger> newCart = new
    // ConcurrentHashMap<>();
    //
    // when(session.getAttribute("cart")).thenReturn(null);
    // when(request.getParameter("action")).thenReturn(null);
    //
    // // Capture the cart that gets set in the session
    // doAnswer(invocation -> {
    // Object value = invocation.getArgument(1);
    // if (value instanceof ConcurrentHashMap) {
    // newCart.putAll((ConcurrentHashMap<Integer, AtomicInteger>) value);
    // }
    // return null;
    // }).when(session).setAttribute(eq("cart"), any());
    //
    // servlet.doPost(request, response);
    //
    // assertTrue("Cart should be empty", newCart.isEmpty());
    // verify(session).setAttribute("cart", newCart);
    // verify(response).sendRedirect("products");
    // }

    @Test
    public void testMultipleItems() throws ServletException, IOException {
        // Add first item
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("productId")).thenReturn("1");
        servlet.doPost(request, response);

        // Add second item
        when(request.getParameter("productId")).thenReturn("2");
        servlet.doPost(request, response);

        assertEquals("Cart should contain two items", 2, cart.size());
        assertEquals("First item quantity should be 1", 1, cart.get(1).get());
        assertEquals("Second item quantity should be 1", 1, cart.get(2).get());
        verify(session, times(2)).setAttribute("cart", cart);
        verify(response, times(2)).sendRedirect("products");
    }
}