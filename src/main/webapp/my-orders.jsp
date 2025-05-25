<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Bill" %>
<%
    request.setAttribute("pageTitle", "My Orders");
%>
<jsp:include page="/header.jsp"/>

<div class="container py-5">
    <div class="text-center mb-5">
        <h2 class="fw-bold text-primary">
            <i class="bi bi-receipt-cutoff me-2"></i>My Orders
        </h2>
        <p class="text-muted">Track your purchase history and order details</p>
    </div>

    <!-- 🔍 Filter Form -->
    <form method="get" action="my-orders" class="row g-4 mb-4 bg-light rounded-4 p-4 shadow-sm">
        <div class="col-md-3">
            <label class="form-label fw-semibold">From Date</label>
            <input type="date" name="fromDate" value="<%= request.getParameter("fromDate") != null ? request.getParameter("fromDate") : "" %>" class="form-control rounded-3"/>
        </div>
        <div class="col-md-3">
            <label class="form-label fw-semibold">To Date</label>
            <input type="date" name="toDate" value="<%= request.getParameter("toDate") != null ? request.getParameter("toDate") : "" %>" class="form-control rounded-3"/>
        </div>
        <div class="col-md-3">
            <label class="form-label fw-semibold">Payment Type</label>
            <select name="paymentType" class="form-select rounded-3">
                <option value="">All</option>
                <option value="Cash" <%= "Cash".equals(request.getParameter("paymentType")) ? "selected" : "" %>>Cash</option>
                <option value="CardPayment" <%= "CardPayment".equals(request.getParameter("paymentType")) ? "selected" : "" %>>Card Payment</option>
                <option value="CashOnDelivery" <%= "CashOnDelivery".equals(request.getParameter("paymentType")) ? "selected" : "" %>>Cash On Delivery</option>
            </select>
        </div>
        <div class="col-md-3">
            <label class="form-label fw-semibold">Payment Status</label>
            <select name="paymentStatus" class="form-select rounded-3">
                <option value="">All</option>
                <option value="Paid" <%= "Paid".equals(request.getParameter("paymentStatus")) ? "selected" : "" %>>Paid</option>
                <option value="Pending" <%= "Pending".equals(request.getParameter("paymentStatus")) ? "selected" : "" %>>Pending</option>
            </select>
        </div>
        <div class="col-md-12 d-flex justify-content-between align-items-center mt-2">
            <button class="btn btn-primary px-4 rounded-pill" type="submit">
                <i class="bi bi-funnel-fill me-1"></i> Apply Filters
            </button>
            <a href="my-orders" class="btn btn-outline-secondary rounded-pill px-4">
                <i class="bi bi-x-circle me-1"></i> Reset
            </a>
        </div>
    </form>

    <!-- 📦 Orders Table -->
    <%
        List<Bill> orders = (List<Bill>) request.getAttribute("orders");
        if (orders == null || orders.isEmpty()) {
    %>
        <div class="alert alert-info text-center py-4 rounded-4 shadow-sm mt-4">
            <i class="bi bi-info-circle-fill me-2"></i>You haven't placed any orders yet.
        </div>
    <%
        } else {
    %>
        <div class="table-responsive shadow-sm rounded-4">
            <table class="table table-hover table-bordered align-middle mb-0">
                <thead class="table-light text-center">
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
                    <tr class="text-center">
                        <td><span class="badge bg-primary-subtle text-primary fw-semibold"><%= order.getId() %></span></td>
                        <td><%= order.getBillDate() %></td>
                        <td><%= order.getTotalQty() %></td>
                        <td class="fw-bold text-success">Rs. <%= String.format("%.2f", order.getTotal()) %></td>
                        <td><%= order.getPaymentType().replaceAll("([a-z])([A-Z])", "$1 $2") %></td>
                        <td>
                            <span class="badge <%= "Paid".equals(order.getPaymentStatus()) ? "bg-success" : "bg-warning text-dark" %>">
                                <%= order.getPaymentStatus() %>
                            </span>
                        </td>
                    </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        </div>
    <%
        }
    %>
</div>

<jsp:include page="/footer.jsp"/>
