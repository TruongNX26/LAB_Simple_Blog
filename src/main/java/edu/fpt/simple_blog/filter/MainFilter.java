package edu.fpt.simple_blog.filter;

import edu.fpt.simple_blog.constant.PathConstant;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class MainFilter implements Filter {

    public static final String PREFIX_PAGE = "/pages";
    public static final String PREFIX_ACTION = "/actions";

    public static final String LOGIN = "/login";
    public static final String REGISTER = "/register";
    public static final String LOGOUT = "/logout";
    public static final String HOME = "/home";
    public static final String CREATE_ARTICLE = "/create-article";
    public static final String ARTICLE = "/article";
    public static final String DETAIL = "/detail";
    public static final String ADMIN = "/admin";
    public static final String COMMENT = "/comment";
    public static final String CONFIRM = "/confirm";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        service(httpRequest, httpResponse, chain);
    }

    private void service(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String path = getPath(request);
        if (path.startsWith(PREFIX_PAGE))
            handlePage(request, response);
        else if (path.startsWith(PREFIX_ACTION))
            handleAction(request, response);
        else if (checkResourcePath(path)) {
            chain.doFilter(request, response);
        } else {
            sendError404(response, "Page or Action not found");
        }
    }

    private boolean checkResourcePath(String path) {
        return path.endsWith(".css") || path.endsWith(".js");
    }

    private void handlePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = getPath(request);
        String page = path.substring(PREFIX_PAGE.length());

        String destination = null;
        switch (page) {
            case LOGIN:
                destination = PathConstant.LOGIN_PAGE;
                break;
            case REGISTER:
                destination = PathConstant.REGISTER_PAGE;
                break;
            case HOME:
                destination = PathConstant.SEARCH_SERVLET;
                break;
            case CREATE_ARTICLE:
                destination = PathConstant.CREATE_ARTICLE_PAGE;
                break;
            case DETAIL:
                destination = PathConstant.DETAIL_SERVLET;
                break;
            case ADMIN:
                destination = PathConstant.ADMIN_SERVLET;
                break;
        }

        if (destination != null) {
            request.getRequestDispatcher(destination).forward(request, response);
        } else {
            sendError404(response, "Page not found");
        }
    }

    private void handleAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = getPath(request);
        String action = path.substring(PREFIX_ACTION.length());

        String destination = null;
        switch (action) {
            case LOGIN:
                destination = PathConstant.LOGIN_SERVLET;
                break;
            case REGISTER:
                destination = PathConstant.REGISTER_SERVLET;
                break;
            case LOGOUT:
                destination = PathConstant.LOGOUT_SERVLET;
                break;
            case ARTICLE:
                destination = PathConstant.ARTICLE_SERVLET;
                break;
            case COMMENT:
                destination = PathConstant.COMMENT_SERVLET;
                break;
            case CONFIRM:
                destination = PathConstant.CONFIRM_SERVLET;
                break;
        }

        if (destination != null) {
            request.getRequestDispatcher(destination).forward(request, response);
        } else {
            sendError404(response, "Action not found");
        }
    }

    private String getPath(HttpServletRequest request) {
        String uri = request.getRequestURI().toLowerCase();
        return uri.substring(request.getContextPath().length());
    }

    private void sendError404(HttpServletResponse response, String msg) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, msg);
    }
}
