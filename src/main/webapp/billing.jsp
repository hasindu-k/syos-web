<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="header.jsp" %>

<h2>Billing Page</h2>

<form method="post" action="billing">
    <!-- Autocomplete Customer Name -->
    <div class="mb-3">
        <label for="customerSearch" class="form-label">Customer Name</label>
        <input type="text" class="form-control" id="customerSearch" name="customerName" autocomplete="off">
        <input type="hidden" id="customerId" name="customerId">
        <div id="suggestions" class="list-group position-absolute" style="z-index:999"></div>
    </div>

    <!-- Product List -->
    <div id="productList" class="mb-3">
        <div class="d-flex mb-2 fw-bold">
            <div class="me-2" style="width: 40%;">Product ID</div>
            <div class="me-2" style="width: 40%;">Quantity</div>
            <div style="width: 20%;"></div>
        </div>
        <!-- Initial Row -->
        <div class="d-flex mb-2 align-items-center">
            <input type="number" class="form-control me-2" name="productId" placeholder="Product ID"
                style="width: 40%;" required>
            <input type="number" class="form-control me-2" name="quantity" placeholder="Quantity"
                style="width: 40%;" required>
            <button type="button" class="btn btn-danger" style="width: 20%;" onclick="removeProduct(this)">✖</button>
        </div>
    </div>

    <button type="button" class="btn btn-secondary mb-3" onclick="addProductRow()">+ Add
        Product</button><br>

    <button type="submit" class="btn btn-primary">Generate Bill</button>
</form>

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
    const productList = document.getElementById("productList");

    productList.querySelectorAll('.product-row').forEach(row => {
        const qtyInput = row.querySelector('input[name="quantity"]');
        qtyInput.addEventListener("keydown", (e) => {
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
    productId.className = "form-control me-2";
    productId.style.width = "40%";
    productId.required = true;

    const quantity = document.createElement("input");
    quantity.type = "number";
    quantity.name = "quantity";
    quantity.placeholder = "Quantity";
    quantity.className = "form-control me-2";
    quantity.style.width = "40%";
    quantity.required = true;

    const removeBtn = document.createElement("button");
    removeBtn.type = "button";
    removeBtn.className = "btn btn-danger";
    removeBtn.textContent = "✖";
    removeBtn.onclick = () => removeProduct(removeBtn);
    removeBtn.style.width = "20%";

    row.appendChild(productId);
    row.appendChild(quantity);
    row.appendChild(removeBtn);
    document.getElementById("productList").appendChild(row);

    // Attach event listener to new row
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

    if (rows.length >= 1) {
        row.remove();
    }
}

document.addEventListener("keydown", function (e) {
    const activeEl = document.activeElement;
    if (
        e.key === "Enter" &&
        activeEl &&
        (activeEl.name === "productId" || activeEl.name === "quantity")
    ) {
        e.preventDefault(); // prevent form submission
        if (activeEl.name === "quantity") {
            addProductRow(); // only when in quantity field
        }
    }
});

</script>

<%@ include file="footer.jsp" %>