import java.io.IOException;
import java.io.Serializable;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("index")
@SessionScoped
public class IndexBean implements Serializable {

	static {
		System.out.println("[DEBUG] ==================== IndexBean CLASS LOADED ====================");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String language = "en";
	
	@Inject
	private LoginBean login;
	
	public IndexBean() {
		System.out.println("[DEBUG] IndexBean - Constructor called");
	}
	
	public void checkAuthentication() throws IOException {
		if (!login.isLoggedIn()) {
			FacesContext.getCurrentInstance().getExternalContext().redirect("Login.xhtml");
		}
	}
	
	public String createRide() {
		System.out.println("createRide");
		// Only drivers and admins can create rides
		if (!canCreateRide()) {
			System.out.println("[DEBUG] User " + login.getCurrentUser().getUsername() + " cannot create rides");
			return null; // Stay on same page
		}
		return "CreateRide";
	}
	
	public String queryRides() {
		System.out.println("queryRides");
		return "QueryRides";
	}
	
	public boolean canCreateRide() {
		if (!login.isLoggedIn()) {
			return false;
		}
		String role = login.getCurrentUser().getRole().getRoleName();
		return "driver".equals(role) || "admin".equals(role);
	}
	
	public boolean isAdmin() {
		if (!login.isLoggedIn()) {
			return false;
		}
		return "admin".equals(login.getCurrentUser().getRole().getRoleName());
	}
	
	public boolean isDriver() {
		if (!login.isLoggedIn()) {
			return false;
		}
		return "driver".equals(login.getCurrentUser().getRole().getRoleName());
	}
	
	public boolean isTraveler() {
		if (!login.isLoggedIn()) {
			return false;
		}
		return "traveler".equals(login.getCurrentUser().getRole().getRoleName());
	}
	
	public String getLanguage() {
		return language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}
}
