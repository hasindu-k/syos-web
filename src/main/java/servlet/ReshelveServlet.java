package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import service.shelvesService;

import java.io.IOException;

@WebServlet("/reshelve")
public class ReshelveServlet extends HttpServlet {
    private shelvesService service;

    @Override
    public void init() {
        service = new shelvesService();
    }

    public void setShelvesService(shelvesService service) {
        this.service = service;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
        	service.reshelf(); // may throw
        } catch (Exception e) {
            e.printStackTrace(); // or log it
            request.setAttribute("error", "Reshelving failed: " + e.getMessage());
        }
        response.sendRedirect("menu"); // always redirect
    }
}
