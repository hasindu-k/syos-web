<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="header.jsp" %>

<div class="container my-5 p-4 shadow rounded bg-light">
    <h2 class="mb-4 text-center text-primary">Billing Page</h2>

    <form method="post" action="billing">
        <!-- Customer Search -->
        <div class="mb-4 position-relative">
            <label for="customerSearch" class="form-label fw-semibold">Customer Name</label>
            <input type="text" class="form-control" id="customerSearch" name="customerName" autocomplete="off" placeholder="Start typing customer name...">
            <input type="hidden" id="customerId" name="customerId">
            <div id="suggestions" class="list-group position-absolute w-100 mt-1" style="z-index:999"></div>
        </div>

        <!-- Product List -->
        <div id="productList" class="mb-4">
            <div class="d-flex fw-bold mb-2">
                <div class="me-2 w-50">Product ID</div>
                <div class="me-2 w-50">Quantity</div>
            </div>

            <!-- Initial Product Row -->
            <div class="d-flex mb-2 align-items-center product-row">
                <input type="number" class="form-control me-2 w-50" name="productId" placeholder="Product ID" required>
                <input type="number" class="form-control me-2 w-50" name="quantity" placeholder="Quantity" required>
                <button type="button" class="btn btn-outline-danger ms-2" onclick="removeProduct(this)">✖</button>
            </div>
        </div>

        <!-- Discount -->
<div class="row">
    <!-- Discount -->
    <div class="col-md-6 mb-3">
        <label for="discount" class="form-label fw-semibold">Discount (%)</label>
        <input type="number" step="0.01" class="form-control" id="discount" name="discountValue" placeholder="Enter discount in %" min="0">
        <input type="hidden" name="discountType" value="PERCENTAGE">
    </div>

    <!-- Received Amount -->
    <div class="col-md-6 mb-3">
        <label for="receivedAmount" class="form-label fw-semibold">Received Amount</label>
        <input type="number" step="0.01" class="form-control" id="receivedAmount" name="receivedAmount" placeholder="Enter received amount" min="0">
    </div>
</div>



        <!-- Buttons -->
        <div class="d-flex justify-content-between">
            <button type="button" class="btn btn-outline-secondary" onclick="addProductRow()">+ Add Product</button>
            <button type="submit" class="btn btn-primary">Generate Bill</button>
        </div>
    </form>
</div>

<script>
document.addEventListener("DOMContentLoaded", function () {
    initProductRows();

    document.getElementById("customerSearch").addEventListener("input", function () {
        const query = this.value;
        const suggestionBox = document.getElementById("suggestions");
        suggestionBox.innerHTML = "";
        if (query.length < 2) return;

        fetch("search-customers?term=" + encodeURIComponent(query))
            .then(response => response.json())
            .then(data => {
                data.forEach(c => {
                    const item = document.createElement("a");
                    item.className = "list-group-item list-group-item-action";
                    item.textContent = c.name + " (ID: " + c.id + ")";
                    item.addEventListener("click", () => {
                        document.getElementById("customerSearch").value = c.name;
                        document.getElementById("customerId").value = c.id;
                        suggestionBox.innerHTML = "";
                    });
                    suggestionBox.appendChild(item);
                });
            });
    });
});

function initProductRows() {
    document.querySelectorAll('.product-row input[name="quantity"]').forEach(input => {
        input.addEventListener("keydown", function (e) {
            if (e.key === "Enter") {
                e.preventDefault();
                addProductRow();
            }
        });
    });
}

function addProductRow() {
    const row = document.createElement("div");
    row.className = "d-flex mb-2 align-items-center product-row";

    const productId = document.createElement("input");
    productId.type = "number";
    productId.name = "productId";
    productId.placeholder = "Product ID";
    productId.className = "form-control me-2 w-50";
    productId.required = true;

    const quantity = document.createElement("input");
    quantity.type = "number";
    quantity.name = "quantity";
    quantity.placeholder = "Quantity";
    quantity.className = "form-control me-2 w-50";
    quantity.required = true;

    const removeBtn = document.createElement("button");
    removeBtn.type = "button";
    removeBtn.className = "btn btn-outline-danger ms-2";
    removeBtn.textContent = "✖";
    removeBtn.onclick = () => removeProduct(removeBtn);

    row.appendChild(productId);
    row.appendChild(quantity);
    row.appendChild(removeBtn);

    document.getElementById("productList").appendChild(row);

    quantity.addEventListener("keydown", (e) => {
        if (e.key === "Enter") {
            e.preventDefault();
            addProductRow();
        }
    });

    productId.focus();
}

function removeProduct(button) {
    const row = button.parentElement;
    const list = document.getElementById("productList");
    const rows = list.querySelectorAll('.product-row');
    if (rows.length > 1) {
        row.remove();
    }
}
</script>

<%@ include file="footer.jsp" %>
