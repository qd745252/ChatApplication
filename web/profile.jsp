<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>My Profile</title>
    </head>
    <body>
        <h1>Welkom bij onze website, <c:out value='${loggedInUser.username}' /></h1>

        <div class="profileContainer">
            <div>
                <h3>Gebruikersnaam: <c:out value="${loggedInUser.username}" /></h3>
                <p>Eerstennaam: <c:out value="${loggedInUser.firstName}" /></p>
                <p>Laatsteennaam: <c:out value="${loggedInUser.lastName}" /></p>
				<c:if test="${not empty loggedInUser.phoneNumber}">
                	<p>Telefoonnummer: <c:out value="${loggedInUser.phoneNumber}" /></p>
				</c:if>
            </div>
        </div>
    </body>
</html>
