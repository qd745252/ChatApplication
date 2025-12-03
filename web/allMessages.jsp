<%-- 
    Document   : AllUsers
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
        <title>All Messages</title>
		<link rel="stylesheet" href="css/style.css" type="text/css">
    </head>
    <body>
		<c:import url="nav.jsp"/>
		<div id="container">
			<h1>Admin Messages Page:</h1>
			<table>
				<thead>
					<tr>
						<th>Message Contents</th>
						<th>To User</th>
						<th>From User</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${messages}" var="message">
						<tr>
								<td class="messageContents"><c:out value='${message.value.messageContents}' /></td>
							<td><c:out value='${ChatDB.selectUser(message.value.toUserID).getUsername()}' /></td>
							<td><c:out value='${ChatDB.selectUser(message.value.fromUserID).getUsername()}' /></td>
							<td>
								<form action='Private' method='post' onsubmit="return confirm('Are you sure you want to delete this message?');">
									<input type='hidden' name='action' value='deleteMessage'>
									<input type="hidden" name="messageID" value="<c:out value="${message.value.messageID}" />">
									<input type='submit' value="Delete Message">
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