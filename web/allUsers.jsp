<%-- 
    Document   : AllUsers
    Created on : 26 nov 2025, 18:18:29
    Author     : creepergd
--%>
<%@page import="data.ChatDB"%>
<%@page import="models.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	User loggedInUser = (User) request.getSession().getAttribute("loggedInUser");

	if (loggedInUser == null && !loggedInUser.getUsername().equals("admin")) {
		response.sendRedirect("Public");
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
			<h1>Chat:</h1>
			<div id="chat">
				<c:forEach items="${messages}" var="message">
					<c:choose>
						<c:when test="${ChatDB.selectUser(message.value.fromUserID).getUsername() eq loggedInUser.username}">
							<p>${ChatDB.selectUser(message.value.fromUserID).getUsername()} (you) to ${ChatDB.selectUser(message.value.toUserID).getUsername()}: ${message.value.messageContents}</p>
						</c:when>
						<c:otherwise>
							<p>${ChatDB.selectUser(message.value.fromUserID).getUsername()}: ${message.value.messageContents}</p>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</div>
			<br>
			<p>(up to 255 characters)</p>
			<br>
			<h2>Send a Message</h2>
			<form action="Private" method="post">
				<input type="hidden" name="action" value="sendMessage">
				<input type="text" name="toUsername" placeholder="To: " value="<c:out value='${toUsername}' />">
				<br>
				<span>${errors["toAndFromUserIDs"]}</span>
				<br>
				<input type="text" name="messageContents" placeholder="Enter Message Here" value="<c:out value='${messageContents}' />">
				<br>
				<span>${errors["messageContents"]}</span>
				<br>
				<input type="submit" value="Send">
			</form>
		</div>
	</body>
