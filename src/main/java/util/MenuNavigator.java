// src/util/MenuNavigator.java
package util;

import java.util.Scanner;

/**
 * Utility class for menu navigation and input handling.
 */
public class MenuNavigator {
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Retrieves user input.
     * @return User input as String.
     */
    public static String getInput() {
        return scanner.nextLine();
    }

    /**
     * Clears the scanner buffer (if needed).
     */
    public static void clearBuffer() {
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }
}
