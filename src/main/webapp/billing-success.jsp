<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="header.jsp" %>

<%
    model.Bill bill = (model.Bill) request.getAttribute("bill");
    String customerName = (String) request.getAttribute("customerName");
%>

<div class="container mt-5 mb-5 p-4 shadow-lg rounded bg-white">
    <div class="alert alert-success text-center">
        <h3 class="alert-heading">✅ Bill Generated Successfully!</h3>
        <p class="lead">Bill ID: <strong>${billId}</strong></p>
    </div>

    <div class="row mb-4">
        <div class="col-md-6">
            <p><strong>📅 Date:</strong> <%= bill.getBillDate() %></p>
        </div>
        <div class="col-md-6 text-md-end">
            <p><strong>👤 Customer:</strong> <%= customerName %></p>
        </div>
    </div>

    <table class="table table-striped table-bordered">
        <thead class="table-light">
            <tr class="text-center">
                <th>🛒 Product</th>
                <th>📦 Qty</th>
                <th>💰 Price</th>
                <th>🧾 Total</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${bill.items}">
            <tr class="text-center align-middle">
                <td>${item.productName}</td>
                <td>${item.quantity}</td>
                <td>Rs. ${item.price}</td>
                <td>Rs. ${item.totalPrice}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <div class="row mt-4">
        <div class="col-md-6">
            <p><strong>Subtotal:</strong> Rs. <%= bill.getSubTotal() %></p>
            <p><strong>Discount:</strong> <%= bill.getDiscountType() %> <%= bill.getDiscountValue() %></p>
        </div>
        <div class="col-md-6 text-md-end">
            <p><strong>Total:</strong> Rs. <%= bill.getTotal() %></p>
            <p><strong>Received:</strong> Rs. <%= bill.getReceivedAmount() %></p>
            <p><strong>Change Return:</strong> Rs. <%= bill.getChangeReturn() %></p>
        </div>
    </div>

    <!-- Action buttons -->
    <div class="row mt-5">
        <div class="col-md-4 text-start">
            <a href="billing" class="btn btn-outline-primary btn-lg">📋 New Bill</a>
        </div>
        <div class="col-md-4 text-center">
            <a href="print-bill?billId=<%= bill.getId() %>" class="btn btn-success btn-lg" target="_blank">🖨️ Print Bill (PDF)</a>
        </div>
        <div class="col-md-4 text-end">
            <a href="menu" class="btn btn-outline-secondary btn-lg">🔙 Back to Menu</a>
        </div>
    </div>
</div>

<%@ include file="footer.jsp" %>
