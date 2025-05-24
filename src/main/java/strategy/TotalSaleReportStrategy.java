// src/strategy/TotalSaleReportStrategy.java
package strategy;

import dao.ReportDao;
import java.util.List;
import java.util.Map;

/**
 * Concrete Strategy for Total Sale Report.
 */
public class TotalSaleReportStrategy implements ReportStrategy {
    private String date;
    private ReportDao reportDao;

    public TotalSaleReportStrategy(String date) {
        this.date = date;
        this.reportDao = new ReportDao();
    }

    @Override
    public List<Map<String, Object>> generateReport() {
        return reportDao.getTotalSaleForDay(date);
    }

    @Override
    public String getReportName() {
        return "Total Sale Report for " + date;
    }
}
