<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Register - Synex POS</title>
</head>
<body>
<h2>Create a New Account</h2>

<% String error = (String) request.getAttribute("error"); %>
<% if (error != null) { %>
    <p style="color:red;"><%= error %></p>
<% } %>

<% String success = (String) request.getAttribute("success"); %>
<% if (success != null) { %>
    <p style="color:green;"><%= success %></p>
<% } %>

<form action="register" method="post">
    <label>Username:</label><br/>
    <input type="text" name="username" required><br/><br/>

    <label>Password:</label><br/>
    <input type="password" name="password" required><br/><br/>

    <label>Role:</label><br/>
    <select name="role">
        <option value="cashier">Cashier</option>
    </select><br/><br/>

    <input type="submit" value="Register">
</form>
<p><a href="login">Back to Login</a></p>
</body>
</html>
