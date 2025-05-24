package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Bill;
import model.BillItem;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDFontFactory;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import controller.BillController;

/**
 * Servlet implementation class PrintBillServlet
 */
@WebServlet("/print-bill")
public class PrintBillServlet extends HttpServlet {
    private final BillController billController = new BillController();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String billIdParam = request.getParameter("billId");
        int billId = Integer.parseInt(billIdParam);

        Bill bill = billController.getBillById(billId);

        if (bill == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Bill not found");
            return;
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=bill-" + billId + ".pdf");

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);

            PDPageContentStream content = new PDPageContentStream(doc, page);

            PDFont font = PDFontFactory.createDefaultFont();

            content.setFont(font, 12);
            content.beginText();
            content.setLeading(14.5f);
            content.newLineAtOffset(50, 700);

            content.showText("Bill ID: " + bill.getId());
            content.newLine();
            content.showText("Date: " + bill.getBillDate());
            content.newLine();
            String customerName = bill.getCustomerId() != null
                    ? billController.getCustomerService().getCustomerById(bill.getCustomerId()).getName()
                    : "Walk-in Customer";
            content.newLine();
            content.newLine();
            content.showText("Items:");
            content.newLine();

            for (BillItem item : bill.getItems()) {
                content.showText(
                        "- " + item.getProductName() + " | Qty: " + item.getQuantity() + " | ₹" + item.getPrice());
                content.newLine();
            }

            content.newLine();
            content.showText("Subtotal: Rs." + bill.getSubTotal());
            content.newLine();
            content.showText("Discount: " + bill.getDiscountType() + " " + bill.getDiscountValue());
            content.newLine();
            content.showText("Total: Rs." + bill.getTotal());
            content.newLine();
            content.showText("Received: Rs." + bill.getReceivedAmount());
            content.newLine();
            content.showText("Change Return: Rs." + bill.getChangeReturn());

            content.endText();
            content.close();

            doc.save(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Failed to generate PDF");
        }
    }
}
