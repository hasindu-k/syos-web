<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Product List</title>
    <!-- Bootstrap CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container py-5">
	<div class="text-end mb-3">
	    <%
    Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
    int itemCount = 0;
    if (cart != null) {
        for (int qty : cart.values()) {
            itemCount += qty;
        }
    }
%>
<a href="cart.jsp" class="btn btn-outline-primary btn-sm">
    🛒 View Cart (<%= itemCount %>)
</a>
	</div>
    <div class="text-center mb-4">
		<h1 class="display-6 fw-bold text-primary">Welcome to Your Shopping Dashboard</h1>
		<p class="text-muted">Browse available products and add them to your cart.</p>
    </div>

    <div class="card shadow-sm">
        <div class="card-header bg-primary text-white">
            <h5 class="mb-0">Available Products</h5>
        </div>
        <div class="card-body p-0">
            <c:choose>
                <c:when test="${not empty products}">
                    <table class="table table-hover mb-0">
                        <thead class="table-light">
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Product Name</th>
                            <th scope="col">Category</th>
                            <th scope="col">Stock Quantity</th>
                            <th scope="col">Status</th>
                            <th scope="col">Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="p" items="${products}" varStatus="status">
                            <tr>
                                <th scope="row">${status.index + 1}</th>
                                <td>${p.product.name}</td>
                                <td>${p.categoryName}</td>
                                <td>
                                    <span class="badge bg-${p.stockQuantity > 0 ? 'success' : 'danger'}">
                                        ${p.stockQuantity}
                                    </span>
                                </td>
                                <td>
                                    <span class="text-${p.product.status == 'Active' ? 'success' : 'secondary'} fw-semibold">
                                        ${p.product.status}
                                    </span>
                                </td>
                                <td>
								    <form action="add-to-cart" method="post" class="d-flex">
								        <input type="hidden" name="productId" value="${p.product.id}" />
								        <input type="number" name="quantity" value="1" min="1" max="10" class="form-control form-control-sm me-2" style="width: 70px;" />
								        <button type="submit" class="btn btn-sm btn-primary">Add</button>
								    </form>
								</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="p-4 text-center text-muted">
                        <p>No products available at the moment.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<!-- Bootstrap JS (Optional if using interactive components) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
