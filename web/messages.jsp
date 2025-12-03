<%-- 
    Document   : messages
    Created on : 26 nov 2025, 17:53:21
    Author     : creepergd
--%>

<%@page import="models.User"%>
<%@page import="data.ChatDB"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	// this is there to prevent people from injecting it into the URL
	User loggedInUser = (User) request.getSession().getAttribute("loggedInUser");

	if (loggedInUser == null) {
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
							<form action="Private" method="post" onsubmit="return confirm('Are you sure you want to delete this message?');">
								<input type="hidden" name="action" value="deleteMessage">
								<input type="hidden" name="messageID" value="${message.value.messageID}">
								<input type="hidden" name="fromUserID" value="${message.value.fromUserID}">
								<input type="submit" value="Delete Message">
							</form>
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
				<textarea name="messageContents" placeholder="Enter Message Here" rows="5" cols="50"><c:out value='${messageContents}' /></textarea>
				<br>
				<span>${errors["messageContents"]}</span>
				<br>
				<input type="submit" value="Send">
			</form>
		</div>
	</body>
</html>