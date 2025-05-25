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
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CartServletConcurrencyTest {

    private static final int THREAD_COUNT = 10;
    private static final int ITERATIONS = 100;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;

    private CartServlet servlet;

    private ConcurrentHashMap<Integer, AtomicInteger> sharedCart;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        servlet = new CartServlet();
        sharedCart = new ConcurrentHashMap<>();
    }

    @Test
    public void testConcurrentAddToCart() throws InterruptedException {
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("productId")).thenReturn("1");

        when(session.getAttribute("cart")).thenReturn(sharedCart);
        doAnswer(invocation -> {
            sharedCart = (ConcurrentHashMap<Integer, AtomicInteger>) invocation.getArgument(1);
            return null;
        }).when(session).setAttribute(eq("cart"), any(Map.class));

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < ITERATIONS; j++) {
                        servlet.doPost(request, response);
                    }
                } catch (ServletException | IOException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        AtomicInteger quantity = sharedCart.get(1);
        assertEquals("Cart should contain correct quantity after concurrent adds.",
                THREAD_COUNT * ITERATIONS, quantity.get());
    }

    @Test
    public void testConcurrentRemoveFromCart() throws InterruptedException {
        sharedCart.put(1, new AtomicInteger(THREAD_COUNT * ITERATIONS)); // preload

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("action")).thenReturn("remove");
        when(request.getParameter("productId")).thenReturn("1");

        when(session.getAttribute("cart")).thenReturn(sharedCart);
        doAnswer(invocation -> {
            sharedCart = (ConcurrentHashMap<Integer, AtomicInteger>) invocation.getArgument(1);
            return null;
        }).when(session).setAttribute(eq("cart"), any(Map.class));

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < ITERATIONS; j++) {
                        servlet.doPost(request, response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        AtomicInteger remaining = sharedCart.get(1);
        int remainingQty = remaining != null ? remaining.get() : 0;
        assertEquals("Cart should be empty or 0 after concurrent removals.", 0, remainingQty);
    }

    @Test
    public void testConcurrentClearCart() throws InterruptedException {
        // Preload with multiple products
        sharedCart.put(1, new AtomicInteger(10));
        sharedCart.put(2, new AtomicInteger(5));
        sharedCart.put(3, new AtomicInteger(20));

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("action")).thenReturn("clear");

        when(session.getAttribute("cart")).thenReturn(sharedCart);
        doAnswer(invocation -> {
            sharedCart = (ConcurrentHashMap<Integer, AtomicInteger>) invocation.getArgument(1);
            return null;
        }).when(session).setAttribute(eq("cart"), any(Map.class));

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < ITERATIONS; j++) {
                        servlet.doPost(request, response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        assertEquals("Cart should be empty after concurrent clears.", 0, sharedCart.size());
    }

}
