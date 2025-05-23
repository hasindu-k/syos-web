<%
    request.setAttribute("pageTitle", "Manager Dashboard");
%>
<jsp:include page="/header.jsp"></jsp:include>

<div class="container mt-5">
    <div class="card shadow">
        <div class="card-body">
            <h2 class="card-title text-center mb-4">Manager Dashboard</h2>
            <ul class="list-group">
                <li class="list-group-item">
                    <a href="${pageContext.request.contextPath}/stocks" class="text-decoration-none">Manage Stocks</a>
                </li>
                <li class="list-group-item">
                    <a href="${pageContext.request.contextPath}/admin/products" class="text-decoration-none">View Products</a>
                </li>
                <li class="list-group-item">
                    <a href="${pageContext.request.contextPath}/admin/categories" class="text-decoration-none">View Categories</a>
                </li>
                <li class="list-group-item">
                    <a href="${pageContext.request.contextPath}/admin/reports" class="text-decoration-none">Generate Reports</a>
                </li>
                <li class="list-group-item">
                    <button type="button" class="btn btn-link text-decoration-none p-0" data-bs-toggle="modal" data-bs-target="#reshelveConfirmModal">
                        Reshelve Items
                    </button>
                </li>
                <li class="list-group-item">
                    <a href="${pageContext.request.contextPath}/logout" class="text-danger text-decoration-none">Logout</a>
                </li>
            </ul>
        </div>
    </div>
</div>

<!-- Bootstrap Confirmation Modal -->
<div class="modal fade" id="reshelveConfirmModal" tabindex="-1" aria-labelledby="reshelveConfirmModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="reshelveConfirmModalLabel">Confirm Reshelving</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        Are you sure you want to reshelve items? This action cannot be undone.
      </div>
      <div class="modal-footer">
        <form id="reshelveForm" action="${pageContext.request.contextPath}/reshelve" method="post">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
            <button type="submit" class="btn btn-danger">Yes, Reshelve</button>
        </form>
      </div>
    </div>
  </div>
</div>

<jsp:include page="/footer.jsp"></jsp:include>
