<%-- 
    Document   : allUsers
    Created on : 26 nov 2025, 18:18:29
    Author     : creepergd
--%>
<%@page import="data.ChatDB"%>
<%@page import="models.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	// this is there to prevent people from injecting it into the URL
	User loggedInUser = (User) request.getSession().getAttribute("loggedInUser");

	if (loggedInUser == null ) {
		response.sendRedirect("Public");
		return;
	}

	if (!loggedInUser.getUsername().equalsIgnoreCase("admin")) {
		response.sendRedirect("Private");
		return;
	}
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Messages</title>
		<link rel="stylesheet" href="css/style.css" type="text/css">
    </head>
    <body>
		<c:import url="nav.jsp"/>
		<div id="container">
			<h1>Admin Users Page:</h1>
			<table>
				<thead>
					<tr>
						<th>Username</th>
						<th>First Name</th>
						<th>Last Name</th>
						<th>Phone Number</th>
						<th></th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${users}" var="user">
						<tr>
							<td><c:out value='${user.username}' /></td>
							<td><c:out value='${user.firstName}' /></td>
							<td><c:out value='${user.lastName}' /></td>
							<c:choose>
								<c:when test="${empty user.phoneNumber}">
									<td>NULL</td>
								</c:when>
								<c:otherwise>
									<td><c:out value='${user.phoneNumber}' /></td>
								</c:otherwise>
							</c:choose>
							<td>
								<form action="Private" method="post">
									<input type="hidden" name="action" value="gotoEditUser">
									<input type="hidden" name="userID" value="<c:out value='${user.userID}' />">
									<input type="submit" value="Edit User">
								</form>
							</td>
							<td>
								<form action='Private' method='post' onsubmit="return confirm('Are you sure you want to delete this user?');">
									<input type='hidden' name='action' value='deleteUser'>
									<input type="hidden" name="userID" value="<c:out value="${user.userID}" />">
									<input type='submit' value="Delete User">
								</form>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<span><c:out value="${errors['accessError']}" /></span>
		</div>
	</body>
</html>