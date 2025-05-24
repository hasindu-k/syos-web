<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    request.setAttribute("pageTitle", "Reports - Admin");
%>
<%
    java.time.LocalDate today = java.time.LocalDate.now();
    String todayStr = today.toString();
%>
<jsp:include page="/header.jsp"/>

<div class="container py-4">
    <!-- Top Bar -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="text-primary mb-0"><i class="bi bi-file-earmark-bar-graph"></i>  Admin Reports</h2>
        <c:if test="${role == 'Admin'}">
            <a href="${pageContext.request.contextPath}/admin" class="btn btn-outline-primary">
                <i class="bi bi-arrow-left"></i> Back to Admin Menu
            </a>
        </c:if>
        <c:if test="${role == 'Manager'}">
            <a href="${pageContext.request.contextPath}/menu" class="btn btn-outline-primary">
                <i class="bi bi-arrow-left"></i> Back to Menu
            </a>
        </c:if>
    </div>

    <!-- Filter Form -->
    <div class="card shadow-sm mb-4">
        <div class="card-body">
            <form class="row g-3 align-items-end" method="get" action="${pageContext.request.contextPath}/admin/reports">
                <div class="col-md-4">
                    <label for="type" class="form-label">Report Type:</label>
                    <select name="type" id="type" class="form-select" required onchange="handleTypeChange()">
					    <option value="">Select Report</option>
					    <option value="totalSale" ${param.type == 'totalSale' ? 'selected' : ''}>Total Sales</option>
					    <option value="reshelved" ${param.type == 'reshelved' ? 'selected' : ''}>Reshelved Items</option>
					    <option value="reorderLevels" ${param.type == 'reorderLevels' ? 'selected' : ''}>Reorder Levels</option>
					    <option value="stockBatch" ${param.type == 'stockBatch' ? 'selected' : ''}>Stock Batch-Wise</option>
					    <option value="bill" ${param.type == 'bill' ? 'selected' : ''}>Bill Report</option> 
					</select>
                </div>
                <div class="col-md-4">
                    <label for="date" class="form-label">Date:</label>
                    <input type="date" name="date" id="date" class="form-control" disabled />
                </div>
                <div class="col-md-2 d-grid">
                    <button type="submit" class="btn btn-primary">Generate</button>
                </div>
                <div class="col-md-2 d-grid">
                    <a href="${pageContext.request.contextPath}/admin/reports" class="btn btn-secondary">Clear Filters</a>
                </div>
            </form>
        </div>
    </div>

    <!-- Alerts -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <!-- Report Title -->
    <c:if test="${not empty reportTitle}">
        <h4 class="text-success mb-3">${reportTitle}</h4>
    </c:if>

    <!-- Report Table -->
    <div class="card shadow-sm">
        <div class="card-body">
            <div class="table-responsive">
            <c:if test="${not empty reportData && fn:length(reportData) > 1}">
		        <p class="text-muted">Total Records: ${fn:length(reportData) - 1}</p>
		    </c:if>
                <c:if test="${not empty reportData}">
                    <table class="table table-bordered table-hover mb-0">
                        <thead class="table-light text-center">
                            <tr>
                                <c:forEach var="col" items="${reportData[0].keySet()}">
                                    <th>${col}</th>
                                </c:forEach>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="row" items="${reportData}" varStatus="loop">
                                <c:if test="${!loop.first}">
                                    <tr class="text-center">
                                        <c:forEach var="col" items="${reportData[0].keySet()}">
                                            <td>${row[col]}</td>
                                        </c:forEach>
                                    </tr>
                                </c:if>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>
                <c:if test="${empty reportData || fn:length(reportData) == 1}">
                    <p class="text-muted">No data available for the selected report.</p>
                </c:if>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/footer.jsp"/>
