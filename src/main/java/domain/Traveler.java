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
	
	private double wallet = 0.0;
	
	public Traveler() {
		super();
	}
	
	public Traveler(String username, String password, String email) {
		super(username, password, email);
		this.wallet = 0.0;
	}
	
	public double getWallet() {
		return wallet;
	}
	
	public void setWallet(double wallet) {
		this.wallet = wallet;
	}
	
	public boolean deposit(double amount) {
		if (amount > 0) {
			this.wallet += amount;
			return true;
		}
		return false;
	}
	
	public boolean withdraw(double amount) {
		if (amount > 0 && this.wallet >= amount) {
			this.wallet -= amount;
			return true;
		}
		return false;
	}
	
	@Override
	public UserRole getRole() {
		return UserRole.TRAVELER;
	}
	
	@Override
	public String toString() {
		return "Traveler [username=" + getUsername() + ", email=" + getEmail() + ", wallet=" + wallet + "]";
	}
}
