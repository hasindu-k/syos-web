</div> <!-- close container -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js" defer></script>

<script defer>
document.addEventListener('DOMContentLoaded', () => {
	const contextPath = '<%= request.getContextPath() %>';
	console.log(contextPath);

    // ===== PRODUCT MODAL =====
    const productModalEl = document.getElementById('productModal');
    const productForm = document.getElementById('productForm');
    const productTitle = document.getElementById('productModalTitle');
    const productModal = productModalEl ? new bootstrap.Modal(productModalEl) : null;

    if (productModal && productForm && productTitle) {
        window.showAddProductModal = () => {
            productTitle.textContent = 'Add New Product';
            productForm.reset();
            productForm.querySelector('#productId').value = '';
            productModal.show();
        };

        window.editProduct = (id) => {
        	console.log("Product id " + id);
            productTitle.textContent = 'Edit Product';
            productForm.reset();
            productForm.querySelector('#productId').value = id;
           
            const url = contextPath + `/admin/products/get?id=` + id;
            console.log("Fetching:", url);
            fetch(url)
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
                         productForm.price.value = data.price;

                         productModal.show();
                    } else {
                        alert('Failed to load product.');
                    }
                })
                .catch(err => {
                    console.error('Fetch error:', err);
                    alert('Error loading product.');
                });
        };

        window.closeProductModal = () => {
            productModal.hide();
        };

        window.handleProductSubmit = (event) => {
            event.preventDefault();
            const formData = new FormData(productForm);
            const productId = formData.get('id');

            const url = productId
                ? contextPath + `/admin/products/update`
                : contextPath + `/admin/products/add`;

            fetch(url, {
                method: 'POST',
                body: new URLSearchParams(formData)
            })
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
                        location.reload();
                    } else {
                        alert('Product operation failed.');
                    }
                })
                .catch(err => {
                    console.error('Error:', err);
                    alert('Error occurred while saving product.');
                });
        };

        window.deleteProduct = (id) => {
            if (!confirm('Delete this product?')) return;
            fetch(contextPath + `/admin/products/delete`, {
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
                .catch(err => {
                    console.error('Delete error:', err);
                    alert('Error occurred while deleting product.');
                });
        };
    }

    // ===== CATEGORY MODAL =====
    const categoryModalEl = document.getElementById('categoryModal');
    const categoryForm = document.getElementById('categoryForm');
    const categoryTitle = document.getElementById('categoryModalTitle');
    const categoryModal = categoryModalEl ? new bootstrap.Modal(categoryModalEl) : null;

    if (categoryModal && categoryForm && categoryTitle) {
        window.showAddCategoryModal = () => {
            categoryTitle.textContent = 'Add New Category';
            categoryForm.reset();
            categoryForm.querySelector('#categoryId').value = '';
            categoryModal.show();
        };

        window.editCategory = (id) => {
            categoryTitle.textContent = 'Edit Category';

            fetch(contextPath + `/admin/categories/get?id=` + id)
                .then(res => res.json())
                .then(data => {
                    if (data.name && data.description) {
                        categoryForm.querySelector('[name="name"]').value = data.name;
                        categoryForm.querySelector('[name="description"]').value = data.description;
                        categoryForm.querySelector('#categoryId').value = data.id;
                        categoryModal.show();
                    } else {
                        alert('Failed to load category.');
                    }
                })
                .catch(err => {
                    console.error('Fetch error:', err);
                    alert('Error loading category.');
                });
        };

        window.handleCategorySubmit = (event) => {
            event.preventDefault();
            const formData = new FormData(categoryForm);
            const categoryId = formData.get('id');
            const url = categoryId
                ? contextPath + `/admin/categories/update`
                : contextPath + `/admin/categories/add`;

            fetch(url, {
                method: 'POST',
                body: new URLSearchParams(formData)
            })
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
                        location.reload();
                    } else {
                        alert('Category operation failed.');
                    }
                })
                .catch(err => {
                    console.error('Error:', err);
                    alert('Error occurred while saving category.');
                });
        };

        window.deleteCategory = (id) => {
            if (!confirm('Delete this category?')) return;
            fetch(contextPath + `/admin/categories/delete`, {
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
                    console.error('Delete error:', err);
                    alert('Error occurred while deleting category.');
                });
        };
        
        window.closeCategoryModal = () => {
        	categoryModal.hide();
        };
    }

    // ===== Optional: Auto-trigger a function if present =====
    if (typeof handleTypeChange === 'function') {
        handleTypeChange();
    }
});
</script>
</body>
</html>
