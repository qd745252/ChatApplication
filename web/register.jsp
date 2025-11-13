<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register</title>
    </head>
    <body>
        <form action="Public" method="post">
            <input type="hidden" name="action" value="register">

            <label>Username:</label>
            <input type="text" name="username" id="username" value="<c:out value='${username}' />">
            
			<label>Password:</label>
            <input type="password" name="password" id="password" value="<c:out value='${password}' />">

			<label>First Name:</label>
			<input type="text" name="firstName" id="firstName" value="<c:out value='${firstName}' />">

			<label>Last Name:</label>
			<input type="text" name="lastName" id="lastName" value="<c:out value='${lastName}' />">

			<label>Phone Number (optional):</label>
			<input type="tel" name="phoneNumber" id="phoneNumber" value="<c:out value='${phoneNumber}' />">
            <input type="submit" value="Register">
        </form>
    </body>
</html>

