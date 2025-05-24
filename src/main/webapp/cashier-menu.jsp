<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    request.setAttribute("pageTitle", "Cashier Dashboard");
%>
<jsp:include page="/header.jsp"></jsp:include>

<!-- Google Fonts + Bootstrap + Bootstrap Icons CDN -->
<link href="https://fonts.googleapis.com/css?family=Poppins:400,600,700&display=swap" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">

<style>
    body {
        background: #f4f6fa;
        font-family: 'Poppins', sans-serif;
    }
    .dashboard-container {
        max-width: 600px;
        margin: 60px auto;
    }
    .dashboard-title {
        font-size: 2rem;
        font-weight: 700;
        color: #1976d2;
        text-align: center;
        margin-bottom: 30px;
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
        padding: 1.2rem;
    }
    .dashboard-card i {
        font-size: 1.8rem;
        color: #1976d2;
        width: 2rem;
        text-align: center;
    }
    .dashboard-card .card-title {
        margin: 0;
        font-size: 1.2rem;
        font-weight: 600;
        color: #333;
    }
    .dashboard-card.logout-card i,
    .dashboard-card.logout-card .card-title {
        color: #e53935;
    }
    a.text-decoration-none {
        text-decoration: none !important;
    }
</style>

<div class="container dashboard-container">
    <h2 class="dashboard-title">Cashier Dashboard</h2>

    <div class="row g-4">
        <div class="col-12">
            <a href="${pageContext.request.contextPath}/products" class="text-decoration-none">
                <div class="card dashboard-card">
                    <div class="card-body">
                        <i class="bi bi-box-seam-fill"></i>
                        <h5 class="card-title">View Products</h5>
                    </div>
                </div>
            </a>
        </div>

        <div class="col-12">
            <a href="${pageContext.request.contextPath}/billing" class="text-decoration-none">
                <div class="card dashboard-card">
                    <div class="card-body">
                        <i class="bi bi-receipt-cutoff"></i>
                        <h5 class="card-title">Create Bill</h5>
                    </div>
                </div>
            </a>
        </div>

        <div class="col-12">
            <a href="${pageContext.request.contextPath}/logout" class="text-decoration-none">
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
