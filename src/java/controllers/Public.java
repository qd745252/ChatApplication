package controllers;

import data.ChatDB;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.User;
import util.PasswordEncryption;
import util.Validation;

public class Public extends HttpServlet {

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

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
						url = "/Private";
					}

				} catch (NamingException | SQLException ex) {
					Logger.getLogger(Public.class.getName()).log(Level.SEVERE, null, ex);
				}
				break;
			}
			case "gotoRegister": {
				url = "/register.jsp";
				break;
			}
			case "register": {
				LinkedHashMap<String, String> errors = new LinkedHashMap<>();
				User user = new User();
				String username = request.getParameter("username");
				String firstName = request.getParameter("firstName");
				String lastName = request.getParameter("lastName");
				String phoneNumber = request.getParameter("phoneNumber");

				String plainPassword = request.getParameter("password");
				String hashedPassword = PasswordEncryption.hashPassword(plainPassword);

				errors.put("username", Validation.validateUsername(username));
				errors.put("password", Validation.validatePassword(plainPassword));
				errors.put("firstName", Validation.validateFirstName(firstName));
				errors.put("lastName", Validation.validateLastName(lastName));
				errors.put("phoneNumber", Validation.validatePhoneNumber(phoneNumber));

				boolean isValid = true;
				if (errors.get("username").isEmpty()) {
					user.setUsername(username.toLowerCase());
				} else {
					isValid = false;
				}
				if (errors.get("password").isEmpty()) {
					user.setPassword(hashedPassword);
				} else {
					isValid = false;
				}
				if (errors.get("firstName").isEmpty()) {
					user.setFirstName(firstName);
				} else {
					isValid = false;
				}
				if (errors.get("lastName").isEmpty()) {
					user.setLastName(lastName);
				} else {
					isValid = false;
				}
				if (errors.get("phoneNumber").isEmpty()) {
					String cleanedPhoneNumber = phoneNumber.replaceAll("[^0-9]", "");
					user.setPhoneNumber(cleanedPhoneNumber);
				} else {
					isValid = false;
				}

				if (!isValid) {
					request.setAttribute("errors", errors);
					request.setAttribute("username", username);
					request.setAttribute("password", plainPassword);
					request.setAttribute("firstName", firstName);
					request.setAttribute("lastName", lastName);
					request.setAttribute("phoneNumber", phoneNumber);

					url = "/register.jsp";
				} else {
					try {
						ChatDB.insertUser(user);
						request.getSession().setAttribute("loggedInUser", user);
						url = "/Private";
					} catch (NamingException | SQLException ex) {
						Logger.getLogger(Public.class.getName()).log(Level.SEVERE, null, ex); //this should literally never happen but just in case go back to public and log in terminal
						url = "/Public";
					}
				}
				break;
			}
		}

		getServletContext().getRequestDispatcher(url).forward(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	public String getServletInfo() {
		return "Short description";
	}
}
