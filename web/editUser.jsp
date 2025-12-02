<%-- 
    Document   : editUser
    Created on : 26 nov 2025, 18:43:19
    Author     : creepergd
--%>

<%@page import="data.ChatDB"%>
<%@page import="models.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
	// this is there to prevent people from injecting it into the URL
	User loggedInUser = (User) request.getSession().getAttribute("loggedInUser");
	int userID = (request.getParameter("userID") != null) ? Integer.parseInt(request.getParameter("userID")) : -1;

	if (loggedInUser == null) {
		response.sendRedirect("Public");
		return;
	}

	User editedUser = (loggedInUser.getUserID() == userID) ? loggedInUser : ChatDB.selectUser(userID);

	request.setAttribute("editedUser", editedUser);
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
				<input type="hidden" name="userID" value="<c:out value="${editedUser.userID}" />">
				<p>
					New Username: <input type="text" name="newUsername" value="<c:out value="${editedUser.username}" />">
					<br>
					<span>${errors['newUsername']}</span>
					<br>
					New Password: (leave blank to keep it the same)
					<br>
					<input type="text" name="newPassword" placeholder='New Password Here'>
					<br>
					<span>${errors['newPassword']}</span>
					<br>
					New First Name: <input type="text" name="newFirstName" value="<c:out value="${editedUser.firstName}" />">
					<br>
					<span>${errors['newFirstName']}</span>
					<br>
					New Last Name: <input type="text" name="newLastName" value="<c:out value="${editedUser.lastName}" />">
					<br>
					<span>${errors['newLastName']}</span>
					<br>
					New Phone Number: <input type="tel" name="newPhoneNumber" value="<c:out value="${editedUser.phoneNumber}" />">
					<br>
					<span>${errors['newPhoneNumber']}</span>
					<br>
				</p>
				<br>
				<p>Enter the password for the logged in user to apply changes</p>
				<p>Current Password: <input type="password" name="currentPassword"></p>
				<span>${errors['currentPassword']}</span>
				<span>${errors['accessError']}</span>
				<br>
				<input type="submit" value="Apply Changes">
			</form>
			<br>
			<form action='Private' method='post' onsubmit="return confirm('Are you sure you want to delete this user?');">
				<input type='hidden' name='action' value='deleteUser'>
				<input type="hidden" name="userID" value="<c:out value="${editedUser.userID}" />">
				<input type='submit' value="Delete User">
			</form>
			<br>
			<form action='Private' method='post'>
				<input type='hidden' name='action' value='gotoProfile'>
				<input type='submit' value="Back">
			</form>
		</div>
	</body>
</html>
