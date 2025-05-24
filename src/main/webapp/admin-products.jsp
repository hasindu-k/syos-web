<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    request.setAttribute("pageTitle", "Manage Products - Admin");
%>
<jsp:include page="/header.jsp"/>

<div class="container my-5 product-container" data-context="${pageContext.request.contextPath}">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="fw-bold text-primary">🛍️ Manage Products</h2>
        <div>
            <c:if test="${role == 'Admin'}">
                <a href="${pageContext.request.contextPath}/admin" class="btn btn-outline-primary me-2">
                    <i class="bi bi-arrow-left-circle"></i> Admin Menu
                </a>
            </c:if>
            <c:if test="${role == 'Manager'}">
                <a href="${pageContext.request.contextPath}/menu" class="btn btn-outline-primary me-2">
                    <i class="bi bi-arrow-left-circle"></i> Main Menu
                </a>
            </c:if>
            <button class="btn btn-success" onclick="showAddProductModal()">
                <i class="bi bi-plus-circle"></i> Add New Product
            </button>
        </div>
    </div>

    <div class="card shadow-sm border-0">
        <div class="card-body p-0">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Category</th>
                        <th>Price</th>
                        <th>Stock</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${products}" var="vm">
                        <tr>
                            <td class="text-muted">${vm.product.id}</td>
                            <td class="fw-semibold">${vm.product.name}</td>
                            <td>${vm.categoryName}</td>
                            <td>$${vm.product.price}</td>
                            <td>${vm.stockQuantity}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${vm.product.status == 'Available'}">
                                        <span class="badge bg-success">Available</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary">Unavailable</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <button class="btn btn-sm btn-outline-warning me-1" onclick="editProduct(${vm.product.id})">
                                    <i class="bi bi-pencil-square"></i>Edit
                                </button>
                                <button class="btn btn-sm btn-outline-danger" onclick="deleteProduct(${vm.product.id})">
                                    <i class="bi bi-trash3"></i>Delete
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Add/Edit Product Modal -->
<div class="modal fade" id="productModal" tabindex="-1" aria-labelledby="productModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content border-0 shadow">
      <form id="productForm" onsubmit="handleProductSubmit(event)">
        <input type="hidden" id="productId" name="id">
        <div class="modal-header bg-primary text-white">
          <h5 class="modal-title" id="productModalTitle">Add New Product</h5>
          <button type="button" class="btn-close" aria-label="Close" onclick="closeProductModal()"></button>
        </div>
        <div class="modal-body px-4 py-3">
          <div class="row g-3">
            <div class="col-md-6">
              <label for="name" class="form-label">Product Name <span class="text-danger">*</span></label>
              <input type="text" class="form-control" id="name" name="name" required>
            </div>
            <div class="col-md-6">
              <label for="price" class="form-label">Price ($) <span class="text-danger">*</span></label>
              <input type="number" class="form-control" id="price" name="price" step="0.01" min="0" required>
            </div>
            <div class="col-md-6">
              <label for="unitId" class="form-label">Unit <span class="text-danger">*</span></label>
              <select id="unitId" name="unitId" class="form-select" required>
                  <option value="1">Each</option>
                  <option value="2">Kilogram</option>
                  <option value="3">Liter</option>
              </select>
            </div>
            <div class="col-md-6">
              <label for="categoryId" class="form-label">Category <span class="text-danger">*</span></label>
              <select id="categoryId" name="categoryId" class="form-select" required>
                  <c:forEach items="${categories}" var="category">
                      <option value="${category.id}">${category.name}</option>
                  </c:forEach>
              </select>
            </div>
            <div class="col-md-6">
              <label for="status" class="form-label">Status <span class="text-danger">*</span></label>
              <select id="status" name="status" class="form-select" required>
                  <option value="Available">Available</option>
                  <option value="Unavailable">Unavailable</option>
              </select>
            </div>
            <div class="col-md-6">
              <label for="warehouseId" class="form-label">Warehouse</label>
              <select id="warehouseId" name="warehouseId" class="form-select">
                  <option value="">Select Warehouse</option>
                  <option value="1">Main Warehouse</option>
                  <option value="2">Secondary Warehouse</option>
              </select>
            </div>
            <div class="col-md-6">
              <label for="note" class="form-label">Note</label>
              <input type="text" id="note" name="note" class="form-control">
            </div>
            <div class="col-md-6">
              <label for="stockAlert" class="form-label">Stock Alert Level <span class="text-danger">*</span></label>
              <input type="number" id="stockAlert" name="stockAlert" class="form-control" required min="0">
            </div>
            <div class="col-md-12">
              <label for="supplierId" class="form-label">Supplier</label>
              <select id="supplierId" name="supplierId" class="form-select">
                  <option value="">Select Supplier</option>
                  <option value="1">Supplier 1</option>
                  <option value="2">Supplier 2</option>
              </select>
            </div>
          </div>
        </div>
        <div class="modal-footer bg-light">
          <button type="button" class="btn btn-outline-secondary" onclick="closeProductModal()">Cancel</button>
          <button type="submit" class="btn btn-primary">Save Product</button>
        </div>
      </form>
    </div>
  </div>
</div>

<script>
  const contextPath = '${pageContext.request.contextPath}';
</script>

<jsp:include page="/footer.jsp"/>
