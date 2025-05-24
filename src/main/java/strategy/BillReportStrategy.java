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

    public BillReportStrategy() {
        this.reportDao = new ReportDao();
    }

    @Override
    public List<Map<String, Object>> generateReport() {
        return reportDao.getBillReport();
    }

    @Override
    public String getReportName() {
        return "Bill Report";
    }
}
