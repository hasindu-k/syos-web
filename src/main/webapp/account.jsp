<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Customer" %>
<%
    request.setAttribute("pageTitle", "My Account");
%>
<jsp:include page="/header.jsp" />

<div class="container py-5">
    <div class="text-center mb-5">
        <h2 class="fw-bold text-primary">
            <i class="bi bi-person-circle me-2"></i>My Account
        </h2>
        <p class="text-muted">View and manage your personal information</p>
    </div>

    <%
        Customer customer = (Customer) request.getAttribute("customer");
        if (customer != null) {
    %>

    <div class="card shadow border-0 rounded-4">
        <div class="card-body p-5">
            <div class="row g-4">
                <div class="col-md-6">
                    <label class="form-label fw-semibold">Full Name</label>
                    <div class="form-control bg-light border-0"><%= customer.getName() %></div>
                </div>

                <div class="col-md-6">
                    <label class="form-label fw-semibold">Email</label>
                    <div class="form-control bg-light border-0"><%= customer.getEmail() %></div>
                </div>

                <div class="col-md-6">
                    <label class="form-label fw-semibold">Phone</label>
                    <div class="form-control bg-light border-0"><%= customer.getPhone() %></div>
                </div>
            </div>
        </div>
    </div>

    <%
        } else {
    %>
    <div class="alert alert-warning text-center py-4 rounded-3 shadow-sm">
        <i class="bi bi-exclamation-triangle-fill me-2"></i>Account information is not available.
    </div>
    <%
        }
    %>
</div>

<jsp:include page="/footer.jsp" />
