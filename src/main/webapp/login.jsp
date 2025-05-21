<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Login - Synex POS</title>
</head>
<body>
<h2>Login to Synex POS</h2>

<% String error = (String) request.getAttribute("error"); %>
<% if (error != null) { %>
    <p style="color:red;"><%= error %></p>
<% } %>

<form action="login" method="post">
    <label for="username">Username:</label><br/>
    <input type="text" id="username" name="username" required><br/><br/>

    <label for="password">Password:</label><br/>
    <input type="password" id="password" name="password" required><br/><br/>

    <input type="submit" value="Login">
</form>
</body>
</html>
