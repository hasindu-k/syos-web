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
        const modalTitle = document.getElementById('modalTitle');
        const form = document.getElementById('productForm');
        const modal = new bootstrap.Modal(document.getElementById('productModal'));

        modalTitle.textContent = 'Edit Product';
        form.reset();
        document.getElementById('productId').value = id;

        const url = `${pageContext.request.contextPath}/admin/products/get?id=` + id;
        console.log("GET URL:", url);

        fetch(url)
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    form.name.value = data.name;
                    form.price.value = data.price;
                    form.unitId.value = data.unitId;
                    form.categoryId.value = data.categoryId;
                    form.status.value = data.status;
                    form.warehouseId.value = data.warehouseId;
                    form.note.value = data.note;
                    form.stockAlert.value = data.stockAlert;
                    form.supplierId.value = data.supplierId;
                    form.price.value = data.price;

                    modal.show();
                } else {
                    console.error('Failed to fetch product:', data.message);
                    alert('Failed to load product details.');
                }
            })
            .catch(error => {
                console.error('Error fetching product:', error);
                alert('Error occurred while fetching product details.');
            });
    }


    function closeProductModal() {
        productModal.hide();
    }

    function handleProductSubmit(event) {
        event.preventDefault();
        const formData = new FormData(form);
        const productId = formData.get('id');
        
        console.log("Product ID", productId);
        
        for (let [key, value] of formData.entries()) {
            console.log(`${key}: ${value}`);
        }
        
        const url = productId ?
        	    `${pageContext.request.contextPath}/admin/products/update` :
        	    `${pageContext.request.contextPath}/admin/products/add`;
                
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
    
    function handleTypeChange() {
        const type = document.getElementById('type').value;
        const dateInput = document.getElementById('date');
        const today = new Date().toISOString().split('T')[0];

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

    // Initialize on page load if needed
    window.onload = handleTypeChange;
</script>
</body>
</html>
