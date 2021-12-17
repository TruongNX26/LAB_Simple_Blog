package edu.fpt.simple_blog.servlet;

import edu.fpt.simple_blog.constant.PathConstant;
import edu.fpt.simple_blog.entity.User;
import edu.fpt.simple_blog.filter.MainFilter;
import edu.fpt.simple_blog.repository.UserRepository;
import edu.fpt.simple_blog.util.MyUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login-servlet")
public class LoginServlet extends HttpServlet {

    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_PASSWORD = "password";

    public static final String ATR_SESSION_USER = "SESSION_USER";
    public static final String ATR_USER_EMAIL = "USER_EMAIL";

    private static final String SUCCESS = MainFilter.PREFIX_PAGE + MainFilter.HOME;
    private static final String FAILED = MainFilter.PREFIX_PAGE + MainFilter.LOGIN;
    private static final String CONFIRM = PathConstant.CONFIRM_PAGE;

    private UserRepository userRepo;

    @Override
    public void init() throws ServletException {
        super.init();
        userRepo = UserRepository.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter(PARAM_EMAIL);
        String password = request.getParameter(PARAM_PASSWORD);

        String destination = null;
        boolean isRedirect = true;
        User user = userRepo.findByEmailPassword(email, MyUtil.encode(password));
        if(user != null) {
            if(user.getRole() == User.Role.ADMIN || user.getStatus() == User.Status.ACTIVE) {
                request.getSession().setAttribute(ATR_SESSION_USER, user);
                destination = SUCCESS;
            } else if(user.getStatus() == User.Status.NEW) {
                destination = CONFIRM;
                request.setAttribute(ATR_USER_EMAIL, user.getEmail());
                isRedirect = false;
            }
        } else {
            destination = FAILED + "?status=failed&email=" + email;
        }

        if(isRedirect) {
            response.sendRedirect(request.getContextPath() + destination);
        } else {
            request.getRequestDispatcher(destination).forward(request, response);
        }
    }
}