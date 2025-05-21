<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Manage Products - Admin</title>
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
            max-width: 600px;
        }

        .form-group {
            margin-bottom: 15px;
        }

        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: 500;
        }

        .form-group input, .form-group select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
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
        <h1>Manage Products</h1>
        
        <div class="action-bar">
            <a href="${pageContext.request.contextPath}/admin" class="btn btn-primary">Back to Admin Menu</a>
            <button class="btn btn-primary" onclick="showAddProductModal()">Add New Product</button>
        </div>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Category</th>
                    <th>Price</th>
                    <th>Stock</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${products}" var="product">
                    <tr>
                        <td>${product.id}</td>
                        <td>${product.name}</td>
                        <td>${product.category}</td>
                        <td>$${product.price}</td>
                        <td>${product.stock}</td>
                        <td class="actions">
                            <button class="btn btn-edit" onclick="editProduct(${product.id})">Edit</button>
                            <button class="btn btn-delete" onclick="deleteProduct(${product.id})">Delete</button>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <!-- Add/Edit Product Modal -->
    <div id="productModal" class="modal">
        <div class="modal-content">
            <h2 id="modalTitle">Add New Product</h2>
            <form id="productForm" onsubmit="handleProductSubmit(event)">
                <input type="hidden" id="productId" name="id">
                <div class="form-group">
                    <label for="name">Product Name</label>
                    <input type="text" id="name" name="name" required>
                </div>
                <div class="form-group">
                    <label for="unitId">Unit</label>
                    <select id="unitId" name="unitId" required>
                        <option value="1">Each</option>
                        <option value="2">Kilogram</option>
                        <option value="3">Liter</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="categoryId">Category</label>
                    <select id="categoryId" name="categoryId" required>
                        <c:forEach items="${categories}" var="category">
                            <option value="${category.id}">${category.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="status">Status</label>
                    <select id="status" name="status" required>
                        <option value="active">Active</option>
                        <option value="inactive">Inactive</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="warehouseId">Warehouse</label>
                    <select id="warehouseId" name="warehouseId" required>
                        <option value="1">Main Warehouse</option>
                        <option value="2">Secondary Warehouse</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="note">Note</label>
                    <input type="text" id="note" name="note">
                </div>
                <div class="form-group">
                    <label for="stockAlert">Stock Alert Level</label>
                    <input type="number" id="stockAlert" name="stockAlert" required min="0">
                </div>
                <div class="form-group">
                    <label for="supplierId">Supplier</label>
                    <select id="supplierId" name="supplierId" required>
                        <option value="1">Supplier 1</option>
                        <option value="2">Supplier 2</option>
                    </select>
                </div>
                <div class="modal-buttons">
                    <button type="button" class="btn btn-cancel" onclick="closeProductModal()">Cancel</button>
                    <button type="submit" class="btn btn-primary">Save</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        const modal = document.getElementById('productModal');
        const form = document.getElementById('productForm');
        const modalTitle = document.getElementById('modalTitle');

        function showAddProductModal() {
            modalTitle.textContent = 'Add New Product';
            form.reset();
            document.getElementById('productId').value = '';
            modal.style.display = 'block';
        }

        function editProduct(id) {
            modalTitle.textContent = 'Edit Product';
            // TODO: Fetch product details and populate form
            modal.style.display = 'block';
        }

        function closeProductModal() {
            modal.style.display = 'none';
        }

        function handleProductSubmit(event) {
            event.preventDefault();
            const formData = new FormData(form);
            const productId = formData.get('id');
            const url = productId ? 
                '${pageContext.request.contextPath}/admin/products/update' : 
                '${pageContext.request.contextPath}/admin/products/add';

            fetch(url, {
                method: 'POST',
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    window.location.reload();
                } else {
                    alert('Operation failed. Please try again.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('An error occurred. Please try again.');
            });
        }

        function deleteProduct(id) {
            if (confirm('Are you sure you want to delete this product?')) {
                fetch('${pageContext.request.contextPath}/admin/products/delete', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: 'id=' + id
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        window.location.reload();
                    } else {
                        alert('Failed to delete product. Please try again.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('An error occurred. Please try again.');
                });
            }
        }

        // Close modal when clicking outside
        window.onclick = function(event) {
            if (event.target == modal) {
                closeProductModal();
            }
        }
    </script>
</body>
</html> 