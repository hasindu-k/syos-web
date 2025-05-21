// src/observer/Observer.java
package observer;

/**
 * Observer Pattern Interface.
 * Observers implement this to receive updates.
 */
public interface Observer {
    void update(String message);
}
