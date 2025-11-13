package controllers;

import data.ChatDB;
import java.io.IOException;
import javax.naming.NamingException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.User;
import util.PasswordEncryption;

public class Public extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "/index.jsp";

		String action = request.getParameter("action");
		if (action == null) {
			action = "default";
		}

		switch (action) {
			case "login": {
				try {
					String username = request.getParameter("username");
					String plainPassword = request.getParameter("password");
					String storedHash = ChatDB.getPasswordForUsername(username);

					boolean isPasswordCorrect = PasswordEncryption.checkPassword(plainPassword, storedHash);
				if (!isPasswordCorrect) {
						request.setAttribute("loginError", "Invalid Username or Password");
					} else {
						User loggedInUser = ChatDB.selectUserByUsername(username);
						request.getSession().setAttribute("loggedInUser", loggedInUser);
						url = "/Private?action=gotoProfile";
					}

				} catch (NamingException | SQLException ex) {
				}
				break;
			}
			case "gotoRegister": {
				url = "/register.jsp";
				break;
			}
			case "register": {
				String username = request.getParameter("username");
				String firstName = request.getParameter("firstName");
				String lastName = request.getParameter("lastName");
				String birthDateString = request.getParameter("birthDate");

				String plainPassword = request.getParameter("password");
				String hashedPassword = PasswordEncryption.hashPassword(plainPassword);

				boolean isValid = true;
				User user = new User();

//                if () {
//					user.setUsername(username);
//				} else {
//					isValid = false;
//				}
//
//                if () {
//					user.setPassword(hashedPassword);
//				} else {
//					isValid = false;
//				}
//
//				if (!isValid) {
//					request.setAttribute("errors", errors);
//					request.setAttribute("username", username);
//					request.setAttribute("password", plainPassword);
//					request.setAttribute("email", email);
//					request.setAttribute("birthDate", birthDate);
//
//					url = "/register.jsp";
//				} else {
//					try {
//						ChatDB.insert(user);
//					} catch (NamingException | SQLException ex) {
//						Logger.getLogger(Public.class.getName()).log(Level.SEVERE, null, ex);
//					}
//					url = "/index.jsp";
//
//				}
			}
			break;

		}

		getServletContext().getRequestDispatcher(url).forward(request, response);

	}

	@Override
	public String getServletInfo() {
		return "Short description";
	}
}
