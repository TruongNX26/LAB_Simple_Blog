<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="title" scope="request" value="Admin"/>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="edu.fpt.simple_blog.servlet.ArticleServlet" %>
<%@ page import="edu.fpt.simple_blog.servlet.AdminServlet" %>

<c:if test="${sessionScope.SESSION_USER.role != 'ADMIN'}">
    <c:redirect url="/pages/login"/>
</c:if>

<!doctype html>
<html lang="en">
<jsp:include page="component/head.jsp"/>
<body>
<jsp:include page="component/navbar.jsp"/>
<div class="container container-main">
    <div class="row">
        <div class="col col-md-6 col-lg-6 col-xl-6">
            <c:set var="title" scope="page" value="${AdminServlet.PARAM_TITLE}"/>
            <c:set var="content" scope="page" value="${AdminServlet.PARAM_CONTENT}"/>
            <c:set var="status" scope="page" value="${AdminServlet.PARAM_STATUS}"/>
            <form action="${pageContext.request.contextPath}/pages/admin" method="GET">
                <div class="row">
                    <div class="col">
                        <label>Title: </label>
                        <input name="${title}" value="${param[title]}" type="text" class="form-control">
                    </div>
                    <div class="col">
                        <label>Status: </label>
                        <select name="${status}" class="form-control">
                            <option>ANY</option>
                            <c:forEach var="item" items="${requestScope.STATUSES}">
                                <option value="${item.name()}"
                                        <c:if test="${param[status] == item.name()}">selected</c:if>
                                >${item.name()}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <label>Content: </label>
                        <input name="${content}" value="${param[content]}" type="text" class="form-control">
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <button type="submit" class="btn btn-primary"><i class="bi bi-search"></i> Search</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <hr>
    <div class="row">
        <c:forEach var="item" items="${requestScope.ARTICLES}">
            <div class="col col-sm-10">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">
                            <a href="${pageContext.request.contextPath}/pages/detail?articleId=${item.id}">${item.title}</a>
                        </h5>
                        <p class="card-text">${item.description}</p>
                        <div>
                            <span class="badge badge-primary">${item.author.name}</span>
                            <span class="badge badge-info">
                                <fmt:formatDate type="date" value="${item.postingDate}"/>
                            </span>
                            <br/>
                            <span class="badge badge-success">${item.status}</span>
                        </div>

                        <div style="margin-top: 20px;">
                            <c:if test="${item.status == 'NEW'}">
                                <form action="${pageContext.request.contextPath}/actions/article" method="GET">
                                    <input name="${ArticleServlet.PARAM_ID}" value="${item.id}" type="hidden"/>
                                    <input name="${ArticleServlet.PARAM_ACTION}" value="APPROVE" type="hidden"/>
                                    <button type="submit" class="btn btn-outline-success"><i
                                            class="bi bi-check2-circle"></i> Approve
                                    </button>
                                </form>
                            </c:if>

                        </div>
                        <div style="margin-top: 20px;">
                            <c:if test="${item.status != 'DELETED'}">
                                <form action="${pageContext.request.contextPath}/actions/article" method="GET">
                                    <input name="${ArticleServlet.PARAM_ID}" value="${item.id}" type="hidden"/>
                                    <input name="${ArticleServlet.PARAM_ACTION}" value="DELETE" type="hidden"/>
                                    <button type="submit" class="btn btn-outline-danger"><i class="bi bi-trash"></i>
                                        Delete
                                    </button>
                                </form>
                            </c:if>
                        </div>

                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="row" style="margin-top: 50px; margin-bottom: 50px;">
        <div class="col">
            <nav>
                <ul class="pagination">
                    <c:set var="page" value="${AdminServlet.PARAM_PAGE}"/>
                    <c:forEach begin="1" end="${requestScope[AdminServlet.ATR_MAX_PAGE]}" varStatus="loop">
                        <li class="page-item">
                            <a class="page-link"
                               href="${pageContext.request.contextPath}/pages/admin?${title}=${param[title]}&${content}=${param[content]}&${status}=${param[status]}&${page}=${loop.index}">${loop.index}</a>
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
