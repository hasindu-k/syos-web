package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		int productId = Integer.parseInt(request.getParameter("productId"));
		int quantity = 1; // default for remove action

		HttpSession session = request.getSession();
		@SuppressWarnings("unchecked")
		Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
		if (cart == null) {
			cart = new HashMap<>();
		}

		switch (action) {
			case "add":
				cart.put(productId, cart.getOrDefault(productId, 0) + quantity);
				break;

			case "remove":
				if (cart.containsKey(productId)) {
					int currentQty = cart.get(productId);
					if (currentQty <= 1) {
						cart.remove(productId);
					} else {
						cart.put(productId, currentQty - 1);
					}
				}
				break;
			case "removeAll":
				cart.remove(productId);
				break;
			case "clear":
				cart.clear();
				break;
		}

		session.setAttribute("cart", cart);
		response.sendRedirect("products");
	}
}
