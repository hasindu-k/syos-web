package servlet.test;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.shelvesService;
import servlet.ReshelveServlet;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class ReshelveServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private shelvesService shelvesService;

    private ReshelveServlet servlet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        servlet = new ReshelveServlet();
        servlet.setShelvesService(shelvesService);
    }

    @Test
    public void testDoPost_SuccessfulReshelve() throws ServletException, IOException {
        when(shelvesService.reshelf()).thenReturn(1); // ✅ return mock value

        servlet.doPost(request, response);

        verify(shelvesService).reshelf();
        verify(response).sendRedirect("menu");
    }


    @Test
    public void testDoPost_ServiceException() throws ServletException, IOException {
        when(shelvesService.reshelf()).thenThrow(new RuntimeException("Database error"));

        servlet.doPost(request, response);

        verify(shelvesService).reshelf();
        verify(response).sendRedirect("menu"); // ✅ still reached
    }
}