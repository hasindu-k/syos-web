<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Product List</title>
</head>
<body>
<h2>Product List</h2>
<ul>
    <%
        java.util.List<model.Product> products = (java.util.List<model.Product>) request.getAttribute("products");
        for (model.Product p : products) {
    %>
        <li><%= p.getName() %> - <%= p.getStatus() %></li>
    <%
        }
    %>
</ul>
</body>
</html>
