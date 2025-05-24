<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setAttribute("pageTitle", "Checkout");
%>
<jsp:include page="/header.jsp"/>

<h2 class="text-center text-primary mb-4">Checkout</h2>

<% String error = (String) request.getAttribute("error");
   String success = (String) request.getAttribute("success");
%>
<% if (error != null) { %>
    <div class="alert alert-danger"><%= error %></div>
<% } else if (success != null) { %>
    <div class="alert alert-success"><%= success %></div>
<% } %>

<form action="checkout" method="post" class="card p-4 shadow-sm">
    <!-- Address -->
    <div class="mb-3">
        <label for="address" class="form-label">Shipping Address</label>
        <textarea class="form-control" name="address" id="address" rows="3" required></textarea>
    </div>

    <!-- Payment Type -->
    <div class="mb-3">
        <label for="paymentType" class="form-label">Payment Method</label>
        <select class="form-select" name="paymentType" id="paymentType" required onchange="toggleCardFields()">
            <option value="">Select Payment Method</option>
            <option value="CashOnDelivery">Cash on Delivery</option>
            <option value="CardPayment">Online Payment (Card)</option>
        </select>
    </div>

    <!-- Card Details (hidden by default) -->
    <div id="cardDetails" style="display:none;">
        <div class="mb-3">
            <label for="cardNumber" class="form-label">Card Number</label>
            <input type="text" class="form-control" name="cardNumber" id="cardNumber">
        </div>
        <div class="mb-3">
            <label for="cardExpiry" class="form-label">Expiry Date (MM/YY)</label>
            <input type="text" class="form-control" name="cardExpiry" id="cardExpiry">
        </div>
        <div class="mb-3">
            <label for="cardCVV" class="form-label">CVV</label>
            <input type="text" class="form-control" name="cardCVV" id="cardCVV">
        </div>
    </div>

    <button type="submit" class="btn btn-success w-100">Place Order</button>
</form>

<script>
function toggleCardFields() {
    const paymentType = document.getElementById("paymentType").value;
    const cardFields = document.getElementById("cardDetails");
    cardFields.style.display = paymentType === "Card" ? "block" : "none";

    document.getElementById("cardNumber").required = paymentType === "Card";
    document.getElementById("cardExpiry").required = paymentType === "Card";
    document.getElementById("cardCVV").required = paymentType === "Card";
}
</script>

<jsp:include page="/footer.jsp"/>
