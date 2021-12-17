package edu.fpt.simple_blog.servlet;

import edu.fpt.simple_blog.filter.MainFilter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout-servlet")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if(session != null) session.removeAttribute(LoginServlet.ATR_SESSION_USER);

        String destination = MainFilter.PREFIX_PAGE + MainFilter.HOME;
        response.sendRedirect(request.getContextPath() + destination);
    }
}
