package controllers;

import data.ChatDB;
import java.io.IOException;
import java.util.LinkedHashMap;
import javax.naming.NamingException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Message;
import models.User;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Private extends HttpServlet {

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = "/profile.jsp";
		User loggedInUser = (User) request.getSession().getAttribute("loggedInUser");

		if (loggedInUser == null) {
			response.sendRedirect("Public");
			return;
		}

		String action = request.getParameter("action");
		if (action == null) {
			action = "default";
		}

		switch (action) {
			case "logout": {
				HttpSession session = request.getSession(false);
				if (session != null) {
					session.invalidate();
				}
				response.sendRedirect("index.jsp");
				return;
			}
			case "gotoMessages": {
				url = "/messages.jsp";
				try {
					LinkedHashMap<String, Message> toUserMessages = ChatDB.selectAllMessagesToUserByUsername(loggedInUser.getUsername());
					LinkedHashMap<String, Message> fromUserMessages = ChatDB.selectAllMessagesFromUserByUsername(loggedInUser.getUsername());
					
					request.setAttribute("toUserMessages", toUserMessages);
					request.setAttribute("fromUserMessages", fromUserMessages);
				} catch (NamingException | SQLException ex) {
					Logger.getLogger(Public.class.getName()).log(Level.SEVERE, null, ex);
					url = "/Public";
				}
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
