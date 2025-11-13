<%-- 
    Document   : Login Page
    Author     : Theron Polivka, Tyler Weis, Conner Lewton
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <link rel="stylesheet" type="text/css" href="css/styles.css">
    </head>
    <body>
        <h1>Login Here</h1>
        <h2><c:out value="${message}" /></h2>
        <div class="inline">
            <span class="italics">Don't have an account?</span>
            <form action='Public' method="post">
                <input type="hidden" name="action" value="gotoRegister">
                <input type="submit" value="Register Here!">
            </form>
        </div>
        <form action="Public" method="post">
            <input type="hidden" name="action" value="login">
            <span class="errorMsg"><c:out value="${loginError}" /></span>
            <label>Username: </label>
            <input type="text" name="username">
            <label>Password: </label>
            <input type="password" name="password">
            <input type="submit" value="login">
        </form>
    </body>
</html>
