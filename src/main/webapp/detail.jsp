<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="title" scope="request" value="Article Detail"/>

<%@ taglib prefix="fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="edu.fpt.simple_blog.servlet.CommentServlet" %>

<c:set var="article" scope="page" value="${requestScope.ARTICLE}"/>

<!doctype html>
<html lang="en">
<jsp:include page="component/head.jsp"/>
<body>
<jsp:include page="component/navbar.jsp"/>
<div class="container container-main">
    <div class="row">
        <div class="col">

            <h1 style="margin-bottom: 30px;">${article.title}</h1>
            <p class="description"><b>${article.description}</b></p>
            <p class="text-justify content">${article.content}</p>
            <hr class="dashed">
            <p>Author: <br/><span class="badge badge-primary">${article.author.name}</span></p>
            <p>Posting Date: <br/><span class="badge badge-info">
                <fmt:formatDate type="date" value="${article.postingDate}" />
            </span></p>
        </div>
    </div>
    <hr/>
    <div class="row">
        <div class="col">
            <h4>Comments: </h4>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-8">
            <div class="row" style="background: white; padding: 30px;">
                <c:forEach var="item" items="${article.comments}">
                    <div class="col-sm-12">
                        <div class="media">
                            <img src="https://upload.wikimedia.org/wikipedia/commons/9/99/Sample_User_Icon.png" class="mr-3 user-avatar">
                            <div class="media-body">
                                <h5 class="mt-0">${item.user.name}</h5>
                                <p>${item.content}</p>
                                <span class="badge badge-info">
                                    <fmt:formatDate type="both" value="${item.creationDate}"/>
                                </span>
                            </div>
                        </div>
                        <hr/>
                    </div>
                </c:forEach>

                <div class="col-sm-12">
                    <c:if test="${sessionScope.SESSION_USER.role == 'MEMBER'}">
                        <c:set var="articleId" scope="page" value="${CommentServlet.PARAM_ARTICLE}"/>
                        <c:set var="content" scope="page" value="${CommentServlet.PARAM_CONTENT}"/>
                        <h6 style="color: brown;">${sessionScope.SESSION_USER.name}</h6>
                        <form action="${pageContext.request.contextPath}/actions/comment" method="POST">
                            <input name="${articleId}" value="${article.id}" type="hidden">
                            <textarea name="${content}" class="comment-area" rows="4"></textarea>
                            <button type="submit" class="btn btn-primary"><i class="bi bi-send"></i> Send</button>
                        </form>
                    </c:if>
                    <c:if test="${empty sessionScope.SESSION_USER}">
                        <div class="alert alert-warning" role="alert">
                            Login to comment.
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="component/script.jsp"/>
</body>
</html>
