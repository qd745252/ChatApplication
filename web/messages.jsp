<%-- 
    Document   : messages
    Created on : 26 nov 2025, 17:53:21
    Author     : creepergd
--%>

<%@page import="data.ChatDB"%>
<%@page import="models.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
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
			<p> Inbox </p>
			<br>
			<table>
				<thead>
					<tr>
						<th>From</th>
						<th>Message</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${toUserMessages}" var="message">
						<tr>
							<td>
								${message.key}	
							</td>
							<td>
								${message.value.messageContents}
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<br>
			<p>Sent</p>
			<br>
			<table>
				<thead>
					<tr>
						<th>From</th>
						<th>Message</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${fromUserMessages}" var="message">
						<tr>
							<td>
								${message.key} (you)
							</td>
							<td>
								${message.value.messageContents}
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<form action="Private" method="post">
				<input type="hidden" name="action" value="sendMessage">
				<input type="text" name="messageContents" placeholder="Enter Message Here">
				<input type="submit" value="Send">
			</form>
		</div>
    </body>
</html>
