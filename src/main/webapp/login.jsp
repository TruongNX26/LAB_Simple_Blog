<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="title" scope="request" value="Login"/>

<%@ page import="edu.fpt.simple_blog.servlet.LoginServlet" %>

<!doctype html>
<html lang="en">
<jsp:include page="component/head.jsp"/>
<body>
<jsp:include page="component/navbar.jsp"/>
<div class="container container-main">
    <div class="row justify-content-center">
        <div class="col-md-12 col-lg-12 col-xl-12" style="margin-bottom: 30px;">
            <h3 style="text-align: center;">Login</h3>
        </div>
        <div class="col col-md-6 col-lg-6 col-xl-6">
            <c:set var="email" scope="page" value="${LoginServlet.PARAM_EMAIL}"/>
            <c:set var="password" scope="page" value="${LoginServlet.PARAM_PASSWORD}"/>
            <form action="${pageContext.request.contextPath}/actions/login" method="POST">
                <div class="form-group">
                    <label>Email address</label>
                    <input name="${email}" type="email" class="form-control" value="${param[email]}">
                </div>
                <div class="form-group">
                    <label>Password</label>
                    <input name="${password}" type="password" class="form-control">
                </div>
                <button type="submit" class="btn btn-primary">Login</button>
                <a href="${pageContext.request.contextPath}/pages/register"
                   style="float: right;">Register</a>
            </form>

            <c:if test="${param.status == 'failed'}">
                <div class="alert alert-danger" role="alert">
                    Login failed, wrong email or password!
                </div>
            </c:if>

            <c:if test="${param.status == 'confirmed'}">
                <div class="alert alert-primary" role="alert">
                    Email confirmed successfully!
                </div>
            </c:if>

        </div>
    </div>
</div>
<jsp:include page="component/script.jsp"/>
</body>
</html>
