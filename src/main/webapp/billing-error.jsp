<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="header.jsp" %>

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">

            <div class="alert alert-danger shadow-lg border-0 rounded-4 p-4">
                <div class="d-flex align-items-center mb-3">
                    <i class="bi bi-x-circle-fill fs-2 text-danger me-3"></i>
                    <h4 class="alert-heading mb-0">Failed to Generate Bill</h4>
                </div>
                <p class="mb-0">We encountered an issue while processing your request. Please double-check your inputs and try again.</p>
            </div>

            <div class="d-grid mt-4">
                <a href="billing" class="btn btn-outline-primary btn-lg shadow-sm">
                    <i class="bi bi-arrow-left-circle me-2"></i> Return to Billing
                </a>
            </div>

        </div>
    </div>
</div>

<%@ include file="footer.jsp" %>
