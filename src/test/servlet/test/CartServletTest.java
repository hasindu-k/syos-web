package servlet.test;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import servlet.CartServlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class CartServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;

    private CartServlet servlet;
    private Map<Integer, Integer> cart;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        servlet = new CartServlet();
        cart = new HashMap<>();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("cart")).thenReturn(cart);
    }

    @Test
    public void testDoPost_AddItem() throws IOException, ServletException {
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("productId")).thenReturn("1");

        servlet.doPost(request, response);

        verify(session).setAttribute("cart", cart);
        verify(response).sendRedirect("products");
        assert cart.get(1) == 1;
    }

    @Test
    public void testDoPost_AddExistingItem() throws IOException, ServletException {
        cart.put(1, 2);
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("productId")).thenReturn("1");

        servlet.doPost(request, response);

        verify(session).setAttribute("cart", cart);
        verify(response).sendRedirect("products");
        assert cart.get(1) == 3;
    }

    @Test
    public void testDoPost_RemoveItem() throws IOException, ServletException {
        cart.put(1, 2);
        when(request.getParameter("action")).thenReturn("remove");
        when(request.getParameter("productId")).thenReturn("1");

        servlet.doPost(request, response);

        verify(session).setAttribute("cart", cart);
        verify(response).sendRedirect("products");
        assert cart.get(1) == 1;
    }

    @Test
    public void testDoPost_RemoveLastItem() throws IOException, ServletException {
        cart.put(1, 1);
        when(request.getParameter("action")).thenReturn("remove");
        when(request.getParameter("productId")).thenReturn("1");

        servlet.doPost(request, response);

        verify(session).setAttribute("cart", cart);
        verify(response).sendRedirect("products");
        assert !cart.containsKey(1);
    }

    @Test
    public void testDoPost_RemoveAll() throws IOException, ServletException {
        cart.put(1, 3);
        when(request.getParameter("action")).thenReturn("removeAll");
        when(request.getParameter("productId")).thenReturn("1");

        servlet.doPost(request, response);

        verify(session).setAttribute("cart", cart);
        verify(response).sendRedirect("products");
        assert !cart.containsKey(1);
    }

    @Test
    public void testDoPost_ClearCart() throws IOException, ServletException {
        cart.put(1, 2);
        cart.put(2, 3);
        when(request.getParameter("action")).thenReturn("clear");

        servlet.doPost(request, response);

        verify(session).setAttribute("cart", cart);
        verify(response).sendRedirect("products");
        assert cart.isEmpty();
    }

    @Test
    public void testDoPost_NewCart() throws IOException, ServletException {
        when(session.getAttribute("cart")).thenReturn(null);
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("productId")).thenReturn("1");

        servlet.doPost(request, response);

        verify(session).setAttribute(eq("cart"), any(Map.class));
        verify(response).sendRedirect("products");
    }
}