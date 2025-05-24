<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    request.setAttribute("pageTitle", "Product List");
%>
<jsp:include page="/header.jsp"/>

<c:set var="userRole" value="${sessionScope.role}" />

<%
    Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
    int itemCount = 0;
    if (cart != null) {
        for (int qty : cart.values()) {
            itemCount += qty;
        }
    }
%>

<!-- Custom Hover Styles -->
<style>
    .btn-outline-secondary:hover,
    .btn-outline-light:hover {
        background-color: #0d6efd !important;
        color: white !important;
        border-color: #0d6efd !important;
    }
</style>

<!-- Header Bar (No background now) -->
<div class="d-flex justify-content-between align-items-center px-1 py-2 rounded mb-4">
    <div class="d-flex align-items-center">
        <i class="bi bi-box-seam fs-4 me-2 text-primary"></i>
        <h5 class="mb-0 fw-bold text-primary">Product Dashboard</h5>
    </div>
    <c:if test="${userRole == 'Customer'}">
        <a href="cart.jsp" class="btn btn-outline-secondary btn-sm">
            🛒 View Cart (<%= itemCount %>)
        </a>
    </c:if>
</div>

<!-- Search + Back Button Row -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div class="w-50">
        <input type="text" id="searchBox" class="form-control form-control-sm" placeholder="Search products...">
    </div>
    <a href="menu" class="btn btn-outline-secondary btn-sm ms-2">
        Back to Menu
    </a>
</div>

<!-- Product Table Card -->
<div class="card shadow-sm">
    <div class="card-header bg-light">
        <h6 class="mb-0 fw-semibold">Available Products</h6>
    </div>
    <div class="card-body p-0">
        <c:choose>
            <c:when test="${not empty products}">
                <table class="table table-hover mb-0" id="productTable">
                    <thead class="table-light">
                        <tr>
                            <th>#</th>
                            <th>Product Name</th>
                            <th>Category</th>
                            <th>Stock</th>
                            <th>Status</th>
                            <c:if test="${userRole == 'Customer'}">
                                <th>Action</th>
                            </c:if>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${products}" varStatus="status">
                            <tr>
                                <td>${status.index + 1}</td>
                                <td class="product-name">${p.product.name}</td>
                                <td class="product-category">${p.categoryName}</td>
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
                                <c:if test="${userRole == 'Customer'}">
                                    <td>
                                        <form action="add-to-cart" method="post" class="d-flex">
                                            <input type="hidden" name="productId" value="${p.product.id}" />
                                            <input type="number" name="quantity" value="1" min="1" max="10"
                                                   class="form-control form-control-sm me-2" style="width: 70px;" />
                                            <button type="submit" class="btn btn-sm btn-primary">Add</button>
                                        </form>
                                    </td>
                                </c:if>
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

<!-- Search Filter JS -->
<script>
    document.getElementById('searchBox').addEventListener('keyup', function () {
        const filter = this.value.toLowerCase();
        document.querySelectorAll('#productTable tbody tr').forEach(row => {
            const name = row.querySelector('.product-name').textContent.toLowerCase();
            const category = row.querySelector('.product-category').textContent.toLowerCase();
            row.style.display = name.includes(filter) || category.includes(filter) ? '' : 'none';
        });
    });
</script>

<jsp:include page="/footer.jsp"/>
