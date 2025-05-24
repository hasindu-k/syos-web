<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setAttribute("pageTitle", "Admin Dashboard");
%>
<jsp:include page="/header.jsp"></jsp:include>

<!-- Google Fonts + Bootstrap + Icons -->
<link href="https://fonts.googleapis.com/css?family=Poppins:400,600,700&display=swap" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">

<style>
    body {
        background: #f4f6fa;
        font-family: 'Poppins', sans-serif;
    }
    .dashboard-container {
        max-width: 900px;
        margin: 50px auto;
    }
    .dashboard-title {
        font-size: 2.2rem;
        font-weight: 700;
        color: #1976d2;
        text-align: center;
        margin-bottom: 40px;
    }
    .dashboard-card {
        background: #ffffff;
        border: none;
        border-radius: 18px;
        box-shadow: 0 6px 24px rgba(0,0,0,0.08);
        transition: transform 0.2s ease;
    }
    .dashboard-card:hover {
        transform: translateY(-5px);
    }
    .dashboard-card .card-body {
        display: flex;
        align-items: center;
        gap: 1rem;
        padding: 1.5rem;
    }
    .dashboard-card i {
        font-size: 2rem;
        color: #1976d2;
        width: 2.5rem;
        text-align: center;
    }
    .dashboard-card .card-title {
        margin: 0;
        font-size: 1.2rem;
        font-weight: 600;
        color: #333;
    }
    .dashboard-card.logout-card i {
        color: #e53935;
    }
    .dashboard-card.logout-card .card-title {
        color: #e53935;
    }
</style>

<div class="container dashboard-container">
    <h2 class="dashboard-title">Welcome, Admin!</h2>

    <div class="row g-4">
        <div class="col-md-6">
            <a href="${pageContext.request.contextPath}/admin/products" class="text-decoration-none">
                <div class="card dashboard-card">
                    <div class="card-body">
                        <i class="bi bi-box-seam"></i>
                        <h5 class="card-title">Manage Products</h5>
                    </div>
                </div>
            </a>
        </div>
        <div class="col-md-6">
            <a href="${pageContext.request.contextPath}/admin/categories" class="text-decoration-none">
                <div class="card dashboard-card">
                    <div class="card-body">
                        <i class="bi bi-tags"></i>
                        <h5 class="card-title">Manage Categories</h5>
                    </div>
                </div>
            </a>
        </div>
        <div class="col-md-6">
            <a href="${pageContext.request.contextPath}/admin/users" class="text-decoration-none">
                <div class="card dashboard-card">
                    <div class="card-body">
                        <i class="bi bi-people"></i>
                        <h5 class="card-title">Manage Users</h5>
                    </div>
                </div>
            </a>
        </div>
        <div class="col-md-6">
            <a href="${pageContext.request.contextPath}/admin/reports" class="text-decoration-none">
                <div class="card dashboard-card">
                    <div class="card-body">
                        <i class="bi bi-bar-chart-line"></i>
                        <h5 class="card-title">View Reports</h5>
                    </div>
                </div>
            </a>
        </div>
        <div class="col-md-6">
            <a href="${pageContext.request.contextPath}/admin/logout" class="text-decoration-none">
                <div class="card dashboard-card logout-card">
                    <div class="card-body">
                        <i class="bi bi-box-arrow-right"></i>
                        <h5 class="card-title">Logout</h5>
                    </div>
                </div>
            </a>
        </div>
    </div>
</div>

<jsp:include page="/footer.jsp"></jsp:include>
