<%@ page import="java.util.Map" %>
<%@ page import="model.Product" %>
<%@ page import="service.ProductService" %>
<%
    request.setAttribute("pageTitle", "Your Cart");
%>
<jsp:include page="/header.jsp"/>

<!-- Your Cart content here -->
<h1 class="display-6 fw-bold text-primary text-center">Your Shopping Cart</h1>
<p class="text-muted text-center">Review your selected items before proceeding to checkout.</p>

<%
    Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
    ProductService productService = new ProductService();
    double total = 0;

    if (cart == null || cart.isEmpty()) {
%>
    <div class="alert alert-info text-center">Your cart is empty.</div>
<%
    } else {
%>
<div class="card shadow-sm">
        <div class="card-body p-0">
            <table class="table table-hover mb-0">
                <thead class="table-light">
                <tr>
                    <th>#</th>
                    <th>Product Name</th>
                    <th>Quantity</th>
                    <th>Unit Price</th>
                    <th>Total</th>
                </tr>
                </thead>
                <tbody>
                <%
                    int index = 1;
                    for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                        int productId = entry.getKey();
                        int quantity = entry.getValue();
                        Product product = productService.getProductById(productId);
                        double price = product.getPrice(); // you must ensure Product has getPrice()
                        double lineTotal = price * quantity;
                        total += lineTotal;
                %>
                <tr>
                    <td><%= index++ %></td>
                    <td><%= product.getName() %></td>
                    <td><%= quantity %></td>
                    <td><%= price %></td>
                    <td><%= lineTotal %></td>
                </tr>
                <% } %>
                </tbody>
                <tfoot class="table-light">
                <tr>
                    <th colspan="4" class="text-end">Total</th>
                    <th><%= total %></th>
                </tr>
                </tfoot>
            </table>
        </div>
    </div>
    <div class="d-flex justify-content-between align-items-center mt-4">
    <button class="btn btn-outline-secondary" onclick="history.back()">Back</button>

    <form action="checkout" method="post" class="mb-0">
        <button class="btn btn-success btn-lg">Proceed to Checkout</button>
    </form>
</div>
<%
    }
%>

<jsp:include page="/footer.jsp"/>
