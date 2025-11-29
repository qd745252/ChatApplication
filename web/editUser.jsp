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
        <title>Edit Page</title>
		<link rel="stylesheet" href="css/style.css" type="text/css">
    </head>
    <body>
		<c:import url="nav.jsp"/>
		<div id="container">
			<h1>Edit Page</h1>
			<form action="Private" method="post">
				<input type="hidden" name="action" value="editUser">
				<p>
					New Username: <input type="text" name="newUsername" value="<c:out value="${loggedInUser.username}" />">
					<br>
					<span>${errors['newUsername']}</span>
					<br>
					New Password: (leave blank to keep it the same)
					<br>
					<input type="text" name="newPassword" placeholder='New Password Here'>
					<br>
					<span>${errors['newPassword']}</span>
					<br>
					New First Name: <input type="text" name="newFirstName" value="<c:out value="${loggedInUser.firstName}" />">
					<br>
					<span>${errors['newFirstName']}</span>
					<br>
					New Last Name: <input type="text" name="newLastName" value="<c:out value="${loggedInUser.lastName}" />">
					<br>
					<span>${errors['newLastName']}</span>
					<br>
					New Phone Number: <input type="tel" name="newPhoneNumber" value="<c:out value="${loggedInUser.phoneNumber}" />">
					<br>
					<span>${errors['newPhoneNumber']}</span>
					<br>
				</p>
				<br>
				<p>Enter current password to apply changes or to delete your account</p>
				<p>Current Password: <input type="password" name="currentPassword"></p>
				<span>${errors['currentPassword']}</span>
				<br>
				<br>
				<input type="submit" value="Apply Changes">
			</form>
			<form action='Private' method='post'>
				<input type='hidden' name='action' value='deleteUser'>
				<input id='delete' type='submit' value="Delete User">
			</form>
		</div>
	</body>
</html>
