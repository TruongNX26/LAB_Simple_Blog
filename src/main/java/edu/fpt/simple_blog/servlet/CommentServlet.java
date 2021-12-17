package edu.fpt.simple_blog.servlet;

import edu.fpt.simple_blog.entity.Article;
import edu.fpt.simple_blog.entity.Comment;
import edu.fpt.simple_blog.entity.User;
import edu.fpt.simple_blog.filter.MainFilter;
import edu.fpt.simple_blog.repository.CommentRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

@WebServlet("/comment-servlet")
public class CommentServlet extends HttpServlet {

    public static final String PARAM_CONTENT = "content";
    public static final String PARAM_ARTICLE = "articleId";

    private CommentRepository commentRepo;

    @Override
    public void init() throws ServletException {
        super.init();
        commentRepo = CommentRepository.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String content = request.getParameter(PARAM_CONTENT);
        long articleId = getArticleId(request);

        if(articleId > -1 && !content.isEmpty()) {
            HttpSession session = request.getSession();
            User user = session == null ? null : (User) session.getAttribute(LoginServlet.ATR_SESSION_USER);
            if(user != null) {
                Comment comment = Comment.builder()
                        .content(content)
                        .creationDate(new Date())
                        .user(user)
                        .article(new Article(articleId))
                        .build();

                commentRepo.create(comment);
            }
        }

        String destination = MainFilter.PREFIX_PAGE + MainFilter.DETAIL + "?" + DetailServlet.PARAM_ARTICLE + "=" + articleId;
        response.sendRedirect(request.getContextPath() + destination);
    }

    private long getArticleId(HttpServletRequest request) {
        long articleId = -1;
        try {
            articleId = Long.parseLong(request.getParameter(PARAM_ARTICLE));
        } catch (Exception e) { /* do nothing */ }

        return articleId;
    }
}
