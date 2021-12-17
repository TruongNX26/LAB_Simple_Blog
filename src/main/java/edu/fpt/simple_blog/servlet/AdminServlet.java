package edu.fpt.simple_blog.servlet;

import edu.fpt.simple_blog.constant.PathConstant;
import edu.fpt.simple_blog.entity.Article;
import edu.fpt.simple_blog.repository.ArticleRepository;
import edu.fpt.simple_blog.util.MyUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin-servlet")
public class AdminServlet extends HttpServlet {

    public static final String PARAM_TITLE = "s_title";
    public static final String PARAM_CONTENT = "s_content";
    public static final String PARAM_STATUS = "s_status";
    public static final String PARAM_PAGE = "page";

    public static final int PAGE_SIZE = 5;
    public static final String ATR_ARTICLES = "ARTICLES";
    public static final String ATR_MAX_PAGE = "MAX_PAGE";

    private ArticleRepository articleRepo;

    @Override
    public void init() throws ServletException {
        super.init();
        articleRepo = ArticleRepository.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter(PARAM_TITLE);
        String content = request.getParameter(PARAM_CONTENT);
        String statusName = request.getParameter(PARAM_STATUS);
        Article.Status status = getArticleStatus(statusName);
        int page = getPage(request);

        List<Article> articles = articleRepo.adminSearch(title, content, status, getOffset(page), PAGE_SIZE);
        long count = articleRepo.getCountAdmin(title, content, status);
        request.setAttribute(ATR_ARTICLES, articles);
        request.setAttribute(ATR_MAX_PAGE, MyUtil.getMaxPage(count, PAGE_SIZE));

        String destination = PathConstant.ADMIN_PAGE;

        request.setAttribute("STATUSES", Article.Status.values());
        request.getRequestDispatcher(destination).forward(request, response);
    }

    private Article.Status getArticleStatus(String statusName) {
        Article.Status status = null;
        try {
            status = Article.Status.valueOf(statusName);
        } catch (Exception e) { /* do nothing */ }
        return status;
    }

    private int getPage(HttpServletRequest request) {
        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter(PARAM_PAGE));
        } catch (Exception e) { /* do nothing */ }

        return page;
    }

    private int getOffset(int page) {
        int offset = 0;
        if(page > 1) {
            offset = (page - 1) * PAGE_SIZE;
        }
        return offset;
    }
}
