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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class MenuServletConcurrencyTest {

    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession session;
    @Mock private RequestDispatcher dispatcher;

    private MenuServlet servlet;
    private static final int THREAD_COUNT = 10;
    private static final int ITERATIONS = 100;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        servlet = new MenuServlet();
    }

    @Test
    public void testConcurrentAdminAccess() throws InterruptedException, ServletException, IOException {
        // Setup
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("role")).thenReturn("admin");
        when(request.getRequestDispatcher("/admin-menu.jsp")).thenReturn(dispatcher);

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        AtomicInteger successCount = new AtomicInteger(0);
        List<Exception> exceptions = new ArrayList<>();

        // Create multiple concurrent requests
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < ITERATIONS; j++) {
                        servlet.doGet(request, response);
                        successCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    exceptions.add(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        // Wait for all threads to complete
        boolean completed = latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        // Verify results
        assertEquals("All requests should complete successfully", 
                    THREAD_COUNT * ITERATIONS, successCount.get());
        assertEquals("No exceptions should occur", 0, exceptions.size());
        verify(dispatcher, times(THREAD_COUNT * ITERATIONS)).forward(eq(request), eq(response));
    }

    @Test
    public void testConcurrentMixedRoles() throws InterruptedException, ServletException, IOException {
        // Setup
        String[] roles = {"admin", "cashier", "manager", "customer"};
        when(request.getSession(false)).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        AtomicInteger successCount = new AtomicInteger(0);
        List<Exception> exceptions = new ArrayList<>();

        // Create multiple concurrent requests with different roles
        for (int i = 0; i < THREAD_COUNT; i++) {
            final String role = roles[i % roles.length];
            executor.submit(() -> {
                try {
                    when(session.getAttribute("role")).thenReturn(role);
                    for (int j = 0; j < ITERATIONS; j++) {
                        servlet.doGet(request, response);
                        successCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    exceptions.add(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        // Wait for all threads to complete
        boolean completed = latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        // Verify results
        assertEquals("All requests should complete successfully", 
                    THREAD_COUNT * ITERATIONS, successCount.get());
        assertEquals("No exceptions should occur", 0, exceptions.size());
        verify(dispatcher, times(THREAD_COUNT * ITERATIONS)).forward(eq(request), eq(response));
    }

    @Test
    public void testConcurrentSessionExpiry() throws InterruptedException, IOException {
        // Setup
        when(request.getSession(false)).thenReturn(null);

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        AtomicInteger successCount = new AtomicInteger(0);
        List<Exception> exceptions = new ArrayList<>();

        // Create multiple concurrent requests with expired sessions
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < ITERATIONS; j++) {
                        servlet.doGet(request, response);
                        successCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    exceptions.add(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        // Wait for all threads to complete
        boolean completed = latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        // Verify results
        assertEquals("All requests should complete successfully", 
                    THREAD_COUNT * ITERATIONS, successCount.get());
        assertEquals("No exceptions should occur", 0, exceptions.size());
        verify(response, times(THREAD_COUNT * ITERATIONS)).sendRedirect("login");
    }
}