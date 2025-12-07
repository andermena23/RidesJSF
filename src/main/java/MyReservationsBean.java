import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import businessLogic.BLFacade;
import domain.RideReservation;
import domain.Traveler;
import domain.User;

@Named("myReservations")
@ViewScoped
public class MyReservationsBean implements Serializable {

	static {
		System.out.println("[DEBUG] ==================== MyReservationsBean CLASS LOADED ====================");
	}

	@Inject
	@Named("businessLogic")
	private BLFacade facadeBL;
	
	@Inject
	@Named("login")
	private LoginBean loginBean;
	
	private static final long serialVersionUID = 1L;
	
	private List<RideReservation> reservations = new ArrayList<>();
	
	public MyReservationsBean() {
		System.out.println("[DEBUG] MyReservationsBean - Constructor called");
	}
	
	@PostConstruct
	public void init() {
		System.out.println("[DEBUG] MyReservationsBean - PostConstruct init called");
		checkAuthentication();
		loadReservations();
	}
	
	public void checkAuthentication() {
		if (!loginBean.isLoggedIn()) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("Login.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		// Check if the user is a traveler
		User user = loginBean.getCurrentUser();
		if (!(user instanceof Traveler)) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("Index.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void loadReservations() {
		if (loginBean.isLoggedIn() && loginBean.getCurrentUser() instanceof Traveler) {
			String username = loginBean.getCurrentUser().getUsername();
			reservations = facadeBL.getTravelerReservations(username);
			System.out.println("[DEBUG] MyReservationsBean - Loaded " + reservations.size() + " reservations for user: " + username);
		}
	}
	
	public String goToIndex() {
		return "Index?faces-redirect=true";
	}
	
	public double getTotalCost(RideReservation reservation) {
		return reservation.getRide().getPrice() * reservation.getSeatsReserved();
	}
	
	// Getters and Setters
	
	public List<RideReservation> getReservations() {
		return reservations;
	}
	
	public void setReservations(List<RideReservation> reservations) {
		this.reservations = reservations;
	}
}
