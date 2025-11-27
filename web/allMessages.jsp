<%-- 
    Document   : AllUsers
    Created on : 26 nov 2025, 18:18:29
    Author     : creepergd
--%>

<%@page import="models.User"%>
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
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
    </body>
</html>
