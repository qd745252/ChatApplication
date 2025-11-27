<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register</title>
		<link rel="stylesheet" href="css/style.css" type="text/css">
    </head>
    <body>
		<c:import url="nav.jsp"/>
		<div id="container">
			<form action="Public" method="post">
				<input type="hidden" name="action" value="register">

				<span>* </span><label>Username:</label>
				<input type="text" name="username" id="username" value="<c:out value='${username}' />">
				<br>
				<span>${errors['username']}</span>
				<br>
				<span>* </span><label>Password:</label>
				<input type="password" name="password" id="password" value="<c:out value='${password}' />">
				<br>
				<span>${errors['password']}</span>
				<br>
				<span>* </span><label>First Name:</label>
				<input type="text" name="firstName" id="firstName" value="<c:out value='${firstName}' />">
				<br>
				<span>${errors['firstName']}</span>
				<br>
				<span>* </span><label>Last Name:</label>
				<input type="text" name="lastName" id="lastName" value="<c:out value='${lastName}' />">
				<br>
				<span>${errors['lastName']}</span>
				<br>
				<label>Phone Number:</label>
				<input type="tel" name="phoneNumber" id="phoneNumber" value="<c:out value='${phoneNumber}' />">
				<br>
				<span>${errors['phoneNumber']}</span>
				<br>
				<br>
				<span>* Required Field</span>
				<br>
				<br>
				<input type="submit" value="Register">
			</form>
		</div>
    </body>
</html>

