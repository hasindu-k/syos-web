// src/factory/ModelFactory.java
package factory;

import model.*;

import java.awt.*;
import java.util.List;

/**
 * Factory Pattern.
 * Creates instances of model classes.
 */
public class ModelFactory {
    public static Product createProduct(String name, int unitId, int categoryId, String status,
                                        int warehouseId, String note, int stockAlert, int supplierId) {
        Product product = new Product(name, unitId, categoryId, status, warehouseId, note, stockAlert, supplierId);
        return product;
    }

    public static Customer createCustomer(String name, String email, String phone) {
        return new Customer(name, email, phone);
    }

    public static Bill createBill(Integer customerId, int totalQty, double subTotal,
                                  String discountType, double discountValue, String taxType,
                                  double taxValue, double total, double receivedAmount,
                                  double changeReturn, String paymentType, String paymentStatus, List<BillItem> items) {
        return new Bill(customerId, totalQty, subTotal, discountType, discountValue,
                total, receivedAmount, changeReturn,
                paymentType, paymentStatus, items);
    }

    public static Stock createStock(int productId, int quantity, double cost,
                                    double price, double tax, int warehouseId, java.util.Date expiryDate) {
        return new Stock(productId, quantity, cost, price, warehouseId, expiryDate);
    }

    
}
