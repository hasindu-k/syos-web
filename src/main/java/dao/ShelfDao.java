// src/dao/BillDao.java
package dao;

import util.DBConnection;
import java.sql.Connection;

import java.sql.*;

/**
 * Data Access Object for Bill operations.
 */
public class ShelfDao {

    Connection connection = DBConnection.INSTANCE.getConnection();

    public int updateShelves(int billID) {
        try {
            System.out.println("updating shelves");
            ResultSet billSet = connection.createStatement().executeQuery("SELECT * FROM `bill_items` WHERE `bill_id` = '" + billID + "' ");
            while (billSet.next()) {
                String item_id = billSet.getString("product_id");
                String qty = billSet.getString("quantity");
                ResultSet itemSet = connection.createStatement().executeQuery("SELECT shelves.stocks_id "
                        + "FROM shelves INNER JOIN stocks ON stocks.id = shelves.stocks_id "
                        + "INNER JOIN products ON products.id = stocks.products_id WHERE products.id = '" + item_id + "'");
                if (itemSet.next()) {
                    String stock_id = itemSet.getString("stocks_id");
                    connection.createStatement().executeUpdate("UPDATE `shelves`"
                            + " SET `quantity` = quantity-'" + qty + "' WHERE `stocks_id` = '" + stock_id + "' ");
                }

            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public int updateSales(int billID) {
        try {
            System.out.println("updating sales table");
            ResultSet billDetailRs = connection.createStatement().executeQuery("SELECT "
                    + "customer_id,total_qty,total,payment_type,payment_status,bill_date"
                    + " FROM bills WHERE bills.id = '" + billID + "' ");

            if (billDetailRs.next()) {
                String customer_id = billDetailRs.getString("customer_id");
                String total_qty = billDetailRs.getString("total_qty");
                String total_amount = billDetailRs.getString("total");
                String payment_type = billDetailRs.getString("payment_type");
                String payment_status = billDetailRs.getString("payment_status");
                String sale_date = billDetailRs.getString("bill_date");

                connection.createStatement().executeUpdate("INSERT INTO `sales` (customer_id,total_qty,total_amount,"
                        + "payment_type,payment_status,sale_date,bills_id) VALUES ('" + customer_id + "',"
                        + "'" + total_qty + "','" + total_amount + "','" + payment_type + "','" + payment_status + "','" + sale_date + "','" + billID + "')");

                ResultSet lastID = connection.createStatement().executeQuery("SELECT LAST_INSERT_ID()");
                if (lastID.next()) {
                    int insertId = lastID.getInt(1);  // Retrieves the last inserted ID
                    ResultSet billItemSet = connection.createStatement().executeQuery("SELECT * "
                            + " FROM bill_items WHERE bill_items.bill_id = '" + billID + "'");
                    while (billItemSet.next()) {
                        String productId = billItemSet.getString("product_id");
                        String qty = billItemSet.getString("quantity");
                        String price = billItemSet.getString("price");
                        String total = billItemSet.getString("total");

                        connection.createStatement().executeUpdate("INSERT INTO `sale_items` "
                                + " (sale_id,quantity,price,total,products_id) VALUES "
                                + " ('" + insertId + "','" + qty + "','" + price + "','" + total + "','" + productId + "')");
                    }
                }
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public int reshelf() {
        System.out.println("Reshelving initiated");
        try {
            ResultSet shelves = connection.createStatement().executeQuery(
                "SELECT shelves.id AS shelf_id, shelves.quantity AS shelf_qty, products.id AS product_id " +
                "FROM shelves " +
                "INNER JOIN stocks ON stocks.id = shelves.stocks_id " +
                "INNER JOIN products ON products.id = stocks.products_id " +
                "WHERE shelves.quantity < 50"
            );

            while (shelves.next()) {
                int shelfId = shelves.getInt("shelf_id");
                int shelfQty = shelves.getInt("shelf_qty");
                int productId = shelves.getInt("product_id");
                int needed = 50 - shelfQty;
                int totalAdded = 0;
                String lastUsedStockId = null;

                ResultSet stocks = connection.createStatement().executeQuery(
                    "SELECT id, quantity FROM stocks " +
                    "WHERE products_id = " + productId + " AND quantity > 0 " +
                    "ORDER BY expiry_date ASC"
                );

                while (stocks.next() && needed > 0) {
                    String stockId = stocks.getString("id");
                    int availableQty = stocks.getInt("quantity");
                    int toTake = Math.min(needed, availableQty);

                    connection.createStatement().executeUpdate(
                        "UPDATE stocks SET quantity = quantity - " + toTake + " WHERE id = '" + stockId + "'");

                    needed -= toTake;
                    totalAdded += toTake;
                    lastUsedStockId = stockId;
                }

                if (totalAdded > 0 && lastUsedStockId != null) {
                    connection.createStatement().executeUpdate(
                        "UPDATE shelves SET quantity = quantity + " + totalAdded + ", stocks_id = '" + lastUsedStockId + "' WHERE id = " + shelfId);
                    System.out.println("Shelf " + shelfId + " refilled to 50 using stocks up to ID: " + lastUsedStockId);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("Reshelving completed");
        return 0;
    }

}
