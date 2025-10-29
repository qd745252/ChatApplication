<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register</title>
        <link rel="stylesheet" type="text/css" href="css/styles.css">
    </head>
    <body>
        <c:import url="/nav/publicNav.jsp"></c:import>
            <form action="Public" method="post">
                <input type="hidden" name="action" value="register">

            <%-- Each input has a corresponding error span that appears if ______Errors is not empty --%>
            <label>Username:</label>
            <input type="text" name="username" id="username" value="<c:out value='${username}' />">
            <span class="errorMsg"><c:out value="${errors.username}" /></span>

            <br>

            <label>Password:</label>
            <input type="password" name="password" id="password" value="<c:out value='${password}' />">
            <span class="errorMsg"><c:forTokens delims="," var="error" items="${errors.password}"><c:out value="${error}" /><br></c:forTokens></span>

            <br>

            <label>Email:</label>
            <input type="text" name="email" id="email" value="<c:out value='${email}' />">
            <span class="errorMsg"><c:forTokens delims="," var="error" items="${errors.email}"><c:out value="${error}"/><br></c:forTokens> </span>

            <br>

            <label>Birth Date:</label>
            <input type="date" name="birthDate" id="birthDate" value="<c:out value='${birthDate}' />">
            <span class="errorMsg"><c:out value="${errors.birthDate}" /></span>

            <br>

            <input type="submit" value="Register">
        </form>
    </body>
</html>
