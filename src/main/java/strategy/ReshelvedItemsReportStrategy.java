// src/strategy/ReshelvedItemsReportStrategy.java
package strategy;

import dao.ReportDao;
import java.util.List;
import java.util.Map;

/**
 * Concrete Strategy for Reshelved Items Report.
 */
public class ReshelvedItemsReportStrategy implements ReportStrategy {
    private String date;
    private ReportDao reportDao;

    public ReshelvedItemsReportStrategy(String date) {
        this.date = date;
        this.reportDao = new ReportDao();
    }

    @Override
    public List<Map<String, Object>> generateReport() {
        return reportDao.getReshelvedItems(date);
    }

    @Override
    public String getReportName() {
        return "Reshelved Items Report for " + date;
    }
}
