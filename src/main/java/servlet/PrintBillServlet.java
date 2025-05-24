package servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;

import controller.BillController;
import model.Bill;
import model.BillItem;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.font.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.UnitValue;

import java.io.IOException;

@WebServlet("/print-bill")
public class PrintBillServlet extends HttpServlet {
    private final BillController billController = new BillController();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String billIdParam = request.getParameter("billId");

        if (billIdParam == null || billIdParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing billId");
            return;
        }

        int billId;
        try {
            billId = Integer.parseInt(billIdParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid billId");
            return;
        }

        Bill bill = billController.getBillById(billId);
        if (bill == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Bill not found");
            return;
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=bill-" + billId + ".pdf");

        try {
            PdfWriter writer = new PdfWriter(response.getOutputStream());
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);

            PdfFont font = PdfFontFactory.createFont();
            doc.setFont(font);

            // Header
            doc.add(new Paragraph("Bill ID: " + bill.getId()));
            doc.add(new Paragraph("Date: " + bill.getBillDate()));
            String customerName = bill.getCustomerId() != null
                    ? billController.getCustomerService().getCustomerById(bill.getCustomerId()).getName()
                    : "Walk-in Customer";
            doc.add(new Paragraph("Customer: " + customerName));
            doc.add(new Paragraph(" "));

            // Table
            Table table = new Table(UnitValue.createPercentArray(new float[]{4, 2, 2, 2}))
                    .useAllAvailableWidth();
            table.addHeaderCell("Product");
            table.addHeaderCell("Qty");
            table.addHeaderCell("Price");
            table.addHeaderCell("Total");

            for (BillItem item : bill.getItems()) {
                table.addCell(item.getProductName());
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell("Rs. " + item.getPrice());
                table.addCell("Rs. " + item.getTotalPrice());
            }

            doc.add(table);
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Subtotal: Rs. " + bill.getSubTotal()));
            doc.add(new Paragraph("Discount: " + bill.getDiscountType() + " " + bill.getDiscountValue()));
            doc.add(new Paragraph("Total: Rs. " + bill.getTotal()));
            doc.add(new Paragraph("Received: Rs. " + bill.getReceivedAmount()));
            doc.add(new Paragraph("Change Return: Rs. " + bill.getChangeReturn()));

            doc.close(); // flushes everything properly

        } catch (Exception e) {
//            e.printStackTrace();
            response.reset(); // important to prevent corrupt output
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to generate PDF");
        }
    }
}
