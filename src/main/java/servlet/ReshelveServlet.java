package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import service.shelvesService;

import java.io.IOException;

@WebServlet("/reshelve")
public class ReshelveServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        shelvesService service = new shelvesService();
        service.reshelf();
        response.sendRedirect("menu");
    }
}
