package edu.fpt.simple_blog.servlet;

import edu.fpt.simple_blog.constant.PathConstant;
import edu.fpt.simple_blog.entity.Article;
import edu.fpt.simple_blog.entity.User;
import edu.fpt.simple_blog.repository.ArticleRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/detail-servlet")
public class DetailServlet extends HttpServlet {

    public static final String PARAM_ARTICLE = "articleId";
    public static final String ATR_ARTICLE = "ARTICLE";

    private ArticleRepository articleRepo;

    @Override
    public void init() throws ServletException {
        super.init();
        articleRepo = ArticleRepository.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long articleId = -1;
        try {
            articleId = Long.parseLong(request.getParameter(PARAM_ARTICLE));
        } catch (Exception e) { /* do nothing */ }

        HttpSession session = request.getSession();
        User user = session == null ? null : (User) session.getAttribute(LoginServlet.ATR_SESSION_USER);

        Article.Status status;
        if(user != null && user.getRole() == User.Role.ADMIN) status = null;
        else status = Article.Status.ACTIVE;

        Article article = articleRepo.searchById(articleId, status);

        if(article != null) {
            String destination = PathConstant.DETAIL_PAGE;
            request.setAttribute(ATR_ARTICLE, article);
            request.getRequestDispatcher(destination).forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Article not found");
        }
    }
}
