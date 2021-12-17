package edu.fpt.simple_blog.servlet;

import edu.fpt.simple_blog.constant.PathConstant;
import edu.fpt.simple_blog.entity.Article;
import edu.fpt.simple_blog.entity.User;
import edu.fpt.simple_blog.filter.MainFilter;
import edu.fpt.simple_blog.repository.ArticleRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/article-servlet")
public class ArticleServlet extends HttpServlet {

    public static final String PARAM_TITLE = "title";
    public static final String PARAM_DESCRIPTION = "description";
    public static final String PARAM_CONTENT = "content";
    public static final String PARAM_ID = "articleId";
    public static final String PARAM_ACTION = "action";

    private static final String ATR_ERRORS = "ERRORS";

    private static final String INVALID_USER = MainFilter.PREFIX_PAGE + MainFilter.LOGIN;
    private static final String SUCCESS = MainFilter.PREFIX_PAGE + MainFilter.HOME;
    private static final String FAILED = PathConstant.CREATE_ARTICLE_PAGE;

    private ArticleRepository articleRepo;

    @Override
    public void init() throws ServletException {
        super.init();
        articleRepo = ArticleRepository.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter(PARAM_ACTION);
        long articleId = -1;
        try {
            articleId = Long.parseLong(request.getParameter(PARAM_ID));
        } catch (Exception e) { /* do nothing */ }

        HttpSession session = request.getSession();
        User user = session == null ? null : (User) session.getAttribute(LoginServlet.ATR_SESSION_USER);

        String destination;
        if (user != null && user.getRole().equals(User.Role.ADMIN)) {
            switch (action) {
                case "DELETE":
                    articleRepo.updateStatus(articleId, Article.Status.DELETED);
                    break;
                case "APPROVE":
                    articleRepo.updateStatus(articleId, Article.Status.ACTIVE);
                    break;
            }
            destination = MainFilter.PREFIX_PAGE + MainFilter.ADMIN;
        } else {
            destination = INVALID_USER;
        }
        response.sendRedirect(request.getContextPath() + destination);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = session == null ? null : (User) session.getAttribute(LoginServlet.ATR_SESSION_USER);

        String destination;
        boolean isRedirect = false;

        if (user != null && user.getRole().equals(User.Role.MEMBER)) {
            Article article = extractArticle(request);
            Map<String, String> errors = validateArticle(article);

            if (errors.isEmpty()) {
                article.setAuthor(user);
                boolean isSuccess = articleRepo.create(article);
                if (isSuccess) {
                    destination = SUCCESS;
                    isRedirect = true;
                } else {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected error creating Article");
                    return;
                }
            } else {
                destination = FAILED;
                request.setAttribute(ATR_ERRORS, errors);
            }
        } else {
            destination = INVALID_USER;
            isRedirect = true;
        }

        if (isRedirect)
            response.sendRedirect(request.getContextPath() + destination);
        else
            request.getRequestDispatcher(destination).forward(request, response);
    }

    private Article extractArticle(HttpServletRequest request) {
        String title = request.getParameter(PARAM_TITLE);
        String description = request.getParameter(PARAM_DESCRIPTION);
        String content = request.getParameter(PARAM_CONTENT);

        return Article.builder()
                .title(title)
                .description(description)
                .content(content)
                .status(Article.Status.NEW)
                .postingDate(new Date())
                .build();
    }

    private Map<String, String> validateArticle(Article article) {
        Map<String, String> errors = new HashMap<>();

        if (article.getTitle().length() < 8 || article.getTitle().length() > 100) {
            errors.put(PARAM_TITLE, "Title length 8 -> 100");
        }

        if (article.getDescription().isEmpty() || article.getDescription().length() > 300) {
            errors.put(PARAM_DESCRIPTION, "Description not empty, max length = 300");
        }

        if (article.getContent().isEmpty() || article.getContent().length() > 5_000) {
            errors.put(PARAM_CONTENT, "Content not empty, max length = 5.000");
        }

        return errors;
    }
}
