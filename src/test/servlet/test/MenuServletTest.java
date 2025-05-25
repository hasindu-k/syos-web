package servlet.test;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import servlet.MenuServlet;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MenuServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private RequestDispatcher dispatcher;

    private MenuServlet servlet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        servlet = new MenuServlet();
    }

    @Test
    public void testDoGet_NoSession() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("login");
    }

    @Test
    public void testDoGet_NoRole() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("role")).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("login");
    }

    @Test
    public void testDoGet_AdminRole() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("role")).thenReturn("admin");
        when(request.getRequestDispatcher("/admin-menu.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(dispatcher).forward(eq(request), eq(response));
    }

    @Test
    public void testDoGet_CashierRole() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("role")).thenReturn("cashier");
        when(request.getRequestDispatcher("/cashier-menu.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(dispatcher).forward(eq(request), eq(response));
    }

    @Test
    public void testDoGet_ManagerRole() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("role")).thenReturn("manager");
        when(request.getRequestDispatcher("/manager-menu.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(dispatcher).forward(eq(request), eq(response));
    }

    @Test
    public void testDoGet_CustomerRole() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("role")).thenReturn("customer");
        when(request.getRequestDispatcher("/customer-menu.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(dispatcher).forward(eq(request), eq(response));
    }

    @Test
    public void testDoGet_UnknownRole() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("role")).thenReturn("unknown");
        when(request.getRequestDispatcher("/error.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("error"), eq("Unknown user role: unknown"));
        verify(dispatcher).forward(eq(request), eq(response));
    }

    @Test
    public void testDoGet_RoleCaseInsensitive() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("role")).thenReturn("ADMIN");
        when(request.getRequestDispatcher("/admin-menu.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(dispatcher).forward(eq(request), eq(response));
    }
}