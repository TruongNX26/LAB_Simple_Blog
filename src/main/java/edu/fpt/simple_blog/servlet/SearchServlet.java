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

@WebServlet("/search-servlet")
public class SearchServlet extends HttpServlet {

    public static final String PARAM_SEARCH = "search";
    public static final String PARAM_PAGE = "page";
    public static final String ATR_ARTICLES = "ARTICLES";
    public static final String ATR_MAX_PAGE = "MAX_PAGE";
    private static final int PAGE_SIZE = 5;

    private ArticleRepository articleRepo;

    @Override
    public void init() throws ServletException {
        super.init();
        articleRepo = ArticleRepository.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String value = request.getParameter(PARAM_SEARCH);
        int page = getPage(request);

        List<Article> articles = articleRepo.searchByContent(value, getOffset(page), PAGE_SIZE);
        int maxPage = MyUtil.getMaxPage(articleRepo.countSearchByContent(value), PAGE_SIZE);

        request.setAttribute(ATR_ARTICLES, articles);
        request.setAttribute(ATR_MAX_PAGE, maxPage);

        String destination = PathConstant.HOME_PAGE;
        request.getRequestDispatcher(destination).forward(request, response);
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
