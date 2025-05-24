<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:include page="/header.jsp"></jsp:include>
<%
    request.setAttribute("pageTitle", "Login - Synex POS");
%>

<style>
    .login-container {
        max-width: 400px;
        margin: 50px auto;
        padding: 30px;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
        border-radius: 10px;
        background-color: #ffffff;
    }

    .login-logo {
        display: block;
        margin: 0 auto 20px;
        width: 100px;
    }
</style>

<div class="login-container">
    <img src="img/SYOS.png" alt="SYOS Logo" class="login-logo">

    <h4 class="text-center mb-4">Login to Synex POS</h4>

    <% String error = (String) request.getAttribute("error"); %>
    <% if (error != null) { %>
        <div class="alert alert-danger text-center py-2"><%= error %></div>
    <% } %>

    <form action="login" method="post">
        <div class="mb-3">
            <label for="username" class="form-label">Username:</label>
            <input type="text" id="username" name="username" class="form-control" required>
        </div>

        <div class="mb-3">
            <label for="password" class="form-label">Password:</label>
            <input type="password" id="password" name="password" class="form-control" required>
        </div>

        <button type="submit" class="btn btn-primary w-100">Login</button>
    </form>
</div>

<jsp:include page="/footer.jsp"></jsp:include>
