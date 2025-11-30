<%@page import="models.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
	User loggedInUser = (User) request.getSession().getAttribute("loggedInUser");

	if (loggedInUser == null) {
		response.sendRedirect("Public");
		return;
	}

	String formattedPhoneNumber = String.format("(%s) %s-%s",
			loggedInUser.getPhoneNumber().substring(0, 3),
			loggedInUser.getPhoneNumber().substring(3, 6),
			loggedInUser.getPhoneNumber().substring(6, 10));
	request.setAttribute("formattedPhoneNumber", formattedPhoneNumber);
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
			<h1>Welcome to our website, <c:out value='${loggedInUser.username}' /></h1>

			<h3>Username: <c:out value="${loggedInUser.username}" /></h3>
			<p>First Name: <c:out value="${loggedInUser.firstName}" /></p>
			<p>Last Name: <c:out value="${loggedInUser.lastName}" /></p>
			<c:if test="${not empty loggedInUser.phoneNumber}">
				<p>Phone Number: <c:out value="${formattedPhoneNumber}" /></p>
			</c:if>
			<br>
			<form action="Private" method="post">
				<input type="hidden" name="action" value="gotoEditUser">
				<input type="submit" value="Edit Profile">
			</form>
		</div>
    </body>
</html>
