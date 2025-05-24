</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js" defer></script>

<script defer>
document.addEventListener('DOMContentLoaded', () => {
    const productModal = new bootstrap.Modal(document.getElementById('productModal'));
    const categoryModal = new bootstrap.Modal(document.getElementById('categoryModal'));

    const productForm = document.getElementById('productForm');
    const categoryForm = document.getElementById('categoryForm');
    const productModalTitle = document.getElementById('productModalTitle');
    const categoryModalTitle = document.getElementById('categoryModalTitle');

    const contextPath = ""; // Set manually or dynamically if needed

    // Product modal handlers
    function showAddProductModal() {
        productModalTitle.textContent = 'Add New Product';
        productForm.reset();
        document.getElementById('productId').value = '';
        productModal.show();
    }

    function editProduct(id) {
        productModalTitle.textContent = 'Edit Product';
        productForm.reset();
        document.getElementById('productId').value = id;

        fetch(`${contextPath}/admin/products/get?id=${id}`)
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    productForm.name.value = data.name;
                    productForm.price.value = data.price;
                    productForm.unitId.value = data.unitId;
                    productForm.categoryId.value = data.categoryId;
                    productForm.status.value = data.status;
                    productForm.warehouseId.value = data.warehouseId;
                    productForm.note.value = data.note;
                    productForm.stockAlert.value = data.stockAlert;
                    productForm.supplierId.value = data.supplierId;
                    productModal.show();
                } else {
                    alert('Failed to load product details.');
                    console.error('Fetch error:', data.message);
                }
            })
            .catch(error => {
                console.error('Error fetching product:', error);
                alert('Error occurred while fetching product details.');
            });
    }

    function handleProductSubmit(event) {
        event.preventDefault();
        const formData = new FormData(productForm);
        const productId = formData.get('id');

        const url = productId
            ? `${contextPath}/admin/products/update`
            : `${contextPath}/admin/products/add`;

        fetch(url, {
            method: 'POST',
            body: new URLSearchParams(formData)
        })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    location.reload();
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
            fetch(`${contextPath}/admin/products/delete`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: 'id=' + id
            })
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
                        location.reload();
                    } else {
                        alert('Failed to delete product.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('An error occurred. Please try again.');
                });
        }
    }

    function handleTypeChange() {
        const type = document.getElementById('type')?.value;
        const dateInput = document.getElementById('date');
        const today = new Date().toISOString().split('T')[0];

        if (!dateInput) return;

        if (type === 'reshelved') {
            dateInput.value = today;
            dateInput.min = today;
            dateInput.max = today;
            dateInput.readOnly = true;
            dateInput.disabled = false;
        } else if (type) {
            dateInput.value = '';
            dateInput.min = '';
            dateInput.max = '';
            dateInput.readOnly = false;
            dateInput.disabled = false;
        } else {
            dateInput.disabled = true;
        }
    }

    // Category modal handlers
    function showAddCategoryModal() {
        categoryModalTitle.textContent = 'Add New Category';
        categoryForm.reset();
        document.getElementById('categoryId').value = '';
        categoryModal.show();
    }

    function editCategory(id) {
        categoryModalTitle.textContent = 'Edit Category';

        fetch(`${contextPath}/admin/categories/get?id=${id}`)
            .then(res => res.json())
            .then(data => {
                if (data.name && data.description) {
                    categoryForm.name.value = data.name;
                    categoryForm.description.value = data.description;
                    document.getElementById('categoryId').value = data.id;
                    categoryModal.show();
                } else {
                    alert('Failed to load category.');
                }
            })
            .catch(error => {
                console.error('Error fetching category:', error);
                alert('Error occurred while fetching category details.');
            });
    }

    function handleCategorySubmit(event) {
        event.preventDefault();
        const formData = new FormData(categoryForm);
        const categoryId = formData.get('id');

        const url = categoryId
            ? `${contextPath}/admin/categories/update`
            : `${contextPath}/admin/categories/add`;

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
            .catch(error => {
                console.error('Error:', error);
                alert('An error occurred. Please try again.');
            });
    }

    function deleteCategory(id) {
        if (confirm('Are you sure you want to delete this category?')) {
            fetch(`${contextPath}/admin/categories/delete`, {
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
                .catch(error => {
                    console.error('Error:', error);
                    alert('An error occurred. Please try again.');
                });
        }
    }

    // Hook up form submit events
    productForm?.addEventListener('submit', handleProductSubmit);
    categoryForm?.addEventListener('submit', handleCategorySubmit);
    document.getElementById('type')?.addEventListener('change', handleTypeChange);

    window.showAddProductModal = showAddProductModal;
    window.editProduct = editProduct;
    window.deleteProduct = deleteProduct;

    window.showAddCategoryModal = showAddCategoryModal;
    window.editCategory = editCategory;
    window.deleteCategory = deleteCategory;

    // Initialize type change handler on load
    handleTypeChange();
});
</script>
</body>
</html>
