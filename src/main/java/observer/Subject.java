// src/observer/Subject.java
package observer;

/**
 * Observer Pattern Interface.
 * Subjects implement this to manage observers.
 */
public interface Subject {
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(String message);
    void update(String message);

}
