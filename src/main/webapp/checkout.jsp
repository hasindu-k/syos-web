<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setAttribute("pageTitle", "Checkout");
%>
<jsp:include page="/header.jsp"/>

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-lg-6 col-md-8">

            <div class="card shadow-lg border-0">
                <div class="card-body p-5">
                    <h2 class="text-center text-primary mb-4 fw-bold">Checkout</h2>

                    <% String error = (String) request.getAttribute("error");
                       String success = (String) request.getAttribute("success");
                    %>
                    <% if (error != null) { %>
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <%= error %>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    <% } else if (success != null) { %>
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            <%= success %>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    <% } %>

                    <form action="checkout" method="post" novalidate>
                        <!-- Shipping Address -->
                        <div class="mb-4">
                            <label for="address" class="form-label fw-semibold">Shipping Address</label>
                            <textarea class="form-control shadow-sm" name="address" id="address" rows="3" required placeholder="Enter full shipping address..."></textarea>
                        </div>

                        <!-- Payment Type -->
                        <div class="mb-4">
                            <label for="paymentType" class="form-label fw-semibold">Payment Method</label>
                            <select class="form-select shadow-sm" name="paymentType" id="paymentType" required onchange="toggleCardFields()">
                                <option value="">-- Select Payment Method --</option>
                                <option value="CashOnDelivery">Cash on Delivery</option>
                                <option value="CardPayment">Online Payment (Card)</option>
                            </select>
                        </div>

                        <!-- Card Payment Fields -->
                        <div id="cardDetails" style="display:none;">
                            <div class="mb-3">
                                <label for="cardNumber" class="form-label">Card Number</label>
                                <input type="text" class="form-control shadow-sm" name="cardNumber" id="cardNumber" placeholder="xxxx-xxxx-xxxx-xxxx">
                            </div>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="cardExpiry" class="form-label">Expiry Date</label>
                                    <input type="text" class="form-control shadow-sm" name="cardExpiry" id="cardExpiry" placeholder="MM/YY">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="cardCVV" class="form-label">CVV</label>
                                    <input type="text" class="form-control shadow-sm" name="cardCVV" id="cardCVV" placeholder="123">
                                </div>
                            </div>
                        </div>

                        <div class="d-grid mt-4">
                            <button type="submit" class="btn btn-success btn-lg">Place Order</button>
                        </div>
                    </form>

                </div>
            </div>

        </div>
    </div>
</div>

<script>
function toggleCardFields() {
    const paymentType = document.getElementById("paymentType").value;
    const cardFields = document.getElementById("cardDetails");

    const show = paymentType === "CardPayment";
    cardFields.style.display = show ? "block" : "none";

    document.getElementById("cardNumber").required = show;
    document.getElementById("cardExpiry").required = show;
    document.getElementById("cardCVV").required = show;
}
</script>

<jsp:include page="/footer.jsp"/>
