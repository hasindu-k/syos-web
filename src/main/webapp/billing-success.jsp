<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="header.jsp" %>

<%
    model.Bill bill = (model.Bill) request.getAttribute("bill");
    String customerName = (String) request.getAttribute("customerName");
%>

<div class="alert alert-success mt-4">
    <h4 class="alert-heading">Bill Generated Successfully!</h4>
    <p>Bill ID: <strong>${billId}</strong></p>
</div>

<div class="mb-4">
    <p><strong>Date:</strong> <%= bill.getBillDate() %></p>
    <p><strong>Customer:</strong> <%= customerName %></p>
</div>

<table class="table table-bordered">
    <thead class="table-light">
        <tr>
            <th>Product</th>
            <th>Qty</th>
            <th>Price</th>
            <th>Total</th>
        </tr>
    </thead>
    <tbody>
    <c:forEach var="item" items="${bill.items}">
        <tr>
            <td>${item.productName}</td>
            <td>${item.quantity}</td>
            <td>Rs. ${item.price}</td>
            <td>Rs. ${item.totalPrice}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<div class="mt-3">
    <p><strong>Subtotal:</strong> Rs. <%= bill.getSubTotal() %></p>
    <p><strong>Discount:</strong> <%= bill.getDiscountType() %> <%= bill.getDiscountValue() %></p>
    <p><strong>Total:</strong> Rs. <%= bill.getTotal() %></p>
    <p><strong>Received:</strong> Rs. <%= bill.getReceivedAmount() %></p>
    <p><strong>Change Return:</strong> Rs. <%= bill.getChangeReturn() %></p>
</div>

<div class="mt-4">
    <a href="print-bill?billId=<%= bill.getId() %>" class="btn btn-outline-success" target="_blank">🖨️ Print Bill (PDF)</a>
    <a href="billing" class="btn btn-primary">New Bill</a>
    <a href="menu" class="btn btn-secondary">Back to Menu</a>
</div>

<%@ include file="footer.jsp" %>
