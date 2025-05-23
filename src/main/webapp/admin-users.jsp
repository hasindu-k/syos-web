<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/header.jsp">
    <jsp:param name="pageTitle" value="User Management"/>
</jsp:include>

<h1>User Management</h1>

<table class="table table-bordered table-hover">
    <thead class="table-light">
        <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Current Role</th>
            <th>Change Role</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="user" items="${users}">
            <tr>
                <td>${user.id}</td>
                <td>${user.username}</td>
                <td>${user.role}</td>
                <td>
                    <form action="${pageContext.request.contextPath}/admin/users/updateRole" method="post" class="d-flex">
                        <input type="hidden" name="userId" value="${user.id}" />
                        <select name="newRole" class="form-select form-select-sm me-2">
                            <c:forEach var="role" items="${['Admin', 'Manager', 'Cashier', 'Customer']}">
                                <option value="${role}" ${user.role == role ? 'selected' : ''}>${role}</option>
                            </c:forEach>
                        </select>
                        <button type="submit" class="btn btn-sm btn-secondary">Update</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<a href="${pageContext.request.contextPath}/admin" class="btn btn-primary mt-3">Back to Admin Menu</a>

<jsp:include page="/footer.jsp"/>
