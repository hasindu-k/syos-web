// src/strategy/BillReportStrategy.java
package strategy;

import dao.ReportDao;
import java.util.List;
import java.util.Map;

/**
 * Concrete Strategy for Bill Report.
 */
public class BillReportStrategy implements ReportStrategy {
    private ReportDao reportDao;
    private String date;

    public BillReportStrategy(String date) {
        this.reportDao = new ReportDao();
        this.date = date;
    }

    @Override
    public List<Map<String, Object>> generateReport() {
        return reportDao.getBillReport(date);
    }

    @Override
    public String getReportName() {
        return "Bill Report for " + date;
    }
}
