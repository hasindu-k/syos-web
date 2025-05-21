// src/strategy/ReorderLevelsReportStrategy.java
package strategy;

import dao.ReportDao;
import java.util.List;
import java.util.Map;

/**
 * Concrete Strategy for Reorder Levels Report.
 */
public class ReorderLevelsReportStrategy implements ReportStrategy {
    private ReportDao reportDao;

    public ReorderLevelsReportStrategy() {
        this.reportDao = new ReportDao();
    }

    @Override
    public List<Map<String, Object>> generateReport() {
        return reportDao.getReorderLevels();
    }

    @Override
    public String getReportName() {
        return "Reorder Levels Report";
    }
}
