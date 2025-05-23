// src/service/StockService.java
package service;

import dao.StockDao;
import model.Stock;
import observer.Observer;
import observer.StockAlertService;
import observer.Subject;

import java.util.Date;
import java.util.List;

/**
 * Service class for Stock operations.
 */
public class StockService implements Subject {
    private StockDao stockDao;
    private List<Observer> observers;

    public StockService() {
        this.stockDao = new StockDao();
        this.observers = new java.util.ArrayList<>();
        // Register default observers
        registerObserver(new StockAlertService());
    }

    /**
     * Adds a new stock entry and notifies observers.
     * @param productId Product ID.
     * @param quantity Quantity.
     * @param costPrice Cost price.
     * @param sellingPrice Selling price.
     * @param tax Tax percentage.
     * @param warehouseId Warehouse ID.
     * @param expiryDate Expiry date.
     * @return Stock ID if successful, else -1.
     */
    public int addStock(int productId, int quantity, int warehouseId, Date expiryDate) {
        Stock stock = new Stock(productId, quantity, warehouseId, expiryDate);
        int stockId = stockDao.addStock(stock);
        if (stockId > 0) {
            notifyObservers("New stock added for Product ID " + productId);
        }
        return stockId;
    }

    /**
     * Retrieves all stocks with product details.
     * @return List of Stock objects.
     */
    public List<Stock> getAllStocks() {
        return stockDao.getAllStocksWithProductDetails();
    }

    /**
     * Registers an observer.
     * @param o Observer to register.
     */
    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    /**
     * Removes an observer.
     * @param o Observer to remove.
     */
    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    /**
     * Notifies all observers with a message.
     * @param message Message to send.
     */
    @Override
    public void notifyObservers(String message) {
        for (Observer o : observers) {
            o.update(message);
        }
    }

    @Override
    public void update(String message) {
        System.out.println("Stock Service received update: " + message);
        // Add logic for stock update, if needed
    }

    /**
     * Retrieves stocks for a specific product ID.
     * @param productId The ID of the product to get stocks for
     * @return List of Stock objects for the given product ID
     */
    public List<Stock> getStocksByProductId(int productId) {
        return stockDao.getStocksByProductId(productId);
    }

    /**
     * Retrieves all stocks that are running low (quantity below threshold).
     * @return List of Stock objects with low quantity
     */
    public List<Stock> getLowStockItems() {
        List<Stock> allStocks = getAllStocks();
        List<Stock> lowStocks = new java.util.ArrayList<>();
        
        for (Stock stock : allStocks) {
            // Consider stock as low if quantity is less than or equal to 10
            if (stock.getQuantity() < 50) {
                lowStocks.add(stock);
            }
        }
        return lowStocks;
    }
    // Additional stock operations can be added here

	public int getStockQuantityByProductId(int id) {
		List<Stock> stocks = stockDao.getStocksByProductId(id);
	    return stocks.stream().mapToInt(Stock::getQuantity).sum();
	}
}
