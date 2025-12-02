import java.io.Serializable;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import businessLogic.BLFacade;
import domain.User;

@Named("register")
@RequestScoped
public class RegisterBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	@Named("businessLogic")
	private BLFacade businessLogic;
	
	private String username;
	private String password;
	private String confirmPassword;
	private String email;
	private String role = "traveler"; // Default role
	private String name; // For drivers
	private String errorMessage;
	private String successMessage;
	
	public RegisterBean() {
		System.out.println("[DEBUG] RegisterBean - Constructor called");
	}
	
	public String register() {
		System.out.println("[DEBUG] Register attempt for user: " + username + ", role: " + role);
		
		// Reset messages
		errorMessage = null;
		successMessage = null;
		
		// Validation
		if (username == null || username.trim().isEmpty()) {
			errorMessage = "Username is required";
			return null;
		}
		
		if (password == null || password.trim().isEmpty()) {
			errorMessage = "Password is required";
			return null;
		}
		
		if (confirmPassword == null || !password.equals(confirmPassword)) {
			errorMessage = "Passwords do not match";
			return null;
		}
		
		if (email == null || email.trim().isEmpty()) {
			errorMessage = "Email is required";
			return null;
		}
		
		if (!email.contains("@")) {
			errorMessage = "Invalid email format";
			return null;
		}
		
		if (role == null || role.trim().isEmpty()) {
			errorMessage = "Role is required";
			return null;
		}
		
		if ("driver".equals(role) && (name == null || name.trim().isEmpty())) {
			errorMessage = "Name is required for drivers";
			return null;
		}
		
		try {
			User user = businessLogic.registerUser(username, password, email, role, name);
			
			if (user != null) {
				System.out.println("[DEBUG] Registration successful for: " + username);
				successMessage = "Registration successful! Please login.";
				
				// Clear form
				username = null;
				password = null;
				confirmPassword = null;
				email = null;
				name = null;
				role = "traveler";
				
				// Redirect to login page after successful registration
				return "Login?faces-redirect=true";
			} else {
				System.out.println("[DEBUG] Registration failed - User may already exist");
				errorMessage = "Registration failed. Username or email may already exist.";
				return null;
			}
		} catch (Exception e) {
			System.err.println("[ERROR] Registration error: " + e.getMessage());
			e.printStackTrace();
			errorMessage = "An error occurred during registration";
			return null;
		}
	}
	
	public String cancel() {
		return "Login?faces-redirect=true";
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
	
	public String getConfirmPassword() {
		return confirmPassword;
	}
	
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getSuccessMessage() {
		return successMessage;
	}
	
	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}
}
