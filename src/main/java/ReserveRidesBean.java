import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import businessLogic.*;
import domain.Ride;
import domain.RideReservation;
import domain.Traveler;
import domain.User;

@Named("reserveRides")
@ViewScoped
public class ReserveRidesBean implements Serializable {

	static {
		System.out.println("[DEBUG] ==================== ReserveRidesBean CLASS LOADED ====================");
	}

	@Inject
	@Named("businessLogic")
	private BLFacade facadeBL;
	
	@Inject
	@Named("login")
	private LoginBean loginBean;
	
	private static final long serialVersionUID = 1L;
	
	private List<String> destinationCities = new ArrayList<>();
	private List<String> departingCities = new ArrayList<>();
	private String selectedFrom = "";
	private String selectedTo = "";
	private Date selectedDate = new Date();
	private List<Date> availableDates = new ArrayList<>();
	private List<Ride> rides = new ArrayList<>();
	private Integer selectedRideId;
	private int seatsToReserve = 1;
	private String reservationMessage;
	private String reservationMessageType; // "success" or "error"
	private java.util.Map<Integer, Integer> seatsPerRide = new java.util.HashMap<>();
	
	public ReserveRidesBean() {
		System.out.println("[DEBUG] ReserveRidesBean - Constructor called");
		System.out.println("[DEBUG] ReserveRidesBean - facadeBL at construction: " + (facadeBL != null ? "injected" : "NULL (will be injected after construction)"));
	}
	
	@PostConstruct
	public void init() {
		System.out.println("[DEBUG] ReserveRidesBean - @PostConstruct init() called - clearing cache for fresh view");
		// Clear any cached data when view is initialized
		rides.clear();
		availableDates.clear();
		reservationMessage = null;
		seatsPerRide.clear();
		System.out.println("[DEBUG] ReserveRidesBean - Cache cleared, bean ready with fresh state");
	}
	
	public String loadDestinations() {
		System.out.println("[DEBUG] ReserveRidesBean - loadDestinations() called with selectedFrom: '" + selectedFrom + "'");
		System.out.println("[DEBUG] ReserveRidesBean - facadeBL is " + (facadeBL != null ? "available" : "NULL"));
		destinationCities = facadeBL.getDestinationCities(selectedFrom);
		System.out.println("[DEBUG] ReserveRidesBean - Loaded destinations (" + destinationCities.size() + " cities): " + destinationCities);
		return "";
	}

	public String loadDepartingCities() {
		System.out.println("[DEBUG] ReserveRidesBean - loadDepartingCities() called");
		System.out.println("[DEBUG] ReserveRidesBean - facadeBL is " + (facadeBL != null ? "available" : "NULL"));
		departingCities = facadeBL.getDepartCities();
		System.out.println("[DEBUG] ReserveRidesBean - Loaded departing cities (" + departingCities.size() + " cities): " + departingCities);
		return "";
	}
	
	public List<String> getDestinationCities() {
		System.out.println("[DEBUG] ReserveRidesBean - getDestinationCities() called, size: " + destinationCities.size());
		return destinationCities;
	}
	
	public void setDestinationCities(List<String> destinationCities) {
		System.out.println("[DEBUG] ReserveRidesBean - setDestinationCities() called with size: " + (destinationCities != null ? destinationCities.size() : "NULL"));
		this.destinationCities = destinationCities;
	}
	
	public String getSelectedFrom() {
		System.out.println("[DEBUG] ReserveRidesBean - getSelectedFrom() called, value: '" + selectedFrom + "'");
		return selectedFrom;
	}
	
	public void setSelectedFrom(String selectedFrom) {
		System.out.println("[DEBUG] ReserveRidesBean - setSelectedFrom() called with: '" + selectedFrom + "'");
		this.selectedFrom = selectedFrom;
		// Automatically load destinations when departure city changes
		if (selectedFrom != null && !selectedFrom.isEmpty()) {
			loadDestinations();
		} else {
			destinationCities.clear();
			selectedTo = "";
		}
	}
	
	public List<String> getDepartingCities() {
		System.out.println("[DEBUG] ReserveRidesBean - getDepartingCities() called, current size: " + departingCities.size());
		if (departingCities.isEmpty()) {
			System.out.println("[DEBUG] ReserveRidesBean - departingCities is empty, triggering loadDepartingCities()");
			loadDepartingCities();
		}
		return departingCities;
	}
	
	public void setDepartingCities(List<String> departingCities) {
		System.out.println("[DEBUG] ReserveRidesBean - setDepartingCities() called with size: " + (departingCities != null ? departingCities.size() : "NULL"));
		this.departingCities = departingCities;
	}
	
	public String getRides(String from, String to, Date date) {
		System.out.println("[DEBUG] ReserveRidesBean - getRides() called with from: '" + from + "', to: '" + to + "', date: " + date);
		System.out.println("[DEBUG] ReserveRidesBean - facadeBL is " + (facadeBL != null ? "available" : "NULL"));
		if (facadeBL != null && from != null && !from.isEmpty() && to != null && !to.isEmpty() && date != null) {
			rides = facadeBL.getRides(from, to, date);
			System.out.println("[DEBUG] ReserveRidesBean - Loaded rides: " + (rides != null ? rides.size() + " rides" : "NULL"));
		} else {
			rides = new ArrayList<>();
			System.out.println("[DEBUG] ReserveRidesBean - Cleared rides (missing parameters)");
		}
		System.out.println("[DEBUG] ReserveRidesBean - getRides completed");
		return "";
	}
	
	public String getThisMonthDatesWithRides(String from, String to, Date date) {
		System.out.println("[DEBUG] ReserveRidesBean - getThisMonthDatesWithRides() called with from: '" + from + "', to: '" + to + "', date: " + date);
		System.out.println("[DEBUG] ReserveRidesBean - facadeBL is " + (facadeBL != null ? "available" : "NULL"));
		
		if (facadeBL != null && from != null && !from.isEmpty() && to != null && !to.isEmpty()) {
			List<Date> result = facadeBL.getThisMonthDatesWithRides(from, to, date);
			System.out.println("[DEBUG] ReserveRidesBean - facadeBL returned: " + (result != null ? result.size() + " dates" : "NULL"));
			if (result != null) {
				availableDates = result;
				System.out.println("[DEBUG] ReserveRidesBean - Loaded available dates (" + availableDates.size() + " dates): " + availableDates);
			} else {
				availableDates = new ArrayList<>();
				System.out.println("[DEBUG] ReserveRidesBean - facadeBL returned null, initialized empty list");
			}
		} else {
			availableDates.clear();
			System.out.println("[DEBUG] ReserveRidesBean - Cleared available dates (missing parameters)");
		}
		
		System.out.println("[DEBUG] ReserveRidesBean - getThisMonthDatesWithRides completed");
		return "";
	}
	
	public String getSelectedTo() {
		System.out.println("[DEBUG] ReserveRidesBean - getSelectedTo() called, value: '" + selectedTo + "'");
		return selectedTo;
	}
	
	public void setSelectedTo(String selectedTo) {
		System.out.println("[DEBUG] ReserveRidesBean - setSelectedTo() called with: '" + selectedTo + "'");
		this.selectedTo = selectedTo;
		// Automatically load available dates when destination changes
		if (selectedTo != null && !selectedTo.isEmpty() && selectedFrom != null && !selectedFrom.isEmpty()) {
			loadAvailableDates();
		} else {
			availableDates.clear();
		}
	}
	
	public Date getSelectedDate() {
		System.out.println("[DEBUG] ReserveRidesBean - getSelectedDate() called, value: " + selectedDate);
		return selectedDate;
	}
	
	public void setSelectedDate(Date selectedDate) {
		System.out.println("[DEBUG] ReserveRidesBean - setSelectedDate() called with: " + selectedDate);
		this.selectedDate = selectedDate;
	}
	
	public void loadAvailableDates() {
		System.out.println("[DEBUG] ReserveRidesBean - loadAvailableDates() wrapper called");
		// Delegate to the actual implementation method
		getThisMonthDatesWithRides(selectedFrom, selectedTo, selectedDate);
	}
	
	public void onDateSelect() {
		System.out.println("[DEBUG] ReserveRidesBean - onDateSelect() called with date: " + selectedDate);
		System.out.println("[DEBUG] ReserveRidesBean - Current availableDates size before reload: " + availableDates.size());
		// Reload available dates for the newly selected month
		if (selectedFrom != null && !selectedFrom.isEmpty() && selectedTo != null && !selectedTo.isEmpty()) {
			loadAvailableDates();
			// Load rides for the selected date
			getRides(selectedFrom, selectedTo, selectedDate);
		} else {
			System.out.println("[DEBUG] ReserveRidesBean - Cannot reload: from='" + selectedFrom + "', to='" + selectedTo + "'");
			rides = new ArrayList<>();
		}
	}
	
	public void onViewChange() {
		System.out.println("[DEBUG] ReserveRidesBean - onViewChange() called with date: " + selectedDate);
		// viewChange event - reload dates for the newly displayed month
		if (selectedFrom != null && !selectedFrom.isEmpty() && selectedTo != null && !selectedTo.isEmpty()) {
			loadAvailableDates();
		}
	}
	
	public List<Date> getAvailableDates() {
		System.out.println("[DEBUG] ReserveRidesBean - getAvailableDates() called, size: " + availableDates.size());
		return availableDates;
	}
	
	public String getAvailableDatesJson() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder json = new StringBuilder("[");
		for (int i = 0; i < availableDates.size(); i++) {
			if (i > 0) json.append(",");
			json.append("\"").append(sdf.format(availableDates.get(i))).append("\"");
		}
		json.append("]");
		String result = json.toString();
		System.out.println("[DEBUG] ReserveRidesBean - getAvailableDatesJson() called, result: " + result);
		return result;
	}
	
	public void setAvailableDatesJson(String json) {
		// This setter is required by JSF but we don't need to parse the JSON back
		System.out.println("[DEBUG] ReserveRidesBean - setAvailableDatesJson() called (no-op)");
	}
	
	public boolean isDateWithRides(Date date) {
		if (availableDates == null || availableDates.isEmpty() || date == null) {
			return false;
		}
		// Check if the date matches any available date (ignoring time)
		for (Date availableDate : availableDates) {
			if (isSameDay(date, availableDate)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isSameDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return false;
		}
		java.util.Calendar cal1 = java.util.Calendar.getInstance();
		java.util.Calendar cal2 = java.util.Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
			   cal1.get(java.util.Calendar.MONTH) == cal2.get(java.util.Calendar.MONTH) &&
			   cal1.get(java.util.Calendar.DAY_OF_MONTH) == cal2.get(java.util.Calendar.DAY_OF_MONTH);
	}
	
	public List<Ride> getRides() {
		System.out.println("[DEBUG] ReserveRidesBean - getRides() getter called, size: " + (rides != null ? rides.size() : "NULL"));
		return rides;
	}
	
	public void setRides(List<Ride> rides) {
		System.out.println("[DEBUG] ReserveRidesBean - setRides() called with size: " + (rides != null ? rides.size() : "NULL"));
		this.rides = rides;
	}
	
	public Integer getSelectedRideId() {
		return selectedRideId;
	}
	
	public void setSelectedRideId(Integer selectedRideId) {
		this.selectedRideId = selectedRideId;
	}
	
	public int getSeatsToReserve() {
		return seatsToReserve;
	}
	
	public void setSeatsToReserve(int seatsToReserve) {
		this.seatsToReserve = seatsToReserve;
	}
	
	public String getReservationMessage() {
		return reservationMessage;
	}
	
	public void setReservationMessage(String reservationMessage) {
		this.reservationMessage = reservationMessage;
	}
	
	public String getReservationMessageType() {
		return reservationMessageType;
	}
	
	public void setReservationMessageType(String reservationMessageType) {
		this.reservationMessageType = reservationMessageType;
	}
	
	public java.util.Map<Integer, Integer> getSeatsPerRide() {
		return seatsPerRide;
	}
	
	public void setSeatsPerRide(java.util.Map<Integer, Integer> seatsPerRide) {
		this.seatsPerRide = seatsPerRide;
	}
	
	public String reserveRide(Integer rideId, int seats) {
		System.out.println("[DEBUG] ReserveRidesBean - reserveRide() called with rideId: " + rideId + ", seats: " + seats);
		
		// Check if user is logged in
		if (loginBean == null || !loginBean.isLoggedIn()) {
			reservationMessage = "You must be logged in to reserve a ride";
			reservationMessageType = "error";
			System.out.println("[DEBUG] ReserveRidesBean - User not logged in");
			return null;
		}
		
		User currentUser = loginBean.getCurrentUser();
		
		// Check if user is a traveler
		if (!(currentUser instanceof Traveler)) {
			reservationMessage = "Only travelers can reserve rides";
			reservationMessageType = "error";
			System.out.println("[DEBUG] ReserveRidesBean - User is not a traveler");
			return null;
		}
		
		// Validate seats
		if (seats <= 0) {
			reservationMessage = "Number of seats must be greater than 0";
			reservationMessageType = "error";
			System.out.println("[DEBUG] ReserveRidesBean - Invalid number of seats");
			return null;
		}
		
		// Find the ride to calculate cost
		Ride selectedRide = null;
		for (Ride ride : rides) {
			if (ride.getRideNumber().equals(rideId)) {
				selectedRide = ride;
				break;
			}
		}
		
		if (selectedRide == null) {
			reservationMessage = "Ride not found";
			reservationMessageType = "error";
			System.out.println("[DEBUG] ReserveRidesBean - Ride not found");
			return null;
		}
		
		// Check wallet balance
		double currentBalance = facadeBL.getWalletBalance(currentUser.getUsername());
		double totalCost = selectedRide.getPrice() * seats;
		
		if (currentBalance < totalCost) {
			reservationMessage = String.format("Insufficient funds! Cost: %.2f€, Your balance: %.2f€. Please deposit money to your wallet.", totalCost, currentBalance);
			reservationMessageType = "error";
			System.out.println("[DEBUG] ReserveRidesBean - Insufficient funds: balance=" + currentBalance + ", cost=" + totalCost);
			return null;
		}
		
		try {
			RideReservation reservation = facadeBL.reserveRide(rideId, currentUser.getUsername(), seats);
			
			if (reservation != null) {
				double newBalance = facadeBL.getWalletBalance(currentUser.getUsername());
				reservationMessage = String.format("Ride reserved successfully! Cost: %.2f€. New balance: %.2f€", totalCost, newBalance);
				reservationMessageType = "success";
				System.out.println("[DEBUG] ReserveRidesBean - Reservation successful");
				// Clear the seats input for this ride
				seatsPerRide.remove(rideId);
				// Reload rides from database to show updated availability
				if (facadeBL != null && selectedFrom != null && !selectedFrom.isEmpty() 
						&& selectedTo != null && !selectedTo.isEmpty() && selectedDate != null) {
					List<Ride> updatedRides = facadeBL.getRides(selectedFrom, selectedTo, selectedDate);
					System.out.println("[DEBUG] ReserveRidesBean - Reloaded rides after reservation: " 
							+ (updatedRides != null ? updatedRides.size() + " rides" : "NULL"));
					// Force update by clearing and re-adding
					rides.clear();
					if (updatedRides != null) {
						rides.addAll(updatedRides);
					}
				}
			} else {
				reservationMessage = "Failed to reserve ride. Please check seat availability or wallet balance.";
				reservationMessageType = "error";
				System.out.println("[DEBUG] ReserveRidesBean - Reservation failed");
			}
		} catch (Exception e) {
			reservationMessage = "An error occurred: " + e.getMessage();
			reservationMessageType = "error";
			System.err.println("[ERROR] ReserveRidesBean - Error reserving ride: " + e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
	
}
