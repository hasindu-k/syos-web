// src/model/Product.java
package model;

/**
 * Represents a Product in the system.
 */
public class Product {
    private int id;
    private String name;
    private String code;
    private int unitId;
    private int categoryId;
    private String status;
    private int warehouseId;
    private String note;
    private int stockAlert;
    private int supplierId;
    private double price;
    private String description;

    public Product(String name, int unitId, int categoryId, String status,
            int warehouseId, String note, int stockAlert, int supplierId,
            double price) {
	 this.name = name;
	 this.code = "";  
	 this.unitId = unitId;
	 this.categoryId = categoryId;
	 this.status = status;
	 this.warehouseId = warehouseId;
	 this.note = note;
	 this.stockAlert = stockAlert;
	 this.supplierId = supplierId;
	 this.price = price;
	 this.description = "";
	}

    // Getters and Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public int getUnitId() { return unitId; }
    public void setUnitId(int unitId) { this.unitId = unitId; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getWarehouseId() { return warehouseId; }
    public void setWarehouseId(int warehouseId) { this.warehouseId = warehouseId; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public int getStockAlert() { return stockAlert; }
    public void setStockAlert(int stockAlert) { this.stockAlert = stockAlert; }

    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
