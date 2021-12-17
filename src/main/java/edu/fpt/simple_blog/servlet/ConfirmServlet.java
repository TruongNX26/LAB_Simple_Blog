package edu.fpt.simple_blog.servlet;

import edu.fpt.simple_blog.constant.PathConstant;
import edu.fpt.simple_blog.entity.User;
import edu.fpt.simple_blog.filter.MainFilter;
import edu.fpt.simple_blog.repository.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/confirm-servlet")
public class ConfirmServlet extends HttpServlet {

    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_CODE = "code";

    private static final String SUCCESS = MainFilter.PREFIX_PAGE + MainFilter.LOGIN +"?status=confirmed";
    private static final String FAILED = PathConstant.CONFIRM_PAGE;

    private UserRepository userRepo;

    @Override
    public void init() throws ServletException {
        super.init();
        userRepo = UserRepository.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter(PARAM_EMAIL);
        int code = getCode(request);

        int confirmCode = userRepo.findConfirmCode(email);

        String destination = FAILED;
        boolean isRedirect = false;
        if(code == confirmCode) {
            boolean isSuccess = userRepo.updateStatus(email, User.Status.ACTIVE);
            if(isSuccess) {
                destination = SUCCESS + "&email=" + email;
                isRedirect = true;
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Confirming user failed");
            }
        } else {
            request.setAttribute("USER_EMAIL", email);
            request.setAttribute("ALERT", "Invalid confirm code");
        }


        if(isRedirect) {
            response.sendRedirect(request.getContextPath() + destination);
        } else {
            request.getRequestDispatcher(destination).forward(request, response);
        }
    }

    private int getCode(HttpServletRequest request) {
        int code = 0;
        try {
            code = Integer.parseInt(request.getParameter(PARAM_CODE));
        } catch (Exception e) { /* do nothing */ }

        return code;
    }
}
