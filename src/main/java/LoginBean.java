import java.io.Serializable;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import businessLogic.BLFacade;
import domain.User;

@Named("login")
@SessionScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	@Named("businessLogic")
	private BLFacade businessLogic;
	
	private String username;
	private String password;
	private String errorMessage;
	private User currentUser;
	
	public LoginBean() {
		System.out.println("[DEBUG] LoginBean - Constructor called");
	}
	
	public String login() {
		System.out.println("[DEBUG] Login attempt for user: " + username);
		
		if (username == null || username.trim().isEmpty() || 
		    password == null || password.trim().isEmpty()) {
			errorMessage = "Username and password are required";
			return null;
		}
		
		try {
			currentUser = businessLogic.authenticateUser(username, password);
			
			if (currentUser != null) {
				System.out.println("[DEBUG] Login successful for: " + username);
				errorMessage = null;
				return "Index?faces-redirect=true";
			} else {
				System.out.println("[DEBUG] Login failed - Invalid credentials");
				errorMessage = "Invalid username or password";
				return null;
			}
		} catch (Exception e) {
			System.err.println("[ERROR] Login error: " + e.getMessage());
			errorMessage = "An error occurred during login";
			return null;
		}
	}
	
	public String logout() {
		System.out.println("[DEBUG] Logout for user: " + (currentUser != null ? currentUser.getUsername() : "unknown"));
		currentUser = null;
		username = null;
		password = null;
		errorMessage = null;
		return "Login?faces-redirect=true";
	}
	
	public boolean isLoggedIn() {
		return currentUser != null;
	}
	
	// Getters and Setters
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public User getCurrentUser() {
		return currentUser;
	}
	
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	
	public double getWalletBalance() {
		if (currentUser != null && currentUser instanceof domain.Traveler) {
			// Fetch current balance from database instead of cached object
			String username = currentUser.getUsername();
			double balance = businessLogic.getWalletBalance(username);
			// Update cached user object too
			if (balance >= 0) {
				((domain.Traveler) currentUser).setWallet(balance);
				return balance;
			}
			// Fallback to cached value if DB fetch fails
			return ((domain.Traveler) currentUser).getWallet();
		}
		return 0.0;
	}
	
	public boolean isTraveler() {
		return currentUser != null && currentUser instanceof domain.Traveler;
	}
}
