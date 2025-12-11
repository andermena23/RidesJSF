import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import businessLogic.*;
import domain.Ride;

@Named("myRides")
@SessionScoped
public class MyRidesBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@jakarta.inject.Inject
	@Named("businessLogic")
	private BLFacade facadeBL;
	
	@jakarta.inject.Inject
	private LoginBean login;
	
	private List<Ride> driverRides = new ArrayList<>();
	private String message = "";
	
	public MyRidesBean() {
		System.out.println("[DEBUG] MyRidesBean - Constructor called");
	}
	
	public void checkAccess() throws java.io.IOException {
		System.out.println("[DEBUG] MyRidesBean - Checking access");
		if (login == null || !login.isLoggedIn()) {
			System.out.println("[DEBUG] MyRidesBean - User not logged in, redirecting to login");
			jakarta.faces.context.FacesContext.getCurrentInstance().getExternalContext().redirect("Login.xhtml");
			return;
		}
		
		String role = login.getCurrentUser().getRole().getRoleName();
		System.out.println("[DEBUG] MyRidesBean - User role: " + role);
		
		if (!"driver".equals(role)) {
			System.out.println("[DEBUG] MyRidesBean - User is not authorized (only drivers allowed), redirecting to index");
			jakarta.faces.context.FacesContext.getCurrentInstance().getExternalContext().redirect("Index.xhtml");
			return;
		}
		
		// Load the driver's rides
		loadMyRides();
	}
	
	public void loadMyRides() {
		System.out.println("[DEBUG] MyRidesBean - loadMyRides() called");
		driverRides.clear();
		message = "";
		
		if (facadeBL != null && login != null && login.isLoggedIn()) {
			try {
				String driverEmail = login.getCurrentUser().getEmail();
				System.out.println("[DEBUG] MyRidesBean - Loading rides for driver: " + driverEmail);
				
				driverRides = facadeBL.getRidesByDriver(driverEmail);
				
				if (driverRides == null) {
					driverRides = new ArrayList<>();
				}
				
				System.out.println("[DEBUG] MyRidesBean - Loaded " + driverRides.size() + " rides");
				
				if (driverRides.isEmpty()) {
					message = "No rides found.";
				}
			} catch (Exception e) {
				System.err.println("[ERROR] MyRidesBean - Failed to load rides: " + e.getMessage());
				e.printStackTrace();
				message = "Error loading rides: " + e.getMessage();
				driverRides = new ArrayList<>();
			}
		} else {
			System.err.println("[ERROR] MyRidesBean - Business logic facade or login not available");
			message = "System error: Unable to load rides.";
		}
	}
	
	public List<Ride> getDriverRides() {
		return driverRides;
	}
	
	public void setDriverRides(List<Ride> driverRides) {
		this.driverRides = driverRides;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean isHasRides() {
		return driverRides != null && !driverRides.isEmpty();
	}
}
