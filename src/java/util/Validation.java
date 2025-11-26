/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import data.ChatDB;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

public class Validation {

    public static String validateUsername(String username) {
        String error = "";
        try {
            if (username == null || username.isEmpty())
                error = "Required field";
            else if (ChatDB.selectUserByUsername(username) != null)
                error = "Username taken.";
            else if (username.length() < 4 || username.length() > 30)
                error = "Username must be between 4 and 30 characters.";
		} catch (NamingException ex) {
            Logger.getLogger(Validation.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SQLException ex) {
            Logger.getLogger(Validation.class.getName()).log(Level.SEVERE, null, ex);
		}
        return error;
    }

    public static String validatePassword(String password) {
        String error = "";
        if (password == null || password.isEmpty())
            error = "Required field";
        else {
            if (password.length() < 8 || password.length() > 64)
                error += "- Password must be longer than 8 characters and less than 64,";
            if (!password.matches(".*[A-Z].*"))
                error += "- Password must contain at least one uppercase letter,";
            if (!password.matches(".*[a-z].*"))
                error += "- Password must containt at least one lowercase letter,";
            if (!password.matches(".*\\d.*"))
                error += "- Password must contain at least one number,";
            if (!password.matches(".*[!@#$%^&*()\\-+=<>?/{}~|].*"))
                error += "- Password must contain at least one special character,";
        }
        return error;
    }

	public static String validateFirstName(String firstName) {
		String error = "";
		if (firstName == null || firstName.isEmpty())
			error = "Required field";
		if (firstName.length() > 25)
			error = "First name is too long";
		return error;
	}

	public static String validateLastName(String lastName) {
		String error = "";
		if (lastName == null || lastName.isEmpty())
			error = "Required field";
		if (lastName.length() > 25)
			error = "First name is too long";
		return error;
	}
	
	public static String validatePhoneNumber(String phoneNumber) {
		String error = "";
		if (phoneNumber == null || phoneNumber.isEmpty())
			return error; // phone number is not required
		if (!phoneNumber.matches("^[(]?[0-9]{3}[)]?\\s*[-\\s\\.]?\\s*[0-9]{3}\\s*[-\\s\\.]?\\s*[0-9]{4}$"))
			error = "Phone number is not a valid phone number";
		return error;
	}

}