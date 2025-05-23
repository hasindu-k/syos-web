<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Manage Categories - Admin</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f0f2f5;
            margin: 0;
            padding: 20px;
        }

        .container {
            max-width: 1000px;
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

        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
        }

        .modal-content {
            background-color: white;
            margin: 10% auto;
            padding: 20px;
            border-radius: 8px;
            width: 80%;
            max-width: 500px;
        }

        .form-group {
            margin-bottom: 15px;
        }

        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: 500;
        }

        .form-group input, .form-group textarea {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }

        .form-group textarea {
            height: 100px;
            resize: vertical;
        }

        .modal-buttons {
            display: flex;
            justify-content: flex-end;
            gap: 10px;
            margin-top: 20px;
        }

        .btn-cancel {
            background-color: #95a5a6;
            color: white;
        }

        .btn-cancel:hover {
            background-color: #7f8c8d;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Manage Categories</h1>

    <div class="action-bar">
        <a href="${pageContext.request.contextPath}/admin" class="btn btn-primary">Back to Admin Menu</a>
        <button class="btn btn-primary" onclick="showAddCategoryModal()">Add New Category</button>
    </div>

    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Description</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${categories}" var="category">
            <tr>
                <td>${category.id}</td>
                <td>${category.name}</td>
                <td>${category.description}</td>
                <td class="actions">
                    <button class="btn btn-edit" onclick="editCategory(${category.id})">Edit</button>
                    <button class="btn btn-delete" onclick="deleteCategory(${category.id})">Delete</button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<!-- Modal -->
<div id="categoryModal" class="modal">
    <div class="modal-content">
        <h2 id="modalTitle">Add Category</h2>
        <form id="categoryForm" onsubmit="handleCategorySubmit(event)">
            <input type="hidden" name="id" id="categoryId" value="">
            <div class="form-group">
                <label>Category Name:</label>
                <input type="text" name="name" required>
            </div>
            <div class="form-group">
                <label>Description:</label>
                <textarea name="description" required></textarea>
            </div>
            <div class="modal-buttons">
                <button type="button" class="btn btn-cancel" onclick="closeCategoryModal()">Cancel</button>
                <button type="submit" class="btn btn-primary">Submit</button>
            </div>
        </form>
    </div>
</div>

<script>
    const modal = document.getElementById('categoryModal');
    const form = document.getElementById('categoryForm');
    const modalTitle = document.getElementById('modalTitle');

    function showAddCategoryModal() {
        modalTitle.textContent = 'Add New Category';
        form.reset();
        document.getElementById('categoryId').value = '';
        modal.style.display = 'block';
    }

    function editCategory(id) {
    	console.log("Editing category ID:", id);
        modalTitle.textContent = 'Edit Category';
        
        const url = "${pageContext.request.contextPath}/admin/categories/get?id=" + id;
        console.log("GET URL:", url); // ← Add this debug

        fetch(url)
            .then(response => response.json())
            .then(data => {
                form.name.value = data.name;
                form.description.value = data.description;
                document.getElementById('categoryId').value = data.id;
                modal.style.display = 'block';
            })
            .catch(error => console.error('Error fetching category:', error));
    }

    function closeCategoryModal() {
        modal.style.display = 'none';
    }

    function handleCategorySubmit(event) {
        event.preventDefault();

        const formData = new FormData(form);
        const categoryId = formData.get('id');
        const url = categoryId ?
            `${pageContext.request.contextPath}/admin/categories/update` :
            `${pageContext.request.contextPath}/admin/categories/add`;

        fetch(url, {
            method: 'POST',
            body: new URLSearchParams(formData)
        })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    location.reload();
                } else {
                    alert('Operation failed: ' + (data.message || 'Please try again.'));
                }
            })
            .catch(err => {
                console.error(err);
                alert('An error occurred. Please try again.');
            });
    }

    function deleteCategory(id) {
        if (confirm('Are you sure you want to delete this category?')) {
            fetch(`${pageContext.request.contextPath}/admin/categories/delete`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: 'id=' + id
            })
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
                        location.reload();
                    } else {
                        alert('Failed to delete category.');
                    }
                })
                .catch(err => {
                    console.error(err);
                    alert('An error occurred. Please try again.');
                });
        }
    }

    window.onclick = function (event) {
        if (event.target === modal) {
            closeCategoryModal();
        }
    };
</script>
</body>
</html>
