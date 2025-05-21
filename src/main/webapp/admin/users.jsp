<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Manage Users - Admin</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f0f2f5;
            margin: 0;
            padding: 20px;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            background-color: #ffffff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        h1 {
            color: #2c3e50;
            margin-bottom: 20px;
        }

        .action-bar {
            margin-bottom: 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .btn {
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-weight: 500;
            text-decoration: none;
            display: inline-block;
        }

        .btn-primary {
            background-color: #3498db;
            color: white;
        }

        .btn-primary:hover {
            background-color: #2980b9;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #f8f9fa;
            font-weight: 600;
        }

        tr:hover {
            background-color: #f5f6f7;
        }

        .actions {
            display: flex;
            gap: 8px;
        }

        .btn-edit {
            background-color: #2ecc71;
            color: white;
        }

        .btn-delete {
            background-color: #e74c3c;
            color: white;
        }

        .status-active {
            color: #27ae60;
            font-weight: 500;
        }

        .status-inactive {
            color: #e74c3c;
            font-weight: 500;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Manage Users</h1>
        
        <div class="action-bar">
            <a href="${pageContext.request.contextPath}/admin" class="btn btn-primary">Back to Admin Menu</a>
            <button class="btn btn-primary" onclick="showAddUserModal()">Add New User</button>
        </div>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Role</th>
                    <th>Status</th>
                    <th>Last Login</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${users}" var="user">
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.username}</td>
                        <td>${user.email}</td>
                        <td>${user.role}</td>
                        <td>
                            <span class="status-${user.active ? 'active' : 'inactive'}">
                                ${user.active ? 'Active' : 'Inactive'}
                            </span>
                        </td>
                        <td>${user.lastLogin}</td>
                        <td class="actions">
                            <button class="btn btn-edit" onclick="editUser(${user.id})">Edit</button>
                            <button class="btn btn-delete" onclick="deleteUser(${user.id})">Delete</button>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <script>
        function showAddUserModal() {
            // TODO: Implement add user modal
            alert('Add user functionality to be implemented');
        }

        function editUser(id) {
            // TODO: Implement edit user functionality
            alert('Edit user ' + id + ' functionality to be implemented');
        }

        function deleteUser(id) {
            if (confirm('Are you sure you want to delete this user?')) {
                // TODO: Implement delete user functionality
                alert('Delete user ' + id + ' functionality to be implemented');
            }
        }
    </script>
</body>
</html> 