package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		@SuppressWarnings("unchecked")
		Map<Integer, AtomicInteger> cart = (Map<Integer, AtomicInteger>) session.getAttribute("cart");

		if (cart == null) {
			cart = new ConcurrentHashMap<>();
			session.setAttribute("cart", cart);
		}

		String action = request.getParameter("action");
		String productIdStr = request.getParameter("productId");
		Integer productId = null;

		// Handle null action
		if (action == null) {
			session.setAttribute("cart", cart);
			response.sendRedirect("products");
			return;
		}

		if (!"clear".equalsIgnoreCase(action)) {
			try {
				productId = Integer.parseInt(productIdStr);
			} catch (NumberFormatException e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID.");
				return;
			}
		}

		int quantity = 1;

		switch (action) {
			case "add":
				cart.computeIfAbsent(productId, k -> new AtomicInteger(0)).addAndGet(quantity);
				break;
			case "remove":
				AtomicInteger currentQty = cart.get(productId);
				if (currentQty != null) {
					if (currentQty.get() <= 1) {
						cart.remove(productId);
					} else {
						currentQty.decrementAndGet();
					}
				}
				break;
			case "removeAll":
				cart.remove(productId);
				break;
			case "clear":
				cart.clear();
				break;
			default:
				// Unknown action, just redirect
				break;
		}

		session.setAttribute("cart", cart); // always write back
		response.sendRedirect("products");
	}
}
