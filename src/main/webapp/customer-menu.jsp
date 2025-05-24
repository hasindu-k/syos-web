<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setAttribute("pageTitle", "Customer Dashboard");
%>
<jsp:include page="/header.jsp"></jsp:include>

<!-- Google Fonts + Bootstrap + Bootstrap Icons -->
<link href="https://fonts.googleapis.com/css?family=Poppins:400,600,700&display=swap" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">

<style>
    body {
        background-color: #f4f6fa;
        font-family: 'Poppins', sans-serif;
    }
    .dashboard-container {
        max-width: 650px;
        margin: 60px auto;
    }
    .dashboard-title {
        font-size: 2rem;
        font-weight: 700;
        color: #2e7d32; /* success color theme */
        text-align: center;
        margin-bottom: 20px;
    }
    .dashboard-subtitle {
        text-align: center;
        color: #777;
        margin-bottom: 30px;
    }
    .dashboard-card {
        background-color: #fff;
        border: none;
        border-radius: 18px;
        box-shadow: 0 6px 24px rgba(0, 0, 0, 0.08);
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
        color: #2e7d32;
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
    <h2 class="dashboard-title">Customer Dashboard</h2>
    <p class="dashboard-subtitle">Here are your available options:</p>

    <div class="row g-4">
        <div class="col-12">
            <a href="${pageContext.request.contextPath}/products" class="text-decoration-none">
                <div class="card dashboard-card">
                    <div class="card-body">
                        <i class="bi bi-bag-heart-fill"></i>
                        <h5 class="card-title">Browse Products</h5>
                    </div>
                </div>
            </a>
        </div>

        <div class="col-12">
            <a href="${pageContext.request.contextPath}/my-orders" class="text-decoration-none">
                <div class="card dashboard-card">
                    <div class="card-body">
                        <i class="bi bi-box2-heart-fill"></i>
                        <h5 class="card-title">My Orders</h5>
                    </div>
                </div>
            </a>
        </div>

        <div class="col-12">
            <a href="${pageContext.request.contextPath}/account" class="text-decoration-none">
                <div class="card dashboard-card">
                    <div class="card-body">
                        <i class="bi bi-gear-fill"></i>
                        <h5 class="card-title">Account Settings</h5>
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
