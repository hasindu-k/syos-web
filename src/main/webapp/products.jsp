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
 .btn-outline-secondary {
            min-width: 180px;
            padding: 8x 0;
            font-size: 16px;
            color: #1976d2;
            background: #fff;
            border: 1px solid #1976d2;
            border-radius: 10px;
            text-decoration: none;
            text-align: center;
            font-weight: bold;
            transition: background 0.2s, color 0.2s;
        }
    .btn-outline-secondary:hover,
    .btn-outline-light:hover {
        background-color: #0d6efd !important;
        color: white !important;
        border-color: #0d6efd !important;
    }
     @media (max-width: 768px) {
        .input-group,
        .dropdown,
        .btn-outline-secondary {
            width: 100% !important;
        }
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

<!-- Search + Category Dropdown + Back Button Row -->
<div class="d-flex justify-content-between align-items-center mb-4 gap-2 flex-wrap">
    <!-- Search Bar with Icon -->
    <div class="input-group w-100 w-md-50">
        <span class="input-group-text bg-white border-end-0">
            <i class="bi bi-search text-muted"></i>
        </span>
        <input type="text" id="searchBox" class="form-control form-control-sm border-start-0" placeholder="Search products...">
    </div>

    <!-- Category Dropdown -->
    <div class="dropdown">
        <button class="btn btn-outline-secondary btn-sm dropdown-toggle"
        type="button"
        id="categoryDropdown"
        data-bs-toggle="dropdown"
        data-selected="All"
        aria-expanded="false">
    Filter by Category
</button>
        <ul class="dropdown-menu" aria-labelledby="categoryDropdown">
            <li><a class="dropdown-item category-option" href="#" data-category="All">All Categories</a></li>
            <c:forEach var="cat" items="${categories}">
                <li><a class="dropdown-item category-option" href="#" data-category="${cat.name}">${cat.name}</a></li>
            </c:forEach>
        </ul>
    </div>

    <!-- Back Button -->
    <a href="menu" class="btn btn-outline-secondary btn-sm">
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
                            <th>Price (LKR)</th>
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
                                <td class="product-price">${p.product.price}</td>
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
                                    <td class="d-flex gap-1">
                                        <!-- Add Button -->
                                        <form action="cart" method="post" class="d-flex">
                                            <input type="hidden" name="action" value="add" />
                                            <input type="hidden" name="productId" value="${p.product.id}" />
                                            <button type="submit" class="btn btn-sm btn-outline-primary">+</button>
                                        </form>
                                    
                                        <!-- Remove Button -->
                                        <form action="cart" method="post">
                                            <input type="hidden" name="action" value="remove" />
                                            <input type="hidden" name="productId" value="${p.product.id}" />
                                            <button type="submit" class="btn btn-sm btn-outline-danger">−</button>
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
</script><script>
    document.getElementById('searchBox').addEventListener('keyup', filterTable);

    document.querySelectorAll('.category-option').forEach(item => {
        item.addEventListener('click', function (e) {
            e.preventDefault();
            const selected = this.getAttribute('data-category');
            const dropdownButton = document.getElementById('categoryDropdown');

            // Update visible text
            dropdownButton.innerText = selected === 'All' ? 'Filter by Category' : selected;

            // Set selected category for filtering logic
            dropdownButton.setAttribute('data-selected', selected.toLowerCase());

            filterTable();
        });
    });

    function filterTable() {
        const filter = document.getElementById('searchBox').value.toLowerCase();
        const selectedCategory = document.getElementById('categoryDropdown').getAttribute('data-selected');

        document.querySelectorAll('#productTable tbody tr').forEach(row => {
            const name = row.querySelector('.product-name').textContent.toLowerCase();
            const category = row.querySelector('.product-category').textContent.toLowerCase();

            const matchesText = name.includes(filter) || category.includes(filter);
            const matchesCategory = selectedCategory === 'all' || selectedCategory === category;

            row.style.display = matchesText && matchesCategory ? '' : 'none';
        });
    }
</script>



<jsp:include page="/footer.jsp"/>
