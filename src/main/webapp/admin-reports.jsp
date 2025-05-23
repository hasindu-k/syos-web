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
		<c:if test="${role == 'Admin'}">
		    <a href="${pageContext.request.contextPath}/admin" class="btn btn-primary">Back to Admin Menu</a>
		</c:if>
		<c:if test="${role == 'Manager'}">
		    <a href="${pageContext.request.contextPath}/menu" class="btn btn-primary">Back to Menu</a>
		</c:if>
<h1 class="mb-4 text-primary">Admin Reports</h1>

    <form class="row g-3 align-items-center mb-4" method="get" action="${pageContext.request.contextPath}/admin/reports">
        <div class="col-auto">
            <label for="type" class="form-label">Report Type:</label>
            <select name="type" id="type" class="form-select" required onchange="handleTypeChange()">
			    <option value="">Select Report</option>
			    <option value="totalSale" ${param.type == 'totalSale' ? 'selected' : ''}>Total Sales</option>
			    <option value="reshelved" ${param.type == 'reshelved' ? 'selected' : ''}>Reshelved Items</option>
			    <option value="reorderLevels" ${param.type == 'reorderLevels' ? 'selected' : ''}>Reorder Levels</option>
			    <option value="stockBatch" ${param.type == 'stockBatch' ? 'selected' : ''}>Stock Batch-Wise</option>
			</select>
        </div>
        
        <div class="col-auto">
		    <label for="date" class="form-label">Date:</label>
		    <input type="date" name="date" id="date" class="form-control" disabled />
		</div>
        
        <div class="col-auto">
		    <button type="submit" class="btn btn-primary mt-4">Generate</button>
		</div>
		
		<div class="col-auto">
		    <a href="${pageContext.request.contextPath}/admin/reports" class="btn btn-secondary mt-4">Clear Filters</a>
		</div>
        
    </form>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <c:if test="${not empty reportTitle}">
        <h2 class="mb-3">${reportTitle}</h2>
    </c:if>

    <div class="table-responsive">
        <c:if test="${not empty reportData}">
            <table class="table table-bordered table-hover">
                <thead class="table-light">
                    <tr>
                        <c:forEach var="col" items="${reportData[0].keySet()}">
                            <th>${col}</th>
                        </c:forEach>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="row" items="${reportData}" varStatus="loop">
                        <c:if test="${!loop.first}">
                            <tr>
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

    <a href="${pageContext.request.contextPath}/admin" class="btn btn-secondary mt-4">Back to Admin Menu</a>
<jsp:include page="/footer.jsp"/>