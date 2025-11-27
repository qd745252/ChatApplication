<%-- 
    Document   : nav 
    Created on : Nov 26 2025, 17:26:56
    Author     : creepergd
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav>
	<form action="Public" method="post">
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

		<c:if test="${loggedInUser.username eq 'admin'}">
			<form action="Private" method="post">
				<input type="hidden" name="action" value="viewAllMessages">
				<input type="submit" value="View All Messages">
			</form>
			<form action="Private" method="post">
				<input type="hidden" name="action" value="viewAllUsers">
				<input type="submit" value="View All Users">
			</form>
		</c:if>
		<form action="Private" method="post">
			<input type="hidden" name="action" value="logout">
			<input type="submit" value="Log Out">
		</form>
	</c:if>
</nav>

