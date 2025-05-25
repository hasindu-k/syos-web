<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Bill" %>
<%
    request.setAttribute("pageTitle", "My Orders");
%>
<jsp:include page="/header.jsp"/>

<div class="container py-4">
    <h2 class="mb-4 text-primary">🧾 My Orders</h2>

    <!-- 🔍 Filter Form -->
    <form method="get" action="my-orders" class="row g-3 mb-4">
        <div class="col-md-3">
            <label class="form-label">From Date</label>
            <input type="date" name="fromDate" value="<%= request.getParameter("fromDate") != null ? request.getParameter("fromDate") : "" %>" class="form-control"/>
        </div>
        <div class="col-md-3">
            <label class="form-label">To Date</label>
            <input type="date" name="toDate" value="<%= request.getParameter("toDate") != null ? request.getParameter("toDate") : "" %>" class="form-control"/>
        </div>
        <div class="col-md-3">
            <label class="form-label">Payment Type</label>
            <select name="paymentType" class="form-select">
                <option value="">All</option>
                <option value="Cash" <%= "Cash".equals(request.getParameter("paymentType")) ? "selected" : "" %>>Cash</option>
                <option value="CardPayment" <%= "CardPayment".equals(request.getParameter("paymentType")) ? "selected" : "" %>>Card Payment</option>
                <option value="CashOnDelivery" <%= "CashOnDelivery".equals(request.getParameter("paymentType")) ? "selected" : "" %>>Cash On Delivery</option>
            </select>
        </div>
        <div class="col-md-3">
            <label class="form-label">Payment Status</label>
            <select name="paymentStatus" class="form-select">
                <option value="">All</option>
                <option value="Paid" <%= "Paid".equals(request.getParameter("paymentStatus")) ? "selected" : "" %>>Paid</option>
                <option value="Pending" <%= "Pending".equals(request.getParameter("paymentStatus")) ? "selected" : "" %>>Pending</option>
            </select>
        </div>
        <div class="col-md-12 d-flex justify-content-between align-items-center mt-2">
            <button class="btn btn-primary" type="submit">
                <i class="bi bi-funnel-fill me-1"></i> Apply Filters
            </button>
            <a href="my-orders" class="btn btn-outline-secondary">Reset</a>
        </div>
    </form>

    <!-- 📦 Orders Table -->
    <%
        List<Bill> orders = (List<Bill>) request.getAttribute("orders");
        if (orders == null || orders.isEmpty()) {
    %>
        <div class="alert alert-info mt-3">You haven't placed any orders yet.</div>
    <%
        } else {
    %>
        <table class="table table-hover mt-2">
            <thead class="table-light">
                <tr>
                    <th>Order ID</th>
                    <th>Date</th>
                    <th>Items</th>
                    <th>Total (Rs.)</th>
                    <th>Payment Type</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (Bill order : orders) {
                %>
                <tr>
                    <td><%= order.getId() %></td>
                    <td><%= order.getBillDate() %></td>
                    <td><%= order.getTotalQty() %></td>
                    <td>Rs. <%= String.format("%.2f", order.getTotal()) %></td>
                    <td><%= order.getPaymentType().replaceAll("([a-z])([A-Z])", "$1 $2") %></td>
                    <td><%= order.getPaymentStatus() %></td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    <%
        }
    %>
</div>

<jsp:include page="/footer.jsp"/>
