<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    request.setAttribute("pageTitle", "Manage Categories - Admin");
%>
<jsp:include page="/header.jsp"/>

<div class="container my-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="fw-bold text-primary">📂 Manage Categories</h2>
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
            <button class="btn btn-success" onclick="showAddCategoryModal()">
                <i class="bi bi-plus-circle"></i> Add New Category
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
                        <th>Description</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${categories}" var="category">
                    <tr>
                        <td class="text-muted">${category.id}</td>
                        <td class="fw-semibold">${category.name}</td>
                        <td>${category.description}</td>
                        <td>
                            <button class="btn btn-sm btn-outline-warning me-1" onclick="editCategory(${category.id})">
                                <i class="bi bi-pencil-square"></i>Edit
                            </button>
                            <button class="btn btn-sm btn-outline-danger" onclick="deleteCategory(${category.id})">
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

<!-- Add/Edit Category Modal -->
<div class="modal fade" id="categoryModal" tabindex="-1" aria-labelledby="modalTitle" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content border-0 shadow">
      <form id="categoryForm" onsubmit="handleCategorySubmit(event)">
        <input type="hidden" name="id" id="categoryId" value="">
        <div class="modal-header bg-primary text-white">
          <h5 class="modal-title" id="categoryModalTitle">Add New Category</h5>
          <button type="button" class="btn-close" aria-label="Close" onclick="closeCategoryModal()"></button>
        </div>
        <div class="modal-body px-4 py-3">
          <div class="mb-3">
            <label for="name" class="form-label">Category Name <span class="text-danger">*</span></label>
            <input type="text" name="name" class="form-control" required>
          </div>
          <div class="mb-3">
            <label for="description" class="form-label">Description</label>
            <textarea name="description" class="form-control" rows="3"></textarea>
          </div>
        </div>
        <div class="modal-footer bg-light">
          <button type="button" class="btn btn-outline-secondary" onclick="closeCategoryModal()">Cancel</button>
          <button type="submit" class="btn btn-primary">Save Category</button>
        </div>
      </form>
    </div>
  </div>
</div>

<jsp:include page="/footer.jsp"/>
