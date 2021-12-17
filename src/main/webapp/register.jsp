<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="title" scope="request" value="Register"/>

<%@ page import="edu.fpt.simple_blog.servlet.RegisterServlet" %>
<c:set var="errors" scope="page" value="${requestScope[RegisterServlet.ATR_ERRORS]}"/>

<!doctype html>
<html lang="en">
<jsp:include page="component/head.jsp"/>
<body>
<jsp:include page="component/navbar.jsp"/>
<div class="container container-main">
    <div class="row justify-content-center">
        <div class="col-md-12 col-lg-12 col-xl-12" style="margin-bottom: 30px;">
            <h3 style="text-align: center;">Register</h3>
        </div>
        <div class="col col-md-6 col-lg-6 col-xl-6">
            <c:set var="email" scope="page" value="${RegisterServlet.PARAM_EMAIL}"/>
            <c:set var="name" scope="page" value="${RegisterServlet.PARAM_NAME}"/>
            <c:set var="password" scope="page" value="${RegisterServlet.PARAM_PASSWORD}"/>
            <form action="${pageContext.request.contextPath}/actions/register" method="POST" id="form-register">
                <div class="form-group">
                    <label>Email address</label>
                    <input name="${email}" type="email" class="form-control" value="${param[email]}">
                    <small class="error">${errors[email]}</small>
                </div>
                <div class="form-group">
                    <label>Name</label>
                    <input name="${name}" type="text" class="form-control" value="${param[name]}">
                    <small class="error">${errors[name]}</small>
                </div>
                <div class="form-group">
                    <label>Password</label>
                    <input name="${password}" type="password" class="form-control" value="${param[password]}" id="input-password">
                    <small class="error">${errors[password]}</small>
                </div>
                <div class="form-group">
                    <label>Confirm Password</label>
                    <input type="password" class="form-control" id="input-confirm">
                    <small class="error" id="alert-confirm">Confirm password doesn't match</small>
                </div>
                <button type="submit" class="btn btn-primary">Register</button>
            </form>
        </div>
    </div>
</div>

<script>
    let registerForm = document.getElementById("form-register");
    registerForm.onsubmit = handleRegisterForm;
    function handleRegisterForm(e) {
        e.preventDefault();

        let alertConfirm = document.getElementById("alert-confirm");
        let password = document.getElementById("input-password").value;
        let confirm = document.getElementById("input-confirm").value;

        if(password === confirm) {
            registerForm.submit();
        } else {
            alertConfirm.style.visibility = "visible";
        }
    }

</script>
<jsp:include page="component/script.jsp"/>
</body>
</html>
