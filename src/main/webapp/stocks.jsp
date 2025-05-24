<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    request.setAttribute("pageTitle", "Stock Management");
%>
<jsp:include page="/header.jsp" />

<div class="container my-5">
    <!-- Header Section -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="text-primary fw-bold mb-0"><i class="bi bi-box-seam"></i> Stock Management</h2>
        <a href="${pageContext.request.contextPath}/menu" class="btn btn-outline-primary btn-sm">
            <i class="bi bi-arrow-left-circle"></i> Back to Menu
        </a>
    </div>

    <!-- Search and Filter Form -->
    <div class="card shadow-sm mb-4">
        <div class="card-body">
            <form method="get" action="stocks" class="row g-2 align-items-center">
                <div class="col-md-4">
                    <input type="text" name="productId" placeholder="Enter Product ID" class="form-control" />
                </div>
                <div class="col-md-auto">
                    <button type="submit" name="action" value="product" class="btn btn-primary btn-sm">
                        <i class="bi bi-search"></i> Check Product Stock
                    </button>
                </div>
                <div class="col-md-auto">
                    <button type="submit" name="action" value="low" class="btn btn-warning btn-sm">
                        <i class="bi bi-exclamation-triangle"></i> View Low Stock
                    </button>
                </div>
                <div class="col-md-auto">
                    <a href="stocks" class="btn btn-secondary btn-sm">
                        <i class="bi bi-x-circle"></i> Clear Filter
                    </a>
                </div>
                <div class="col-md-auto ms-auto">
                    <button type="button" class="btn btn-success btn-sm" data-bs-toggle="modal" data-bs-target="#addStockModal">
                        <i class="bi bi-plus-circle"></i> Add New Stock
                    </button>
                </div>
            </form>
        </div>
    </div>

    <!-- Alerts -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger">
            <i class="bi bi-exclamation-circle-fill"></i> ${error}
        </div>
    </c:if>

    <c:if test="${empty stocks}">
        <div class="alert alert-info text-center">
            <i class="bi bi-info-circle-fill"></i> No stock data available.
        </div>
    </c:if>

    <!-- Stock Table -->
    <c:if test="${not empty stocks}">
        <div class="card shadow-sm">
            <div class="card-header bg-primary text-white">
                <h5 class="mb-0"><i class="bi bi-list-ul"></i> Stock List</h5>
            </div>
            <div class="card-body p-0">
                <table class="table table-hover mb-0">
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
                                <td>
                                    <span class="badge bg-${stock.quantity > 10 ? 'success' : 'danger'}">
                                        <i class="bi ${stock.quantity > 10 ? 'bi-check-circle-fill' : 'bi-exclamation-circle-fill'}"></i>
                                        ${stock.quantity}
                                    </span>
                                </td>
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
                <h5 class="modal-title" id="addStockModalLabel">
                    <i class="bi bi-plus-circle"></i> Add New Stock
                </h5>
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
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-check-circle"></i> Add Stock
                </button>
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                    <i class="bi bi-x-circle"></i> Cancel
                </button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/footer.jsp"></jsp:include>
