// src/service/ReportService.java
package service;

import strategy.ReportStrategy;

import java.util.List;
import java.util.Map;

/**
 * Service class for generating reports using Strategy Pattern.
 */
public class ReportService {
    /**
     * Generates and displays a report based on the provided strategy.
     * @param strategy ReportStrategy implementation.
     */
    public void generateReport(ReportStrategy strategy) {
        List<Map<String, Object>> data = strategy.generateReport();
        displayReport(data, strategy.getReportName());
    }

    /**
     * Displays the report data.
     * @param data Report data.
     * @param title Report title.
     */
    private void displayReport(List<Map<String, Object>> data, String title) {
        System.out.println("\\n=== " + title + " ===");
        if (data.isEmpty()) {
            System.out.println("No data available.");
            return;
        }
        for (Map<String, Object> row : data) {
            row.forEach((key, value) -> {
                System.out.printf("%-20s", value);

            });
            System.out.println("----------------------------------");
        }
    }
}
