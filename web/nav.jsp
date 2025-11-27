<%-- 
    Document   : PublicNav
    Created on : Mar 25, 2025, 1:26:56 PM
    Author     : tp728946
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<head>
	<link rel="stylesheet" href="css/style.css" type="text/css">
</head>

<nav>
	<form id="home_button" action="Public" method="post">
		<input type="hidden" name="action" value="default">
		<input type="submit" value="Home">
	</form>
	<form action="Public" method="post">
		<input type="hidden" name="action" value="gotoRegister">
		<input type="submit" value="Register">
	</form>
	<c:if test="${not empty loggedInUser}">
		<form action="Private" method="post">
			<input type="hidden" name="action" value="default">
			<input type="submit" value="Profile">
		</form>	
		<form action="Private" method="post">
			<input type="hidden" name="action" value="gotoMessages">
			<input type="submit" value="Messages">
		</form>
		<form action="Private" method="post">
			<input type="hidden" name="action" value="logout">
			<input type="submit" value="Log Out">
		</form>
	</c:if>
</nav>

