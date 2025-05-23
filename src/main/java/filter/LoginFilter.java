package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebFilter("/*")
public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri = request.getRequestURI();
        HttpSession session = request.getSession(false);
        boolean loggedIn = session != null && session.getAttribute("role") != null;

        // Publicly accessible paths
        boolean isLogin = uri.endsWith("login") || uri.endsWith("login.jsp");
        boolean isRegister = uri.endsWith("register") || uri.endsWith("register.jsp");
        boolean isLogout = uri.endsWith("logout");
        boolean isStatic = uri.contains("/css/") || uri.contains("/js/") || uri.contains("/images/") || uri.endsWith(".ico");

        boolean isPublic = isLogin || isRegister || isLogout || isStatic;

        if (loggedIn || isPublic) {
            chain.doFilter(req, res); // Allow request
        } else {
            response.sendRedirect(request.getContextPath() + "/login"); // Redirect to login
        }
    }
}
