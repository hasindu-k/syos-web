package observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete Subject class for the Observer pattern.
 * This class manages stock-related notifications to observers.
 */
public class StockSubject implements Subject {
    private List<Observer> observers;

    public StockSubject() {
        this.observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    @Override
    public void update(String message) {
        notifyObservers(message);
    }
}
