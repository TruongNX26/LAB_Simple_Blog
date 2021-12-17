<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="title" scope="request" value="Home"/>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="edu.fpt.simple_blog.servlet.SearchServlet" %>

<!doctype html>
<html lang="en">
<jsp:include page="component/head.jsp"/>
<body>
<jsp:include page="component/navbar.jsp"/>
<div class="container container-main">
    <div class="row">
        <c:forEach var="item" items="${requestScope.ARTICLES}">
            <div class="col col-sm-7">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">
                            <a href="${pageContext.request.contextPath}/pages/detail?articleId=${item.id}">${item.title}</a>
                        </h5>
                        <p class="card-text">${item.description}</p>
                        <span class="badge badge-primary">${item.author.name}</span>
                        <span class="badge badge-info" style="float: right;">
                                <fmt:formatDate type="date" value="${item.postingDate}"/>
                            </span>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="row" style="margin-top: 50px; margin-bottom: 50px;">
        <div class="col">
            <nav>
                <ul class="pagination">
                    <c:set var="page" scope="page" value="${SearchServlet.PARAM_PAGE}"/>
                    <c:set var="search" scope="page" value="${SearchServlet.PARAM_SEARCH}"/>
                    <c:forEach begin="1" end="${requestScope[SearchServlet.ATR_MAX_PAGE]}" varStatus="loop">
                        <li class="page-item">
                            <a class="page-link"
                               href="${pageContext.request.contextPath}/pages/home?${search}=${param[search]}&${page}=${loop.index}">${loop.index}</a>
                        </li>
                    </c:forEach>
                </ul>
            </nav>
        </div>
    </div>
</div>
<jsp:include page="component/script.jsp"/>
</body>
</html>
