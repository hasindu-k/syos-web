// src/observer/StockAlertService.java
package observer;

/**
 * Concrete Observer for stock alerts.
 */
public class StockAlertService implements Observer {
    @Override
    public void update(String message) {
        System.out.println("Stock Alert: " + message);
    }
}
