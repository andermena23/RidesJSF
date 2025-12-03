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
		return "CreateRide?faces-redirect=true";
	}
	
	public String queryRides() {
		System.out.println("queryRides");
		return "QueryRides?faces-redirect=true";
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
		changeLocale(language);
	}
	
	public void changeLocale(String language) {
		FacesContext context = FacesContext.getCurrentInstance();
		java.util.Locale locale;
		
		switch (language) {
			case "eu":
				locale = java.util.Locale.forLanguageTag("eu");
				break;
			case "es":
				locale = java.util.Locale.forLanguageTag("es");
				break;
			case "en":
			default:
				locale = java.util.Locale.ENGLISH;
				break;
		}
		
		context.getViewRoot().setLocale(locale);
		System.out.println("[DEBUG] Changed locale to: " + locale);
	}
}
