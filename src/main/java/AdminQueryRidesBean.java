import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import businessLogic.BLFacade;
import domain.Admin;
import domain.Ride;
import domain.RideReservation;
import domain.User;

@Named("adminQueryRides")
@ViewScoped
public class AdminQueryRidesBean implements Serializable {

	static {
		System.out.println("[DEBUG] ==================== AdminQueryRidesBean CLASS LOADED ====================");
	}

	@Inject
	@Named("businessLogic")
	private BLFacade facadeBL;
	
	@Inject
	@Named("login")
	private LoginBean loginBean;
	
	private static final long serialVersionUID = 1L;
	
	private List<Ride> rides = new ArrayList<>();
	private Map<Integer, List<RideReservation>> reservationsByRide = new HashMap<>();
	private Map<Integer, Boolean> expandedRides = new HashMap<>();
	
	public AdminQueryRidesBean() {
		System.out.println("[DEBUG] AdminQueryRidesBean - Constructor called");
	}
	
	@PostConstruct
	public void init() {
		System.out.println("[DEBUG] AdminQueryRidesBean - PostConstruct init called");
		checkAuthentication();
		loadRides();
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
		
		// Check if the user is an admin
		User user = loginBean.getCurrentUser();
		if (!(user instanceof Admin)) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("Index.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void loadRides() {
		System.out.println("[DEBUG] AdminQueryRidesBean - loadRides called");
		if (facadeBL != null) {
			rides = facadeBL.getAllRides();
			if (rides == null) {
				rides = new ArrayList<>();
			}
			System.out.println("[DEBUG] AdminQueryRidesBean - Loaded " + rides.size() + " rides");
		}
	}
	
	public void toggleReservations(Integer rideId) {
		System.out.println("[DEBUG] AdminQueryRidesBean - toggleReservations for ride: " + rideId);
		
		if (expandedRides.containsKey(rideId) && expandedRides.get(rideId)) {
			// Collapse
			expandedRides.put(rideId, false);
		} else {
			// Expand and load reservations if not already loaded
			expandedRides.put(rideId, true);
			if (!reservationsByRide.containsKey(rideId)) {
				loadReservationsForRide(rideId);
			}
		}
	}
	
	private void loadReservationsForRide(Integer rideId) {
		System.out.println("[DEBUG] AdminQueryRidesBean - loadReservationsForRide: " + rideId);
		List<RideReservation> reservations = facadeBL.getRideReservations(rideId);
		if (reservations == null) {
			reservations = new ArrayList<>();
		}
		reservationsByRide.put(rideId, reservations);
		System.out.println("[DEBUG] AdminQueryRidesBean - Loaded " + reservations.size() + " reservations for ride " + rideId);
	}
	
	public boolean isExpanded(Integer rideId) {
		return expandedRides.containsKey(rideId) && expandedRides.get(rideId);
	}
	
	public List<RideReservation> getReservationsForRide(Integer rideId) {
		if (!reservationsByRide.containsKey(rideId)) {
			return new ArrayList<>();
		}
		return reservationsByRide.get(rideId);
	}
	
	public int getReservationCount(Integer rideId) {
		List<RideReservation> reservations = getReservationsForRide(rideId);
		return reservations.size();
	}
	
	public int getTotalSeatsReserved(Integer rideId) {
		List<RideReservation> reservations = getReservationsForRide(rideId);
		int total = 0;
		for (RideReservation reservation : reservations) {
			total += reservation.getSeatsReserved();
		}
		return total;
	}
	
	public double getTotalRevenue(Integer rideId) {
		Ride ride = null;
		for (Ride r : rides) {
			if (r.getRideNumber().equals(rideId)) {
				ride = r;
				break;
			}
		}
		
		if (ride == null) {
			return 0.0;
		}
		
		int totalSeats = getTotalSeatsReserved(rideId);
		return ride.getPrice() * totalSeats;
	}
	
	public String goToIndex() {
		return "Index?faces-redirect=true";
	}
	
	// Getters and Setters
	
	public List<Ride> getRides() {
		return rides;
	}
	
	public void setRides(List<Ride> rides) {
		this.rides = rides;
	}
}
