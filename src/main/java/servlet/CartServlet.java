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
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
	    Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");

	    if (cart == null) {
	        cart = new HashMap<>();
	    }

	    String action = request.getParameter("action");
	    String productIdStr = request.getParameter("productId");
	    Integer productId = null;

	    // Only try to parse productId if it's needed
	    if (action != null && !"clear".equalsIgnoreCase(action)) {
	        try {
	            productId = Integer.parseInt(productIdStr);
	        } catch (NumberFormatException e) {
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID.");
	            return;
	        }
	    }
		
		int quantity = 1; // default for remove action

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
