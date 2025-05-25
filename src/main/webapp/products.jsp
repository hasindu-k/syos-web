<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.concurrent.atomic.AtomicInteger" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    request.setAttribute("pageTitle", "Product List");
    Map<Integer, AtomicInteger> cart = (Map<Integer, AtomicInteger>) session.getAttribute("cart");
    int itemCount = 0;
    if (cart != null) {
        for (AtomicInteger qty : cart.values()) {
            itemCount += qty.get();
        }
    }
%>

<jsp:include page="/header.jsp"/>

<c:set var="userRole" value="${sessionScope.role}" />

<!-- Header Bar -->
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

<!-- Filters -->
<div class="d-flex justify-content-between align-items-center mb-4 gap-2 flex-wrap">
    <div class="input-group w-100 w-md-50">
        <span class="input-group-text bg-white border-end-0">
            <i class="bi bi-search text-muted"></i>
        </span>
        <input type="text" id="searchBox" class="form-control form-control-sm border-start-0" placeholder="Search products...">
    </div>

    <div class="dropdown">
        <button class="btn btn-outline-secondary btn-sm dropdown-toggle"
                type="button"
                id="categoryDropdown"
                data-bs-toggle="dropdown"
                data-selected="all"
                aria-expanded="false">
            Filter by Category
        </button>
        <ul class="dropdown-menu" aria-labelledby="categoryDropdown">
            <li><a class="dropdown-item category-option" href="#" data-category="all">All Categories</a></li>
            <c:forEach var="cat" items="${categories}">
                <li><a class="dropdown-item category-option" href="#" data-category="${cat.name.toLowerCase()}">${cat.name}</a></li>
            </c:forEach>
        </ul>
    </div>

    <a href="menu" class="btn btn-outline-secondary btn-sm">
        Back to Menu
    </a>
</div>

<!-- Table View for Staff -->
<c:if test="${userRole != 'Customer'}">
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
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="p" items="${products}" varStatus="status">
                                <tr>
                                    <td>${status.index + 1}</td>
                                    <td class="product-name">${p.product.name}</td>
                                    <td class="product-category">${p.categoryName}</td>
                                    <td>${p.product.price}</td>
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
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <div id="noTableResults" class="text-center text-muted p-4 d-none">
                        <i class="bi bi-search fs-4"></i>
                        <p class="mt-2 mb-0">No products match your filter.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="p-4 text-center text-muted">
                        <p>No products available at the moment.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</c:if>

<!-- Card View for Customers -->
<c:if test="${userRole == 'Customer'}">
    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-3" id="productCardContainer">
        <c:forEach var="p" items="${products}">
            <div class="col product-card"
                 data-name="${p.product.name.toLowerCase()}"
                 data-category="${p.categoryName.toLowerCase()}">
                <div class="card h-100 shadow-sm border rounded-4 overflow-hidden">
                    <img src="<%=request.getContextPath()%>/img/bucket.png" alt="SYOS Logo" class="logo-img" />
                    <div class="card-body d-flex flex-column justify-content-between">
                        <h5 class="card-title text-success fw-bold text-center mb-2">
                            ${p.product.name}
                        </h5>
                        <p class="card-text mb-1">
                            <strong>Category:</strong> ${p.categoryName}<br>
                            <strong>Price:</strong> LKR ${p.product.price}<br>
                            <strong>Stock:</strong>
                            <span class="badge bg-${p.stockQuantity > 0 ? 'success' : 'danger'}">${p.stockQuantity}</span><br>
                            <strong>Status:</strong>
                            <span class="text-${p.product.status == 'Active' ? 'success' : 'secondary'} fw-semibold">
                                ${p.product.status}
                            </span>
                        </p>
                        <div class="mt-3 d-flex justify-content-between align-items-center gap-2">
                            <form action="cart" method="post" class="flex-grow-1">
                                <input type="hidden" name="action" value="add" />
                                <input type="hidden" name="productId" value="${p.product.id}" />
                                <button type="submit" class="btn btn-sm btn-green w-100">+</button>
                            </form>
                            <form action="cart" method="post" class="flex-grow-1">
                                <input type="hidden" name="action" value="remove" />
                                <input type="hidden" name="productId" value="${p.product.id}" />
                                <button type="submit" class="btn btn-sm btn-red w-100">−</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
    <div id="noCardResults" class="text-center text-muted p-4 d-none">
        <i class="bi bi-search fs-4"></i>
        <p class="mt-2 mb-0">No products match your filter.</p>
    </div>
</c:if>

<!-- JS Filtering Logic -->
<script>
    document.getElementById('searchBox').addEventListener('keyup', filterProducts);

    document.querySelectorAll('.category-option').forEach(item => {
        item.addEventListener('click', function (e) {
            e.preventDefault();
            const selected = this.getAttribute('data-category');
            const dropdownButton = document.getElementById('categoryDropdown');
            dropdownButton.innerText = selected === 'all' ? 'Filter by Category' : selected;
            dropdownButton.setAttribute('data-selected', selected);
            filterProducts();
        });
    });

    function filterProducts() {
        const filterText = document.getElementById('searchBox').value.toLowerCase();
        const selectedCategory = (document.getElementById('categoryDropdown').getAttribute('data-selected') || 'all').toLowerCase();

        // Table View
        let tableMatches = 0;
        const tableRows = document.querySelectorAll('#productTable tbody tr');
        tableRows.forEach(row => {
            const name = row.querySelector('.product-name')?.textContent.toLowerCase() || "";
            const category = row.querySelector('.product-category')?.textContent.toLowerCase() || "";
            const matchesText = name.includes(filterText) || category.includes(filterText);
            const matchesCategory = selectedCategory === 'all' || selectedCategory === category;
            const match = matchesText && matchesCategory;
            row.style.display = match ? '' : 'none';
            if (match) tableMatches++;
        });
        const noTable = document.getElementById('noTableResults');
        if (noTable) noTable.classList.toggle('d-none', tableMatches > 0);

        // Card View
        let cardMatches = 0;
        const cards = document.querySelectorAll('.product-card');
        cards.forEach(card => {
            const name = card.getAttribute('data-name') || "";
            const category = card.getAttribute('data-category') || "";
            const matchesText = name.includes(filterText) || category.includes(filterText);
            const matchesCategory = selectedCategory === 'all' || selectedCategory === category;
            const match = matchesText && matchesCategory;
            card.style.display = match ? '' : 'none';
            if (match) cardMatches++;
        });
        const noCard = document.getElementById('noCardResults');
        if (noCard) noCard.classList.toggle('d-none', cardMatches > 0);
    }
</script>

<jsp:include page="/footer.jsp"/>

<style>
    .logo-img {
        width: 80px;
        height: auto;
        margin: 0 auto;
        display: block;
        padding-top: 10px;
    }

    .btn-green {
        background-color: #28a745;
        color: white;
        border: 1px solid #28a745;
    }

    .btn-green:hover {
        background-color: transparent;
        color: #28a745;
    }

    .btn-red {
        background-color: #dc3545;
        color: white;
        border: 1px solid #dc3545;
    }

    .btn-red:hover {
        background-color: transparent;
        color: #dc3545;
    }
</style>
