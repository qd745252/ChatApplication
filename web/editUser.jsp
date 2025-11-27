<%-- 
    Document   : editUser
    Created on : 26 nov 2025, 18:43:19
    Author     : creepergd
--%>

<%@page import="models.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
	User loggedInUser = (User) request.getSession().getAttribute("loggedInUser");

	if (loggedInUser == null) {
		response.sendRedirect("Public");
		return;
	}
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>My Profile</title>
		<link rel="stylesheet" href="css/style.css" type="text/css">
    </head>
    <body>
		<c:import url="nav.jsp"/>
		<div id="container">
			<h1>Welkom bij onze website, <c:out value='${loggedInUser.username}' /></h1>

			<h3>Gebruikersnaam: <c:out value="${loggedInUser.username}" /></h3>
			<p>Eerstennaam: <c:out value="${loggedInUser.firstName}" /></p>
			<p>Laatsteennaam: <c:out value="${loggedInUser.lastName}" /></p>
			<c:if test="${not empty loggedInUser.phoneNumber}">
				<p>Telefoonnummer: <c:out value="${loggedInUser.phoneNumber}" /></p>
			</c:if>
			<br>
			<form action="Private" method="post">
				<input type="hidden" name="action" value="editUser">
				<input type="submit" value="Edit Profile">
			</form>
		</div>
    </body>
</html>
