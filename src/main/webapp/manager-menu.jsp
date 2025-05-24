<%
    request.setAttribute("pageTitle", "Manager Dashboard");
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
    .dashboard-card button {
        background: none;
        border: none;
        padding: 0;
        width: 100%;
        text-align: left;
    }
</style>

<div class="container dashboard-container">
    <h2 class="dashboard-title">Manager Dashboard</h2>

    <div class="row g-4">
        <div class="col-md-6">
            <a href="${pageContext.request.contextPath}/stocks" class="text-decoration-none">
                <div class="card dashboard-card">
                    <div class="card-body">
                        <i class="bi bi-box-seam-fill"></i>
                        <h5 class="card-title">Manage Stocks</h5>
                    </div>
                </div>
            </a>
        </div>

        <div class="col-md-6">
            <a href="${pageContext.request.contextPath}/admin/products" class="text-decoration-none">
                <div class="card dashboard-card">
                    <div class="card-body">
                        <i class="bi bi-basket-fill"></i>
                        <h5 class="card-title">View Products</h5>
                    </div>
                </div>
            </a>
        </div>

        <div class="col-md-6">
            <a href="${pageContext.request.contextPath}/admin/categories" class="text-decoration-none">
                <div class="card dashboard-card">
                    <div class="card-body">
                        <i class="bi bi-tags-fill"></i>
                        <h5 class="card-title">View Categories</h5>
                    </div>
                </div>
            </a>
        </div>

        <div class="col-md-6">
            <a href="${pageContext.request.contextPath}/admin/reports" class="text-decoration-none">
                <div class="card dashboard-card">
                    <div class="card-body">
                        <i class="bi bi-graph-up-arrow"></i>
                        <h5 class="card-title">Generate Reports</h5>
                    </div>
                </div>
            </a>
        </div>

        <div class="col-md-6">
            <div class="card dashboard-card">
                <div class="card-body">
                    <i class="bi bi-arrow-repeat"></i>
                    <form id="reshelveForm" action="${pageContext.request.contextPath}/reshelve" method="post" class="w-100">
                        <button type="button" class="card-title" data-bs-toggle="modal" data-bs-target="#reshelveConfirmModal">
                            Reshelve Items
                        </button>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-md-6">
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

<!-- Reshelve Confirmation Modal -->
<div class="modal fade" id="reshelveConfirmModal" tabindex="-1" aria-labelledby="reshelveConfirmModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="reshelveConfirmModalLabel">Confirm Reshelving</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        Are you sure you want to reshelve items? This action cannot be undone.
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
        <button type="submit" form="reshelveForm" class="btn btn-danger">Yes, Reshelve</button>
      </div>
    </div>
  </div>
</div>

<jsp:include page="/footer.jsp"></jsp:include>
