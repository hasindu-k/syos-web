<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    request.setAttribute("pageTitle", "Stock Management");
%>
<jsp:include page="/header.jsp"></jsp:include>

<div class="container my-5">
	<div class="mb-3">
        <a href="${pageContext.request.contextPath}/menu" class="btn btn-primary">Back to Menu</a>
    </div>
    <h2 class="mb-4 text-center">Stock Management</h2>

    <!-- Search and Filter Form -->
    <form method="get" action="stocks" class="row g-2 mb-4">
        <div class="col-md-4">
            <input type="text" name="productId" placeholder="Enter Product ID" class="form-control" />
        </div>
        <div class="col-md-auto">
            <button type="submit" name="action" value="product" class="btn btn-primary">Check Product Stock</button>
        </div>
        <div class="col-md-auto">
            <button type="submit" name="action" value="low" class="btn btn-warning">View Low Stock</button>
        </div>
        <div class="col-md-auto">
            <a href="stocks" class="btn btn-secondary">Clear Filter</a>
        </div>
        <div class="col-md-auto">
            <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addStockModal">
                Add New Stock
            </button>
        </div>
    </form>

    <!-- Alerts -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <!-- Stock Table -->
    <c:if test="${empty stocks}">
        <div class="alert alert-info">No stock data available.</div>
    </c:if>

    <c:if test="${not empty stocks}">
        <div class="card shadow-sm">
            <div class="card-body">
                <table class="table table-hover table-bordered">
                    <thead class="table-light">
                        <tr>
                            <th>Stock ID</th>
                            <th>Product ID</th>
                            <th>Quantity</th>
                            <th>Expiry Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="stock" items="${stocks}">
                            <tr>
                                <td>${stock.id}</td>
                                <td>${stock.productId}</td>
                                <td>${stock.quantity}</td>
                                <td><fmt:formatDate value="${stock.expiryDate}" pattern="yyyy-MM-dd" /></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:if>
</div>

<!-- Add Stock Modal -->
<div class="modal fade" id="addStockModal" tabindex="-1" aria-labelledby="addStockModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <form action="stocks" method="post" class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addStockModalLabel">Add New Stock</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3">
                    <label class="form-label">Product ID</label>
                    <input type="number" name="productId" class="form-control" required />
                </div>
                <div class="mb-3">
                    <label class="form-label">Quantity</label>
                    <input type="number" name="quantity" class="form-control" required />
                </div>
                <div class="mb-3">
                    <label class="form-label">Warehouse ID</label>
                    <input type="number" name="warehouseId" class="form-control" required />
                </div>
                <div class="mb-3">
                    <label class="form-label">Expiry Date</label>
                    <input type="date" name="expiryDate" class="form-control" required />
                </div>
            </div>
            <div class="modal-footer">
                <button type="submit" class="btn btn-primary">Add Stock</button>
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/footer.jsp"></jsp:include>
