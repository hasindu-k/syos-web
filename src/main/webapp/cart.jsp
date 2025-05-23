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
<!-- table rendering code here (same as before) -->
<!-- checkout + back buttons -->
<%
    }
%>

<jsp:include page="/footer.jsp"/>
