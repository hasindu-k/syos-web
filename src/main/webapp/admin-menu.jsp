<%
    request.setAttribute("pageTitle", "Admin Dashboard");
%>
<jsp:include page="/header.jsp"></jsp:include>

<div class="container mt-5">
    <div class="card shadow">
        <div class="card-body">
            <h2 class="card-title text-center mb-4">Welcome, Admin!</h2>
            <ul class="list-group">
                <li class="list-group-item">
                    <a href="${pageContext.request.contextPath}/admin/products" class="text-decoration-none">Manage Products</a>
                </li>
                <li class="list-group-item">
                    <a href="${pageContext.request.contextPath}/admin/categories" class="text-decoration-none">Manage Categories</a>
                </li>
                <li class="list-group-item">
                    <a href="${pageContext.request.contextPath}/admin/users" class="text-decoration-none">Manage Users</a>
                </li>
                <li class="list-group-item">
                    <a href="${pageContext.request.contextPath}/admin/reports" class="text-decoration-none">View Reports</a>
                </li>
                <li class="list-group-item">
                    <a href="${pageContext.request.contextPath}/admin/logout" class="text-danger text-decoration-none">Logout</a>
                </li>
            </ul>
        </div>
    </div>
</div>

<jsp:include page="/footer.jsp"></jsp:include>
