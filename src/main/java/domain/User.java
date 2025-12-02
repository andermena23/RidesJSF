package domain;

import java.io.Serializable;

/**
 * Interface that defines the common contract for all user types in the system.
 * All user implementations (Driver, Admin, Traveler) must implement this interface.
 */
public interface User extends Serializable {
	
	/**
	 * Gets the username
	 * @return the username
	 */
	String getUsername();
	
	/**
	 * Sets the username
	 * @param username the username to set
	 */
	void setUsername(String username);
	
	/**
	 * Gets the password
	 * @return the password
	 */
	String getPassword();
	
	/**
	 * Sets the password
	 * @param password the password to set
	 */
	void setPassword(String password);
	
	/**
	 * Gets the email
	 * @return the email
	 */
	String getEmail();
	
	/**
	 * Sets the email
	 * @param email the email to set
	 */
	void setEmail(String email);
	
	/**
	 * Gets the user role
	 * @return the role as a UserRole enum
	 */
	UserRole getRole();
}
