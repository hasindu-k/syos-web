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
        System.out.println("reshelf initiated");
        try {

            ResultSet itemSet2 = connection.createStatement().executeQuery("SELECT * "
                    + "FROM shelves INNER JOIN stocks ON stocks.id = shelves.stocks_id"
                    + " INNER JOIN products ON products.id = stocks.products_id WHERE shelves.quantity < 50");
            while (itemSet2.next()) {
                String stock_id = itemSet2.getString("shelves.stocks_id");
                int remainingQty = itemSet2.getInt("shelves.quantity");
                int inStockQty = itemSet2.getInt("stocks.quantity");
                int neededQty = 50 - remainingQty;
                int stockAlert = itemSet2.getInt("stock_alert");
                int sumQty = remainingQty + inStockQty;
                if (remainingQty < neededQty) {
                    if (sumQty < stockAlert) {
//                            ResultSet shelfItem = DBConnection.INSTANCE.getConnection().createStatement().executeQuery("SELECT * FROM "
//                                    + "shelves WHERE stocks_id = '" + stock_id + "'");
                        connection.createStatement().executeUpdate("UPDATE stocks SET quantity = '" + remainingQty + "' WHERE stocks.id = '" + stock_id + "'");
                        ResultSet newStockSet = connection.createStatement().executeQuery("""
                                                                           SELECT * 
                                                                           FROM stocks
                                                                           INNER JOIN products ON products.id = stocks.products_id 
                                                                           WHERE products.id = '1' 
                                                                           AND stocks.quantity > '""" + stockAlert + "' \n"
                                + "ORDER BY stocks.expiry_date ASC \n"
                                + "LIMIT 1;");
                        if (newStockSet.next()) {
                            String newStockId = newStockSet.getString("stocks.id");
                            int newQty = newStockSet.getInt("quantity");
                            if (newQty > 50) {
                                newQty = 50;
                            }
                            connection.createStatement().executeUpdate("UPDATE shelves SET `quantity` = '" + newQty + "',"
                                    + " `stocks_id` = '" + newStockId + "' WHERE stocks_id = '" + stock_id + "'  ");

                            connection.createStatement().executeUpdate("UPDATE stocks SET `quantity` = `quantity` - '" + newQty + "',"
                                    + " WHERE id = '" + newStockId + "'  ");
                            System.out.println("Reshelving Completed");

                        }
                    } else if (sumQty < neededQty) {
                        ResultSet newStockSet = connection.createStatement().executeQuery("SELECT * FROM `stocks` WHERE `id` = '" + stock_id + "'");
                        connection.createStatement().executeUpdate("UPDATE stocks SET quantity = '" + 0 + "' WHERE stocks.id = '" + stock_id + "'");
                        if (newStockSet.next()) {
                            String newQty = newStockSet.getString("quantity");
                            connection.createStatement().executeUpdate("UPDATE shelves SET `quantity` = '" + newQty + "'"
                                    + " WHERE stocks_id = '" + stock_id + "'  ");

                            connection.createStatement().executeUpdate("UPDATE stocks SET `quantity` = `quantity` - '" + newQty + "',"
                                    + " WHERE id = '" + stock_id + "'  ");
                            System.out.println("Reshelving Completed");

                        }
                    }

                } else if (remainingQty < 50) {
                    connection.createStatement().executeUpdate("UPDATE stocks SET quantity = quantity - '" + neededQty + "' WHERE stocks.id = '" + stock_id + "'");
                    connection.createStatement().executeUpdate("UPDATE shelves SET `quantity` = quantity + '" + neededQty + "'"
                            + " WHERE stocks_id = '" + stock_id + "'  ");

//                        connection.createStatement().executeUpdate("UPDATE stocks SET `quantity` = `quantity` - '" + newQty + "',"
//                                + " WHERE id = '" + stock_id + "'  ");
                    System.out.println("Reshelving Completed");

                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
}
