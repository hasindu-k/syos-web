</div> <!-- close container -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Bootstrap 5 modal handling
    const productModal = new bootstrap.Modal(document.getElementById('productModal'));
    const modalTitle = document.getElementById('modalTitle');
    const form = document.getElementById('productForm');

    function showAddProductModal() {
        modalTitle.textContent = 'Add New Product';
        form.reset();
        document.getElementById('productId').value = '';
        productModal.show();
    }

    function editProduct(id) {
        modalTitle.textContent = 'Edit Product';
        // TODO: Fetch product details and populate form
        productModal.show();
    }

    function closeProductModal() {
        productModal.hide();
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
</script>
</body>
</html>
