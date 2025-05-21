// src/model/Bill.java
package model;

import java.util.Date;
import java.util.List;

/**
 * Represents a Bill in the system.
 */
public class Bill {
    private int id;
    private Integer customerId; // Nullable for walk-in customers
    private int totalQty;
    private double subTotal;
    private String discountType; // "FIXED" or "PERCENTAGE"
    private double discountValue;
    private double total;
    private double receivedAmount;
    private double changeReturn;
    private String paymentType; // "Cash"
    private String paymentStatus; // "Paid" or "Pending"
    private Date billDate;
    private List<BillItem> items;

    public Bill(Integer customerId, int totalQty, double subTotal, String discountType,
                double discountValue, double total,
                double receivedAmount, double changeReturn, String paymentType,
                String paymentStatus, List<BillItem> items) {
        this.customerId = customerId;
        this.totalQty = totalQty;
        this.subTotal = subTotal;
        this.discountType = discountType;
        this.discountValue = discountValue;
//        this.taxType = taxType;
//        this.taxValue = taxValue;
        this.total = total;
        this.receivedAmount = receivedAmount;
        this.changeReturn = changeReturn;
        this.paymentType = paymentType;
        this.paymentStatus = paymentStatus;
        this.billDate = new Date();
        this.items = items;
    }

    // Getters and Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public int getTotalQty() { return totalQty; }
    public void setTotalQty(int totalQty) { this.totalQty = totalQty; }

    public double getSubTotal() { return subTotal; }
    public void setSubTotal(double subTotal) { this.subTotal = subTotal; }

    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }

    public double getDiscountValue() { return discountValue; }
    public void setDiscountValue(double discountValue) { this.discountValue = discountValue; }

//    public String getTaxType() { return taxType; }
//    public void setTaxType(String taxType) { this.taxType = taxType; }
//
//    public double getTaxValue() { return taxValue; }
//    public void setTaxValue(double taxValue) { this.taxValue = taxValue; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public double getReceivedAmount() { return receivedAmount; }
    public void setReceivedAmount(double receivedAmount) { this.receivedAmount = receivedAmount; }

    public double getChangeReturn() { return changeReturn; }
    public void setChangeReturn(double changeReturn) { this.changeReturn = changeReturn; }

    public String getPaymentType() { return paymentType; }
    public void setPaymentType(String paymentType) { this.paymentType = paymentType; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public Date getBillDate() { return billDate; }
    public void setBillDate(Date billDate) { this.billDate = billDate; }

    public List<BillItem> getItems() { return items; }
    public void setItems(List<BillItem> items) { this.items = items; }
}
