import java.io.Serializable;
import jakarta.enterprise.context.SessionScoped;
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
	
	public IndexBean() {
		System.out.println("[DEBUG] IndexBean - Constructor called");
	}
	
	public String createRide() {
		System.out.println("createRide");
		return "CreateRide";
	}
	
	public String queryRides() {
		System.out.println("queryRides");
		return "QueryRides";
	}
	
	public String getLanguage() {
		return language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}
}
