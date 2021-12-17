<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="title" scope="request" value="Create Article"/>

<%@ page import="edu.fpt.simple_blog.servlet.ArticleServlet" %>

<c:if test="${sessionScope.SESSION_USER.role != 'MEMBER'}">
    <c:redirect url="/pages/login" />
</c:if>

<!doctype html>
<html lang="en">
<jsp:include page="component/head.jsp"/>
<body>
<jsp:include page="component/navbar.jsp"/>
<div class="container container-main">
    <div class="row">
        <div class="col">
            <c:set var="title" scope="page" value="${ArticleServlet.PARAM_TITLE}"/>
            <c:set var="description" scope="page" value="${ArticleServlet.PARAM_DESCRIPTION}"/>
            <c:set var="content" scope="page" value="${ArticleServlet.PARAM_CONTENT}"/>
            <c:set var="errors" scope="page" value="${requestScope.ERRORS}"/>
            <form action="${pageContext.request.contextPath}/actions/article" method="POST">
                <div class="form-group">
                    <label>Article Title</label>
                    <input name="${title}" value="${param[title]}" type="text" class="form-control">
                    <small class="error">${errors[title]}</small>
                </div>
                <div class="form-group">
                    <label>Description</label>
                    <textarea name="${description}" class="form-control" rows="2">${param[description]}</textarea>
                    <small class="error">${errors[description]}</small>
                </div>
                <div class="form-group">
                    <label>Content</label>
                    <textarea name="${content}" class="form-control" rows="10">${param[content]}</textarea>
                    <small class="error">${errors[content]}</small>
                </div>
                <button type="submit" class="btn btn-primary" style="margin-bottom: 100px;">Submit</button>
            </form>
        </div>
    </div>
</div>
<jsp:include page="component/script.jsp"/>
</body>
</html>
