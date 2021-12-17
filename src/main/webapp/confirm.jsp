<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="title" scope="request" value="Confirm"/>

<%@ page import="edu.fpt.simple_blog.servlet.ConfirmServlet" %>
<%@ page import="edu.fpt.simple_blog.servlet.ConfirmServlet" %>

<!doctype html>
<html lang="en">
<jsp:include page="component/head.jsp"/>
<body>
<jsp:include page="component/navbar.jsp"/>

<div class="container container-main">
    <div class="row">
        <div class="col col-md-6 col-lg-6 col-xl-6">
            <h2>Confirm User:</h2>
            <form action="${pageContext.request.contextPath}/actions/confirm" method="POST">
                <div class="form-group">
                    <label>Email address</label>
                    <input name="${ConfirmServlet.PARAM_EMAIL}" value="${requestScope.USER_EMAIL}" type="email" class="form-control" readonly>
                </div>
                <div class="form-group">
                    <label>Code</label>
                    <input name="${ConfirmServlet.PARAM_CODE}" type="number" class="form-control">
                    <small class="error">${requestScope.ALERT}</small>
                </div>
                <button type="submit" class="btn btn-primary">Confirm</button>
            </form>
        </div>
    </div>
</div>

<jsp:include page="component/script.jsp"/>
</body>
</html>