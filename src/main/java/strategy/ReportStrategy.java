// src/strategy/ReportStrategy.java
package strategy;

import java.util.List;
import java.util.Map;

/**
 * Strategy Pattern Interface for report generation.
 */
public interface ReportStrategy {
    List<Map<String, Object>> generateReport();
    String getReportName();
}
