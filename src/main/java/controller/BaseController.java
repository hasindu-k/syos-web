// src/controller/BaseController.java
package controller;

/**
 * Abstract base controller implementing Template Method.
 */
public abstract class BaseController {
    /**
     * Displays the header.
     */
    protected abstract void showHeader();

    /**
     * Displays the menu options.
     */
    protected abstract void showOptions();

    /**
     * Retrieves the user's menu choice.
     * @return User's choice as integer.
     */
    protected abstract int getUserChoice();

    /**
     * Handles the user's choice.
     * @param choice User's choice.
     * @return True to exit controller, else False.
     */
    protected abstract boolean handleChoice(int choice);

    /**
     * Runs the controller's workflow.
     * Continuously displays the menu and handles user input.
     */
    public void run() {
        while (true) {
            showHeader();
            showOptions();
            int choice = getUserChoice();
            boolean shouldExit = handleChoice(choice);
            if (shouldExit) {
                break;
            }
        }
    }
}
