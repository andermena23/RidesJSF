package domain;

import javax.persistence.*;

/**
 * Admin user type that extends AbstractUser.
 * Admins have special privileges for managing the system.
 */
@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends AbstractUser {

	private static final long serialVersionUID = 1L;
	
	public Admin() {
		super();
	}
	
	public Admin(String username, String password, String email) {
		super(username, password, email);
	}
	
	@Override
	public UserRole getRole() {
		return UserRole.ADMIN;
	}
	
	@Override
	public String toString() {
		return "Admin [username=" + getUsername() + ", email=" + getEmail() + "]";
	}
}
