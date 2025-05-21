<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error - Synex POS</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f4f6f8;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .error-container {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
            border-radius: 10px;
            padding: 30px;
            max-width: 500px;
            width: 100%;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
            text-align: center;
        }

        h2 {
            margin-top: 0;
            margin-bottom: 15px;
            font-size: 24px;
        }

        p {
            font-size: 16px;
            margin-bottom: 25px;
        }

        .button {
            display: inline-block;
            padding: 10px 20px;
            background-color: #721c24;
            color: white;
            text-decoration: none;
            font-weight: bold;
            border-radius: 6px;
            transition: background-color 0.3s ease;
        }

        .button:hover {
            background-color: #501418;
        }
    </style>
</head>
<body>

<div class="error-container">
    <h2>Error</h2>
    <p><%= request.getAttribute("error") != null ? request.getAttribute("error") : "An unexpected error occurred." %></p>
    <a href="login" class="button">Back to Login</a>
</div>

</body>
</html>
