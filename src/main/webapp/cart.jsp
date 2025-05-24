<%@ page import="java.util.Map" %>
<%@ page import="model.Product" %>
<%@ page import="service.ProductService" %>
<%
    request.setAttribute("pageTitle", "Your Cart");
%>
<jsp:include page="/header.jsp" />

<!-- Modern Cart UI -->
<div class="container py-5">
    <h1 class="display-5 fw-bold text-primary text-center mb-3">
        <i class="bi bi-cart3 me-2"></i>Your Shopping Cart
    </h1>
    <p class="text-muted text-center mb-4">Review your selected items before proceeding to checkout.</p>

    <%
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        ProductService productService = new ProductService();
        double total = 0;

        if (cart == null || cart.isEmpty()) {
    %>
        <div class="alert alert-info text-center p-4 shadow-sm rounded">
            <i class="bi bi-info-circle me-2"></i>Your cart is currently empty.
        </div>
    <%
        } else {
    %>
        <div class="card shadow-lg border-0">
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table align-middle mb-0 table-hover">
                        <thead class="table-primary">
                            <tr>
                                <th>#</th>
                                <th>Product</th>
                                <th class="text-center">Quantity</th>
                                <th class="text-end">Unit Price (LKR)</th>
                                <th class="text-end">Subtotal (LKR)</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                int index = 1;
                                for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                                    int productId = entry.getKey();
                                    int quantity = entry.getValue();
                                    Product product = productService.getProductById(productId);
                                    double price = product.getPrice();
                                    double lineTotal = price * quantity;
                                    total += lineTotal;
                            %>
                            <tr>
                                <td><%= index++ %></td>
                                <td><strong><%= product.getName() %></strong></td>
                                <td class="text-center">
                                    <div class="d-flex justify-content-center align-items-center">
                                        <span class="badge bg-secondary me-2"><%= quantity %></span>
                                        <form action="cart" method="post" class="d-inline">
                                            <input type="hidden" name="action" value="removeAll" />
                                            <input type="hidden" name="productId" value="<%= productId %>" />
                                            <button type="submit" class="btn btn-sm btn-outline-danger" title="Remove Item">
                                                <i class="bi bi-trash-fill"></i>
                                            </button>
                                        </form>
                                    </div>
                                </td>
                                <td class="text-end">Rs. <%= String.format("%.2f", price) %></td>
                                <td class="text-end">Rs. <%= String.format("%.2f", lineTotal) %></td>
                            </tr>
                            <% } %>
                        </tbody>
                        <tfoot class="table-light">
                            <tr>
                                <th colspan="4" class="text-end h5">Total:</th>
                                <th class="text-end h5 text-success">Rs. <%= String.format("%.2f", total) %></th>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>

        <div class="d-flex justify-content-between align-items-center mt-4">
            <a href="products" class="btn btn-outline-primary btn-lg">
                <i class="bi bi-arrow-left-circle me-2"></i>Continue Shopping
            </a>
            <a href="checkout.jsp" class="btn btn-success btn-lg">
                Proceed to Checkout <i class="bi bi-arrow-right-circle ms-2"></i>
            </a>
        </div>
    <%
        }
    %>
</div>

<jsp:include page="/footer.jsp" />
