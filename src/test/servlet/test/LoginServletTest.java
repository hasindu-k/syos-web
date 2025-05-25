package servlet.test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servlet.LoginServlet;

public class LoginServletTest {

	@Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher dispatcher;

    private LoginServlet loginServlet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        loginServlet = new LoginServlet();
        loginServlet.init(); // manually call init() to initialize AuthService
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);
        loginServlet.doGet(request, response);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_ValidCredentials() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("ad@123");
        when(request.getSession()).thenReturn(session);

        loginServlet.doPost(request, response);

        verify(session).setAttribute("username", "admin");
        verify(session).setAttribute("role", "Admin");
        verify(response).sendRedirect("menu");
    }

    @Test
    public void testDoPost_InvalidCredentials() throws Exception {
        when(request.getParameter("username")).thenReturn("wrong");
        when(request.getParameter("password")).thenReturn("wrong");
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        loginServlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), anyString());
        verify(dispatcher).forward(request, response);
    }
}
