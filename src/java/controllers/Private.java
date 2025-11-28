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
		}

		getServletContext()
				.getRequestDispatcher(url).forward(request, response);
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
