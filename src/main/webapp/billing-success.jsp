<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="header.jsp" %>

<%
    String billId = request.getParameter("billId");
%>

<div class="alert alert-success mt-4">
    <h4 class="alert-heading">Bill Generated Successfully!</h4>
    <p>Bill ID: <strong><%= billId %></strong></p>
    <a href="print-bill?billId=<%= billId %>" class="btn btn-outline-success" target="_blank">🖨️ Print Bill (PDF)</a>
    
</div>

<a href="billing" class="btn btn-primary">New Bill</a>
<a href="menu" class="btn btn-secondary">Back to Menu</a>

<%@ include file="footer.jsp" %>
