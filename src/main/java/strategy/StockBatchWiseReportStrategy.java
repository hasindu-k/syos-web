// src/strategy/StockBatchWiseReportStrategy.java
package strategy;

import dao.ReportDao;
import java.util.List;
import java.util.Map;

/**
 * Concrete Strategy for Stock Batch-Wise Report.
 */
public class StockBatchWiseReportStrategy implements ReportStrategy {
    private ReportDao reportDao;

    public StockBatchWiseReportStrategy() {
        this.reportDao = new ReportDao();
    }

    @Override
    public List<Map<String, Object>> generateReport() {
        return reportDao.getStockBatchWise();
    }

    @Override
    public String getReportName() {
        return "Stock Batch-Wise Report";
    }
}
