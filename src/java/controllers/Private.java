package controllers;

import data.ChatDB;
import java.io.IOException;
import java.util.LinkedHashMap;
import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Message;
import models.User;
import util.Validation;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.PasswordEncryption;

public class Private extends HttpServlet {

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = "/profile.jsp";
		HttpSession session = request.getSession(false);
		User loggedInUser = (User) request.getSession().getAttribute("loggedInUser");

		if (loggedInUser == null) {
			response.sendRedirect("Public");
			return;
		}

		try {
			User userInDB = ChatDB.selectUser(loggedInUser.getUserID());

			if (userInDB == null) {
				if (session != null) {
					session.invalidate();
				}
				response.sendRedirect("Public");
				return;
			}
		} catch (NamingException | SQLException ex) {
			Logger.getLogger(Private.class.getName()).log(Level.SEVERE, null, ex);
			if (session != null) {
				session.invalidate();
			}
			response.sendRedirect("Public");
			return;
		}

		String action = request.getParameter("action");
		if (action == null) {
			action = "default";
		}

		switch (action) {
			case "logout": {
				if (session != null) {
					session.invalidate();
				}
				response.sendRedirect("index.jsp");
				return;
			}
			case "gotoEditUser": {
				url = "/editUser.jsp";
				break;
			}
			case "gotoMessages": {
				url = "/messages.jsp";
				try {
					LinkedHashMap<Integer, Message> toUserMessages = ChatDB.selectAllMessagesToUser(loggedInUser.getUserID());
					LinkedHashMap<Integer, Message> fromUserMessages = ChatDB.selectAllMessagesFromUser(loggedInUser.getUserID());

					ArrayList<Message> allMessages = new ArrayList<>();
					allMessages.addAll(toUserMessages.values());
					allMessages.addAll(fromUserMessages.values());

					allMessages.sort(Comparator.comparingInt(Message::getMessageID));

					LinkedHashMap<Integer, Message> sortedMessagesHashMap = new LinkedHashMap<>();
					for (Message message : allMessages) {
						sortedMessagesHashMap.put(message.getMessageID(), message);
					}

					request.setAttribute("messages", sortedMessagesHashMap);
				} catch (NamingException | SQLException ex) {
					Logger.getLogger(Public.class.getName()).log(Level.SEVERE, null, ex);
					url = "/Public";
				}
				break;
			}
			case "sendMessage": {
				url = "/Private?action=gotoMessages";
				LinkedHashMap<String, String> errors = new LinkedHashMap<>();
				try {
					String messageContents = request.getParameter("messageContents");
					User fromUser = (User) request.getSession().getAttribute("loggedInUser");
					int fromUserID = fromUser != null ? fromUser.getUserID() : -1;

					String toUsername = request.getParameter("toUsername");
					int toUserID = -1;
					User toUser = null;

					if (toUsername == null || toUsername.isBlank()) {
						errors.put("toAndFromUserIDs", "Please enter a recipient user");
					} else {
						toUser = ChatDB.selectUserByUsername(toUsername);
						if (toUser == null) {
							errors.put("toAndFromUserIDs", "Recipient user not found");
						} else {
							toUserID = toUser.getUserID();
							String userValidationError = Validation.validateToAndFromUserIDs(toUserID, fromUserID);
							if (!userValidationError.isBlank()) {
								errors.put("toAndFromUserIDs", userValidationError);
							}
						}
					}
					if (messageContents == null || messageContents.isBlank()) {
						errors.put("messageContents", "Message cannot be empty");
					}

					if (errors.isEmpty()) {
						ChatDB.insertMessage(new Message(messageContents, toUserID, fromUserID));
					} else {
						request.setAttribute("toUsername", toUsername);
						request.setAttribute("messageContents", messageContents);
						request.setAttribute("errors", errors);
					}
				} catch (NamingException | SQLException ex) {
					Logger.getLogger(Public.class.getName()).log(Level.SEVERE, null, ex);
					errors.put("sqlError", "There is a problem with the database, please contact your Administrator");
					request.setAttribute("errors", errors);
					url = "/Public";
				}
				break;
			}
			case "editUser": {
				LinkedHashMap<String, String> errors = new LinkedHashMap<>();
				User newUser = new User();

				newUser.setUserID(loggedInUser.getUserID());

				String newUsername = request.getParameter("newUsername");
				String newPassword = request.getParameter("newPassword");
				String newFirstName = request.getParameter("newFirstName");
				String newLastName = request.getParameter("newLastName");
				String newPhoneNumber = request.getParameter("newPhoneNumber");
				String currentPassword = request.getParameter("currentPassword");

				boolean isAnyFieldUpdated = false;

				String finalFirstName = (newFirstName != null && !newFirstName.isBlank()) ? newFirstName : loggedInUser.getFirstName();
				String finalLastName = (newLastName != null && !newLastName.isBlank()) ? newLastName : loggedInUser.getLastName();
				String finalPhoneNumber = (newPhoneNumber != null && !newPhoneNumber.isBlank()) ? newPhoneNumber : loggedInUser.getPhoneNumber();

				if (newUsername != null && !newUsername.isBlank()) {
					isAnyFieldUpdated = true;
				}
				if (newFirstName != null && !newFirstName.isBlank()) {
					isAnyFieldUpdated = true;
				}
				if (newLastName != null && !newLastName.isBlank()) {
					isAnyFieldUpdated = true;
				}
				if (newPhoneNumber != null && !newPhoneNumber.isBlank()) {
					isAnyFieldUpdated = true;
				}
				if (newPassword != null && !newPassword.isBlank()) {
					isAnyFieldUpdated = true;
				}

				boolean currentPasswordMatches = false;

				if (isAnyFieldUpdated) {
					if (currentPassword == null || currentPassword.isBlank()) {
						errors.put("currentPassword", "Current password is required to make changes");
					} else if (PasswordEncryption.checkPassword(currentPassword, loggedInUser.getPassword())) {
						currentPasswordMatches = true;
					} else {
						errors.put("currentPassword", "Current password does not match");
					}
				}

				if (newUsername != null && !newUsername.isBlank()) {
					String usernameValidationError = Validation.validateUsername(newUsername);
					if (!usernameValidationError.isBlank()) {
						errors.put("newUsername", usernameValidationError);
					} else if (!newUsername.equalsIgnoreCase(loggedInUser.getUsername())) {
						try {
							User existingUser = ChatDB.selectUserByUsername(newUsername.toLowerCase());
							if (existingUser != null && existingUser.getUserID() != loggedInUser.getUserID()) {
								errors.put("newUsername", "This username is already in use.");
							} else {
								newUser.setUsername(newUsername.toLowerCase());
							}
						} catch (NamingException | SQLException ex) {
							Logger.getLogger(Public.class.getName()).log(Level.SEVERE, null, ex);
							url = "/Public";
						}
					} else {
						newUser.setUsername(loggedInUser.getUsername());
					}
				} else {
					newUser.setUsername(loggedInUser.getUsername());
				}

				String firstNameError = Validation.validateFirstName(finalFirstName);
				if (firstNameError.isBlank()) {
					newUser.setFirstName(finalFirstName);
				} else {
					errors.put("newFirstName", firstNameError);
				}

				String lastNameError = Validation.validateLastName(finalLastName);
				if (lastNameError.isBlank()) {
					newUser.setLastName(finalLastName);
				} else {
					errors.put("newLastName", lastNameError);
				}

				String phoneError = Validation.validatePhoneNumber(finalPhoneNumber);
				if (phoneError.isBlank()) {
					String cleanedPhone = finalPhoneNumber.replaceAll("[^0-9]", "");
					newUser.setPhoneNumber(cleanedPhone);
				} else {
					errors.put("newPhoneNumber", phoneError);
				}

				if (newPassword != null && !newPassword.isBlank()) {
					if (isAnyFieldUpdated) {
						if (!currentPasswordMatches) {
							errors.put("currentPassword", "Current password is incorrect");
						} else {
							String passwordValidationError = Validation.validatePassword(newPassword);
							if (passwordValidationError.isBlank()) {
								newUser.setPassword(PasswordEncryption.hashPassword(newPassword));
							} else {
								errors.put("newPassword", passwordValidationError);
							}
						}
					} else {
						String passwordValidationError = Validation.validatePassword(newPassword);
						if (passwordValidationError.isBlank()) {
							newUser.setPassword(PasswordEncryption.hashPassword(newPassword));
						} else {
							errors.put("newPassword", passwordValidationError);
						}
					}
				} else {
					newUser.setPassword(loggedInUser.getPassword());
				}

				if (errors.isEmpty() && (isAnyFieldUpdated || newPassword != null && !newPassword.isBlank())) {
					try {
						ChatDB.updateUser(newUser);
						request.getSession().setAttribute("loggedInUser", newUser);
					} catch (NamingException | SQLException ex) {
						Logger.getLogger(Public.class.getName()).log(Level.SEVERE, null, ex);
					}
				} else {
					url = "/editUser.jsp";
					request.setAttribute("errors", errors);
					request.setAttribute("newUsername", newUsername);
					request.setAttribute("newFirstName", newFirstName);
					request.setAttribute("newLastName", newLastName);
					request.setAttribute("newPhoneNumber", newPhoneNumber);
				}
				break;
			}
			case "deleteUser": {
				int userID = (request.getParameter("userID") != null) ? Integer.parseInt(request.getParameter("userID")) : -1;

				if (userID > 0) {
					String username = loggedInUser.getUsername();

					boolean isAdmin = username.equals("admin");
					boolean isSelf = (loggedInUser.getUserID() == userID);

					if ((isAdmin && !isSelf) || (!isAdmin && isSelf)) {
						try {
							ChatDB.deleteUser(userID);
						} catch (NamingException | SQLException ex) {
							Logger.getLogger(Public.class.getName()).log(Level.SEVERE, null, ex);
						}
						response.sendRedirect("Public");
						return;
					}
				}
				url = "/editUser.jsp";
				break;
			}
			case "viewAllUsers": {
				url = "/allUsers.jsp";
				break;
			}
			case "viewAllMessages": {
				url = "/allMessages.jsp";
				break;
			}
		}
		getServletContext().getRequestDispatcher(url).forward(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	public String getServletInfo() {
		return "Short description";
	}
}
