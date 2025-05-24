<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/header.jsp">
    <jsp:param name="pageTitle" value="User Management"/>
</jsp:include>

<div class="container py-4">
    <!-- Top Bar -->
    <div class="d-flex justify-content-between align-items-center mb-4">
         <h2 class="fw-bold text-primary">👥 Manage Users</h2>
        <a href="${pageContext.request.contextPath}/admin" class="btn btn-outline-primary">
            <i class="bi bi-arrow-left"></i> Back to Admin Menu
        </a>
    </div>

    <!-- User Table -->
    <div class="card shadow-sm">
        <div class="card-body p-0">
            <table class="table table-hover table-bordered mb-0">
                <thead class="table-light text-center">
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Current Role</th>
                        <th>Change Role</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="user" items="${users}">
                        <tr class="align-middle text-center">
                            <td>${user.id}</td>
                            <td>${user.username}</td>
                            <td>
                                <span class="badge bg-info text-dark">${user.role}</span>
                            </td>
                            <td>
                                <form action="${pageContext.request.contextPath}/admin/users/updateRole" method="post" class="d-flex justify-content-center align-items-center">
                                    <input type="hidden" name="userId" value="${user.id}" />
                                    <select name="newRole" class="form-select form-select-sm me-2" style="width: auto;">
                                        <c:forEach var="role" items="${['Admin', 'Manager', 'Cashier', 'Customer']}">
                                            <option value="${role}" ${user.role == role ? 'selected' : ''}>${role}</option>
                                        </c:forEach>
                                    </select>
                                    <button type="submit" class="btn btn-sm btn-outline-secondary">Update</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/footer.jsp"/>
