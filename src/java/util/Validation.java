/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import data.ChatDB;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import models.User;

public class Validation {

	public static String validateUsername(String username) {
		String error = "";
		if (username == null || username.isBlank()) {
			error = "Please enter a username";
		} else if (username.length() < 4 || username.length() > 30) {
			error = "Username must be between 4 and 30 characters";
		}
		return error;
	}

	public static String validatePassword(String password) {
		ArrayList<String> errors = new ArrayList<>();
		String errorMessage = "";
		if (password == null || password.isBlank()) {
			errorMessage = "Please enter a password";
		} else {
			if (password.length() < 8 || password.length() > 32) {
				errors.add("Password must be longer than 8 characters and less than 32");
			}
			if (!password.matches(".*[A-Z].*")) {
				errors.add("Password must contain at least one uppercase letter");
			}
			if (!password.matches(".*[a-z].*")) {
				errors.add("Password must containt at least one lowercase letter");
			}
			if (!password.matches(".*\\d.*")) {
				errors.add("Password must contain at least one number");
			}
			if (!password.matches(".*[!@#$%^&*()\\-+=<>?/{}~|].*")) {
				errors.add("Password must contain at least one special character");
			}
			if (!errors.isEmpty()) {
				errorMessage = String.join(", ", errors);
			}
		}
		return errorMessage;
	}

	public static String validateFirstName(String firstName) {
		String error = "";
		if (firstName == null || firstName.isBlank()) {
			error = "Please enter a first name";
		}
		if (firstName.length() > 25) {
			error = "First name is too long";
		}
		return error;
	}

	public static String validateLastName(String lastName) {
		String error = "";
		if (lastName == null || lastName.isBlank()) {
			error = "Please enter a last name";
		}
		if (lastName.length() > 25) {
			error = "First name is too long";
		}
		return error;
	}

	public static String validatePhoneNumber(String phoneNumber) {
		String error = "";
		if (phoneNumber == null || phoneNumber.isBlank()) {
			return error; // phone number is not required
		}
		if (!phoneNumber.matches("^[(]?[0-9]{3}[)]?\\s*[-\\s\\.]?\\s*[0-9]{3}\\s*[-\\s\\.]?\\s*[0-9]{4}$")) {
			error = "Phone number is not a valid phone number";
		}
		return error;
	}

	public static String validateMessageContents(String messageContents) {
		String error = "";
		if (messageContents == null || messageContents.isBlank()) {
			error = "Please enter a message with text";
		}
		return error;
	}

	public static String validateToAndFromUserIDs(int toUserID, int fromUserID) {
		String error = "";
		try {
			User toUser = ChatDB.selectUser(toUserID);
			if (toUser == null) {
				error = "This user does not exist in the database";
			} else if (toUser.getUserID() == fromUserID) {
				error = "You cannot message yourself";
			}
		} catch (NamingException | SQLException ex) {
			Logger.getLogger(Validation.class.getName()).log(Level.SEVERE, null, ex);
			error = "Error validating user";
		}
		return error;
	}
}
