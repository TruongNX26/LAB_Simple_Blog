package edu.fpt.simple_blog.servlet;

import edu.fpt.simple_blog.constant.PathConstant;
import edu.fpt.simple_blog.entity.User;
import edu.fpt.simple_blog.filter.MainFilter;
import edu.fpt.simple_blog.repository.UserRepository;
import edu.fpt.simple_blog.util.EmailUtil;
import edu.fpt.simple_blog.util.MyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/register-servlet")
public class RegisterServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_PASSWORD = "password";

    public static final String ATR_ERRORS = "ERRORS";

    private static final String SUCCESS = MainFilter.PREFIX_PAGE + MainFilter.LOGIN;
    private static final String FAILED = PathConstant.REGISTER_PAGE;

    private UserRepository userRepo;

    @Override
    public void init() throws ServletException {
        super.init();
        userRepo = UserRepository.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String destination = null;
        boolean isRedirect = false;

        User user = extractUser(request);
        Map<String, String> errors = validateUser(user);

        if (errors.isEmpty()) {
            user.setPassword(MyUtil.encode(user.getPassword()));
            boolean isCreated = userRepo.create(user);
            if (isCreated) {
                destination = SUCCESS + "?email=" + user.getEmail();
                sendConfirmEmail(user.getEmail(), user.getConfirmCode());
            }
            isRedirect = true;
        } else {
            destination = FAILED;
            request.setAttribute(ATR_ERRORS, errors);
        }

        if (isRedirect) {
            response.sendRedirect(request.getContextPath() + destination);
        } else {
            request.getRequestDispatcher(destination).forward(request, response);
        }
    }

    private void sendConfirmEmail(String receiver, int confirmCode) {
        new Thread(() -> {
            try {
                String emailContent = "Your confirm code is: " + confirmCode;
                EmailUtil.sendMail(receiver, emailContent);
            } catch (MessagingException e) {
                logger.error("Sending confirm email failed", e);
            }
        }).start();
    }

    private User extractUser(HttpServletRequest request) {
        String email = request.getParameter(PARAM_EMAIL);
        String name = request.getParameter(PARAM_NAME);
        String password = request.getParameter(PARAM_PASSWORD);

        return User.builder()
                .email(email)
                .name(name)
                .password(password)
                .role(User.Role.MEMBER)
                .status(User.Status.NEW)
                .confirmCode(createConfirmCode())
                .build();
    }

    private Map<String, String> validateUser(User user) {
        Map<String, String> errors = new HashMap<>();

        if (user.getEmail().isEmpty()) {
            errors.put(PARAM_EMAIL, "Email must not be empty");
        } else {
            boolean isExisted = userRepo.checkExistedEmail(user.getEmail());
            if(isExisted) errors.put(PARAM_EMAIL, "Email already existed");
        }

        if (user.getPassword().length() < 6 || user.getPassword().length() > 20) {
            errors.put(PARAM_PASSWORD, "Password length 6 -> 20");
        }

        if (user.getName().length() < 6 || user.getName().length() > 30) {
            errors.put(PARAM_NAME, "User's name length 6 -> 30");
        }

        return errors;
    }

    private int createConfirmCode() {
        return (int) (Math.random() * 8999 + 1000);
    }
}
