<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <link rel="stylesheet" type="text/css" href="css/style.css">
    </head>
    <body>
		<c:import url="nav.jsp"/>
		<div id="container">
			<h1>Welcome to my chat app!</h1>
			<p>Don't have an account?</p>
			<br>
			<form action='Public' method="post">
				<input type="hidden" name="action" value="gotoRegister">
				<input type="submit" value="Register Here!">
			</form>
			<form action="Public" method="post">
				<input type="hidden" name="action" value="login">
				<span><c:out value="${loginError}" /></span>
				<br>
				<label>Username: </label>
				<input type="text" name="username">
				<br>
				<label>Password: </label>
				<input type="password" name="password">
				<br>
				<br>
				<input type="submit" value="Login">
			</form>
			<span><c:out value="${errors['sqlError']}" /></span>
		</div>
    </body>
</html>