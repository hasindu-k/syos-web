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
import servlet.RegisterServlet;
import service.AuthService;
import service.CustomerService;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class RegisterServletTest {

    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private RequestDispatcher dispatcher;
    @Mock private AuthService mockAuthService;
    @Mock private CustomerService mockCustomerService;

    private RegisterServlet servlet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        servlet = new RegisterServlet();
        servlet.setAuthService(mockAuthService);
        servlet.setCustomerService(mockCustomerService);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_SuccessfulCustomerRegistration() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("newuser");
        when(request.getParameter("password")).thenReturn("pass123");
        when(request.getParameter("role")).thenReturn("customer");
        when(request.getParameter("fullName")).thenReturn("Test User");
        when(request.getParameter("email")).thenReturn("test@example.com");
        when(request.getParameter("phone")).thenReturn("1234567890");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);
        when(mockAuthService.registerUser("newuser", "pass123", "customer")).thenReturn(true);

        servlet.doPost(request, response);

        verify(mockCustomerService).registerCustomer("Test User", "test@example.com", "1234567890");
        verify(request).setAttribute(eq("success"), anyString());
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_FailedRegistration() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("existinguser");
        when(request.getParameter("password")).thenReturn("pass123");
        when(request.getParameter("role")).thenReturn("admin");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);
        when(mockAuthService.registerUser("existinguser", "pass123", "admin")).thenReturn(false);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), anyString());
        verify(dispatcher).forward(request, response);
    }
}
