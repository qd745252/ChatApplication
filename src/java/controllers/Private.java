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
				url = "/Public";
				break;
			}
			case "gotoEditUser": {
				int userID = (request.getParameter("userID") != null) ? Integer.parseInt(request.getParameter("userID")) : -1;
				request.setAttribute("userID", userID);

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
					allMessages.forEach(message -> {
						sortedMessagesHashMap.put(message.getMessageID(), message);
					});

					request.setAttribute("messages", sortedMessagesHashMap);
				} catch (NamingException | SQLException ex) {
					Logger.getLogger(Public.class.getName()).log(Level.SEVERE, null, ex);
					LinkedHashMap<String, String> errors = new LinkedHashMap<>();
					errors.put("sqlError", "There is a problem with the database, please contact your Administrator");
					request.setAttribute("errors", errors);
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
					User toUser;

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
					
					if (messageContents.length() > 255) {
						errors.put("messageContents", "Message cannot be over 255 characters");
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
				url = "/profile.jsp";
				int userID = (request.getParameter("userID") != null) ? Integer.parseInt(request.getParameter("userID")) : -1;
				LinkedHashMap<String, String> errors = new LinkedHashMap<>();

				boolean isAdmin = loggedInUser.getUsername().equals("admin");
				boolean isSelf = (loggedInUser.getUserID() == userID);
				if (!(isAdmin || isSelf)) {
					errors.put("accessError", "You do not have permission to edit this user");
					request.setAttribute("errors", errors);
					url = "/index.jsp";
					break;
				}

				User targetUser;
				try {
					targetUser = ChatDB.selectUser(userID);
					if (targetUser == null) {
						errors.put("sqlError", "User not found");
						request.setAttribute("errors", errors);
						url = "/index.jsp";
						break;
					}
				} catch (NamingException | SQLException ex) {
					Logger.getLogger(Public.class.getName()).log(Level.SEVERE, null, ex);
					errors.put("sqlError", "There is a problem with the database, please contact your Administrator");
					request.setAttribute("errors", errors);
					url = "/index.jsp";
					break;
				}

				String newUsername = request.getParameter("newUsername");
				String newPassword = request.getParameter("newPassword");
				String newFirstName = request.getParameter("newFirstName");
				String newLastName = request.getParameter("newLastName");
				String newPhoneNumber = request.getParameter("newPhoneNumber");
				String currentPassword = request.getParameter("currentPassword");

				boolean isAnyFieldUpdated = false;

				if (newUsername != null && !newUsername.isBlank() && !newUsername.equalsIgnoreCase(targetUser.getUsername())) {
					isAnyFieldUpdated = true;
				}
				if (newFirstName != null && !newFirstName.isBlank() && !newFirstName.equalsIgnoreCase(targetUser.getFirstName())) {
					isAnyFieldUpdated = true;
				}
				if (newLastName != null && !newLastName.isBlank() && !newLastName.equalsIgnoreCase(targetUser.getLastName())) {
					isAnyFieldUpdated = true;
				}
				if (newPhoneNumber != null && !newPhoneNumber.isBlank() && !newPhoneNumber.equals(targetUser.getPhoneNumber())) {
					isAnyFieldUpdated = true;
				}
				if (newPassword != null && !newPassword.isBlank() && !PasswordEncryption.checkPassword(newPassword, targetUser.getPassword())) {
					isAnyFieldUpdated = true;
				}

				boolean currentPasswordMatches = false;

				if (currentPassword == null || currentPassword.isBlank()) {
					errors.put("currentPassword", "Current password is required to make changes");
				} else if (PasswordEncryption.checkPassword(currentPassword, loggedInUser.getPassword())) { //logged in user because admin or user can edit this
					currentPasswordMatches = true;
				} else {
					errors.put("currentPassword", "Current password does not match");
				}

				if (newUsername != null && !newUsername.isBlank()) {
					if (isAdmin && isSelf && !newUsername.equalsIgnoreCase("admin")) {
						errors.put("newUsername", "Admin user cannot change their username.");
						targetUser.setUsername(targetUser.getUsername());
					} else {
						String usernameValidationError = Validation.validateUsername(newUsername);
						if (!usernameValidationError.isBlank()) {
							errors.put("newUsername", usernameValidationError);
						} else if (!newUsername.equalsIgnoreCase(targetUser.getUsername())) {
							try {
								User existingUser = ChatDB.selectUserByUsername(newUsername.toLowerCase());
								if (existingUser != null && existingUser.getUserID() != targetUser.getUserID()) {
									errors.put("newUsername", "This username is already in use.");
								} else {
									targetUser.setUsername(newUsername.toLowerCase());
								}
							} catch (NamingException | SQLException ex) {
								Logger.getLogger(Public.class.getName()).log(Level.SEVERE, null, ex);
								errors.put("sqlError", "Database error occurred.");
								request.setAttribute("errors", errors);
								url = "/index.jsp";
							}
						}
					}
				} else {
					errors.put("newUsername", "Please enter a username");
				}

				String firstNameError = Validation.validateFirstName(newFirstName);
				if (firstNameError.isBlank()) {
					targetUser.setFirstName(newFirstName);
				} else {
					errors.put("newFirstName", firstNameError);
				}

				String lastNameError = Validation.validateLastName(newLastName);
				if (lastNameError.isBlank()) {
					targetUser.setLastName(newLastName);
				} else {
					errors.put("newLastName", lastNameError);
				}

				String phoneError = Validation.validatePhoneNumber(newPhoneNumber);
				if (Validation.validatePhoneNumber(newPhoneNumber).isBlank()) {
					if (newPhoneNumber != null) {
						newPhoneNumber = newPhoneNumber.replaceAll("[^0-9]", "");
					}
					targetUser.setPhoneNumber(newPhoneNumber);
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
								targetUser.setPassword(PasswordEncryption.hashPassword(newPassword));
							} else {
								errors.put("newPassword", passwordValidationError);
							}
						}
					} else {
						String passwordValidationError = Validation.validatePassword(newPassword);
						if (passwordValidationError.isBlank()) {
							targetUser.setPassword(PasswordEncryption.hashPassword(newPassword));
						} else {
							errors.put("newPassword", passwordValidationError);
						}
					}
				}

				if (errors.isEmpty() && (isAnyFieldUpdated || (newPassword != null && !newPassword.isBlank()))) {
					try {
						ChatDB.updateUser(targetUser);
						if (isSelf) {
							request.getSession().setAttribute("loggedInUser", targetUser);
						}
					} catch (NamingException | SQLException ex) {
						Logger.getLogger(Public.class.getName()).log(Level.SEVERE, null, ex);
						errors.put("sqlError", "Database error occurred.");
						request.setAttribute("errors", errors);
						url = "/index.jsp";
					}
				} else {
					request.setAttribute("errors", errors);
					request.setAttribute("newUsername", newUsername);
					request.setAttribute("newFirstName", newFirstName);
					request.setAttribute("newLastName", newLastName);
					request.setAttribute("newPhoneNumber", newPhoneNumber);
					url = "/editUser.jsp";
				}

				break;
			}
			case "deleteUser": {
				int userID = (request.getParameter("userID") != null) ? Integer.parseInt(request.getParameter("userID")) : -1;
				LinkedHashMap<String, String> errors = new LinkedHashMap<>();
				url = "/index.jsp";

				boolean isAdmin = loggedInUser.getUsername().equals("admin");
				boolean isSelf = (loggedInUser.getUserID() == userID);

				if (isAdmin) {
					url = "/Private?action=viewAllUsers";
				}

				if (!(isAdmin || isSelf)) {
					errors.put("accessError", "You do not have permission to delete this user");
				}

				if (isAdmin && isSelf) {
					errors.put("accessError", "Admin cannot delete themselves");
				}

				if (userID > 0) {
					if (errors.isEmpty() && ((isAdmin && !isSelf) || (!isAdmin && isSelf))) {
						try {
							LinkedHashMap<Integer, Message> fromMessages = ChatDB.selectAllMessagesFromUser(userID);
							LinkedHashMap<Integer, Message> toMessages = ChatDB.selectAllMessagesToUser(userID);

							fromMessages.forEach((id, message) -> {
								try {
									ChatDB.deleteMessage(id);
								} catch (NamingException | SQLException ex) {
									Logger.getLogger(Public.class.getName()).log(Level.SEVERE, null, ex);
								}
							});

							toMessages.forEach((id, message) -> {
								try {
									ChatDB.deleteMessage(id);
								} catch (NamingException | SQLException ex) {
									Logger.getLogger(Public.class.getName()).log(Level.SEVERE, null, ex);
								}
							});

							ChatDB.deleteUser(userID);

						} catch (NamingException | SQLException ex) {
							Logger.getLogger(Public.class.getName()).log(Level.SEVERE, null, ex);
							errors.put("sqlError", "There is a problem with the database, please contact your Administrator");
							request.setAttribute("errors", errors);
							url = "/index.jsp";
						}
					}
				}
				request.setAttribute("errors", errors);
				break;
			}
			case "viewAllUsers": {
				url = "/allUsers.jsp";
				try {
					request.setAttribute("users", ChatDB.selectAllUsers());
				} catch (NamingException | SQLException ex) {
					Logger.getLogger(Public.class.getName()).log(Level.SEVERE, null, ex);
					LinkedHashMap<String, String> errors = new LinkedHashMap<>();
					errors.put("sqlError", "There is a problem with the database, please contact your Administrator");
					request.setAttribute("errors", errors);
					url = "/index.jsp";
				}
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
