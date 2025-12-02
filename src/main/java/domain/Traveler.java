package domain;

import javax.persistence.*;

/**
 * Traveler (passenger) user type that extends AbstractUser.
 * Travelers can book rides but cannot create them.
 */
@Entity
@DiscriminatorValue("TRAVELER")
public class Traveler extends AbstractUser {

	private static final long serialVersionUID = 1L;
	
	public Traveler() {
		super();
	}
	
	public Traveler(String username, String password, String email) {
		super(username, password, email);
	}
	
	@Override
	public UserRole getRole() {
		return UserRole.TRAVELER;
	}
	
	@Override
	public String toString() {
		return "Traveler [username=" + getUsername() + ", email=" + getEmail() + "]";
	}
}
