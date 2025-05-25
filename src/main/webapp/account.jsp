<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Customer" %>
<%
    request.setAttribute("pageTitle", "My Account");
%>
<jsp:include page="/header.jsp" />

<div class="container py-4">
    <h2 class="mb-4 text-primary">👤 My Account</h2>

    <%
        Customer customer = (Customer) request.getAttribute("customer");
        if (customer != null) {
    %>
    <div class="card shadow-sm p-4">
        <div class="row mb-3">
            <div class="col-md-6">
                <strong>Full Name:</strong>
                <p><%= customer.getName() %></p>
            </div>
            <div class="col-md-6">
                <strong>Email:</strong>
                <p><%= customer.getEmail() %></p>
            </div>
            <div class="col-md-6">
                <strong>Phone:</strong>
                <p><%= customer.getPhone() %></p>
            </div>
        </div>
    </div>
    <%
        } else {
    %>
    <div class="alert alert-warning">Account information is not available.</div>
    <%
        }
    %>
</div>

<jsp:include page="/footer.jsp" />
