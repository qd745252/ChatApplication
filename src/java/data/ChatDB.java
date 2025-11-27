/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import java.sql.*;
import java.util.ArrayList;
import javax.naming.NamingException;
import models.Message;
import models.User;

public class ChatDB {

	public static int insertUser(User user) throws NamingException, SQLException {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;

		String query
				= "INSERT INTO users (username, password, first_name, last_name, phone_number) "
				+ "VALUES (?, ?, ?, ?, ?)";

		ps = connection.prepareStatement(query);
		ps.setString(1, user.getUsername());
		ps.setString(2, user.getPassword());
		ps.setString(3, user.getFirstName());
		ps.setString(4, user.getLastName());
		ps.setString(5, user.getPhoneNumber());

		int rows = ps.executeUpdate();

		ps.close();
		pool.freeConnection(connection);

		return rows;

	}

	public static User selectUser(int userID) throws NamingException, SQLException {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user = null;

		String query = "SELECT * FROM users "
				+ "WHERE user_id = ?";

		ps = connection.prepareStatement(query);
		ps.setInt(1, userID);
		rs = ps.executeQuery();

		if (rs.next()) {
			user = new User();
			user.setUserID(rs.getInt("user_id"));
			user.setUsername(rs.getString("username"));
			user.setPassword(rs.getString("password"));
			user.setFirstName(rs.getString("first_name"));
			user.setLastName(rs.getString("last_name"));
			user.setPhoneNumber(rs.getString("phone_number"));
		}

		rs.close();
		ps.close();
		pool.freeConnection(connection);

		return user;
	}

	public static User selectUserByUsername(String username) throws NamingException, SQLException {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user = null;

		String query = "SELECT * FROM users "
				+ "WHERE username = ?";

		ps = connection.prepareStatement(query);
		ps.setString(1, username);
		rs = ps.executeQuery();

		if (rs.next()) {
			user = new User();
			user.setUserID(rs.getInt("user_id"));
			user.setUsername(rs.getString("username"));
			user.setPassword(rs.getString("password"));
			user.setFirstName(rs.getString("first_name"));
			user.setLastName(rs.getString("last_name"));
			user.setPhoneNumber(rs.getString("phone_number"));
		}

		rs.close();
		ps.close();
		pool.freeConnection(connection);

		return user;
	}

	public static User updateUser(User user) throws NamingException, SQLException {
		User dbUser = selectUser(user.getUserID());
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		String query = "";

		if (dbUser.getPassword().equals(user.getPassword())) {
			query = "UPDATE users "
					+ "SET first_name = ?, "
					+ "last_name = ?, "
					+ "phone_number = ? "
					+ "WHERE username = ?";
			ps = connection.prepareStatement(query);
			ps.setString(1, user.getFirstName());
			ps.setString(2, user.getLastName());
			ps.setString(3, user.getPhoneNumber());
			ps.setString(4, user.getUsername());
		} else {
			query = "UPDATE users "
					+ "SET password = ?, "
					+ "first_name = ?, "
					+ "last_name = ?, "
					+ "phone_number = ? "
					+ "WHERE username = ?";
			ps.setString(1, user.getPassword());
			ps.setString(2, user.getFirstName());
			ps.setString(3, user.getLastName());
			ps.setString(4, user.getPhoneNumber());
			ps.setString(5, user.getUsername());
		}

		ps.executeUpdate();

		ps.close();
		pool.freeConnection(connection);
		return user;
	}

	public static String getPasswordForUsername(String username) throws NamingException, SQLException {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT password FROM users "
				+ "WHERE username = ?";

		ps = connection.prepareStatement(query);
		ps.setString(1, username);
		rs = ps.executeQuery();
		String password = null;
		if (rs.next()) {
			password = rs.getString("password");
		}

		rs.close();
		ps.close();
		pool.freeConnection(connection);

		return password;
	}

	public static ArrayList<User> selectAllUsers() throws NamingException, SQLException {
		ArrayList<User> userList = new ArrayList<>();
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT * FROM users";

		ps = connection.prepareStatement(query);
		rs = ps.executeQuery();

		while (rs.next()) {
			User user = new User();
			user.setUserID(rs.getInt("user_id"));
			user.setUsername(rs.getString("username"));
			user.setPassword(rs.getString("password"));
			user.setFirstName(rs.getString("first_name"));
			user.setLastName(rs.getString("last_name"));
			user.setPhoneNumber(rs.getString("phone_number"));
			userList.add(user);
		}

		rs.close();
		ps.close();
		pool.freeConnection(connection);
		return userList;
	}

	public static int insertMessage(Message message) throws NamingException, SQLException {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;

		String query
				= "INSERT INTO messages(message_contents, to_user_id, from_user_id) "
				+ "VALUES (?, ?, ?)";

		ps = connection.prepareStatement(query);
		ps.setString(1, message.getMessageContents());
		ps.setInt(2, message.getToUserID());
		ps.setInt(3, message.getFromUserID());

		int rows = ps.executeUpdate();

		ps.close();
		pool.freeConnection(connection);

		return rows;
	}

	public static Message selectMessage(int messageID) throws NamingException, SQLException {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Message message = null;

		String query = "SELECT * FROM messages"
				+ "WHERE message_id = ?";

		ps = connection.prepareStatement(query);
		ps.setInt(1, messageID);
		rs = ps.executeQuery();

		if (rs.next()) {
			message = new Message();
			message.setMessageID(rs.getInt("message_id"));
			message.setMessageContents(rs.getString("message_contents"));
			message.setToUserID(rs.getInt("to_user_id"));
			message.setFromUserID(rs.getInt("from_user_id"));
		}

		rs.close();
		ps.close();
		pool.freeConnection(connection);

		return message;
	}

	public static Message selectAllMessagesFromUser(int userID) throws NamingException, SQLException {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Message message = null;

		String query = "SELECT * FROM messages"
				+ "WHERE from_user_id = ?";

		ps = connection.prepareStatement(query);
		ps.setInt(1, userID);
		rs = ps.executeQuery();

		while (rs.next()) {
			message = new Message();
			message.setMessageID(rs.getInt("message_id"));
			message.setMessageContents(rs.getString("message_contents"));
			message.setToUserID(rs.getInt("to_user_id"));
			message.setFromUserID(rs.getInt("from_user_id"));
		}

		rs.close();
		ps.close();
		pool.freeConnection(connection);

		return message;
	}

	public static Message selectAllMessagesFromUserByUsername(String username) throws NamingException, SQLException {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Message message = null;

		String query = "SELECT * FROM messages"
				+ "WHERE from_user_id = ?";

		ps = connection.prepareStatement(query);
		User user = ChatDB.selectUserByUsername(username);
		ps.setInt(1, user.getUserID());
		rs = ps.executeQuery();

		while (rs.next()) {
			message = new Message();
			message.setMessageID(rs.getInt("message_id"));
			message.setMessageContents(rs.getString("message_contents"));
			message.setToUserID(rs.getInt("to_user_id"));
			message.setFromUserID(rs.getInt("from_user_id"));
		}

		rs.close();
		ps.close();
		pool.freeConnection(connection);

		return message;
	}

		public static Message updateMessage(Message message, User user) throws NamingException, SQLException {
		User dbUser = selectUser(message.getFromUserID()); // The user parameter on this method indicates that the update method may be being called from another user, in my case admin
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		String query = "";

		if (dbUser == null || user.getUsername().equals("admin")) {
			query = "UPDATE messages"
					+ "SET message_contents = ?, "
					+ "to_user_id = ?,"
					+ "WHERE from_user_id = ?";
			ps = connection.prepareStatement(query);
			ps.setString(1, message.getMessageContents());
			ps.setInt(2, message.getToUserID());
			ps.setInt(3, message.getFromUserID());
		}

		ps.executeUpdate();

		ps.close();
		pool.freeConnection(connection);
		return message;
	}
}
