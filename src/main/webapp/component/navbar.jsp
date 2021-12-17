<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="edu.fpt.simple_blog.servlet.SearchServlet" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/pages/home">Home</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <c:if test="${sessionScope.SESSION_USER.role != 'ADMIN'}">
                <li class="nav-item active">
                    <a class="nav-link" href="${pageContext.request.contextPath}/pages/create-article">Post Article <span class="sr-only">(current)</span></a>
                </li>
            </c:if>
        </ul>

        <c:if test="${empty sessionScope.SESSION_USER}">
            <a href="${pageContext.request.contextPath}/pages/login" class="btn btn-primary navbar-comp">Login</a>
        </c:if>

        <c:if test="${not empty sessionScope.SESSION_USER}">
            <span class="badge badge-success navbar-comp">${sessionScope.SESSION_USER.name}</span>
            <c:if test="${sessionScope.SESSION_USER.role == 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/pages/admin" class="btn btn-primary navbar-comp">ADMIN</a>
            </c:if>
            <a href="${pageContext.request.contextPath}/actions/logout" class="btn btn-primary navbar-comp"><i class="bi bi-box-arrow-left"></i></a>
        </c:if>

        <c:set var="search" scope="page" value="${SearchServlet.PARAM_SEARCH}"/>
        <form action="${pageContext.request.contextPath}/pages/home" method="GET" class="form-inline my-2 my-lg-0">
            <input name="${search}" value="${param[search]}" type="text" class="form-control mr-sm-2" placeholder="Search">
            <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
        </form>
    </div>
</nav>