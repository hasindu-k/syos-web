<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Customer Menu</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f4f6f8;
            margin: 0;
            display: flex;
            justify-content: center;
            align-items: flex-start;
            min-height: 100vh;
        }

        .container {
            background-color: #ffffff;
            padding: 30px 40px;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            max-width: 600px;
            width: 100%;
            margin: 50px auto;
        }

        h2 {
            color: #2c3e50;
            margin-bottom: 10px;
        }

        p {
            color: #555;
            margin-bottom: 25px;
        }

        ul {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        li {
            margin-bottom: 15px;
        }

        a {
            display: block;
            padding: 12px 18px;
            background-color: #3498db;
            color: white;
            text-decoration: none;
            border-radius: 6px;
            transition: background-color 0.3s ease;
        }

        a:hover {
            background-color: #2980b9;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>Welcome, Customer!</h2>
    <p>Here are your available options:</p>

    <ul>
        <li><a href="products">Browse Products</a></li>
        <li><a href="my-orders">My Orders</a></li>
        <li><a href="account">Account Settings</a></li>
        <li><a href="logout">Logout</a></li>
    </ul>
</div>

</body>
</html>
