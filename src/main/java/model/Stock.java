// src/model/Stock.java
package model;

import java.util.Date;

/**
 * Represents a Stock entry.
 */
public class Stock {
    private int id;
    private int productId;
    private int quantity;
    private int warehouseId;
    private Date expiryDate;
    private String batch;

    public Stock(int productId, int quantity, int warehouseId, Date expiryDate) {
        this.productId = productId;
        this.quantity = quantity;
        this.warehouseId = warehouseId;
        this.expiryDate = expiryDate;
        this.batch = ""; // Default empty batch
    }

    // Getters and Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getWarehouseId() { return warehouseId; }
    public void setWarehouseId(int warehouseId) { this.warehouseId = warehouseId; }

    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }

    public String getBatch() { return batch; }
    public void setBatch(String batch) { this.batch = batch; }
}
