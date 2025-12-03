import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import businessLogic.*;
import domain.Ride;

@Named("removeRide")
@SessionScoped
public class RemoveRideBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@jakarta.inject.Inject
	@Named("businessLogic")
	private BLFacade facadeBL;
	
	private Date selectedDate = new Date();
	private List<Ride> rides = new ArrayList<>();
	private List<Ride> selectedRides = new ArrayList<>();
	
	private String message = "";
	private boolean success = false;
	
	@jakarta.inject.Inject
	private LoginBean login;
	
	public RemoveRideBean() {
		System.out.println("[DEBUG] RemoveRideBean - Constructor called");
	}
	
	@jakarta.annotation.PostConstruct
	public void init() {
		System.out.println("[DEBUG] RemoveRideBean - PostConstruct init() called");
		// Load rides for today's date by default
		if (selectedDate != null) {
			loadRides();
		}
	}
	
	public void checkAccess() throws java.io.IOException {
		System.out.println("[DEBUG] RemoveRideBean - Checking access");
		if (login == null || !login.isLoggedIn()) {
			System.out.println("[DEBUG] RemoveRideBean - User not logged in, redirecting to login");
			jakarta.faces.context.FacesContext.getCurrentInstance().getExternalContext().redirect("Login.xhtml");
			return;
		}
		
		String role = login.getCurrentUser().getRole().getRoleName();
		System.out.println("[DEBUG] RemoveRideBean - User role: " + role);
		
		if (!"driver".equals(role) && !"admin".equals(role)) {
			System.out.println("[DEBUG] RemoveRideBean - User is not authorized, redirecting to index");
			jakarta.faces.context.FacesContext.getCurrentInstance().getExternalContext().redirect("Index.xhtml");
		}
	}
	
	public Date getSelectedDate() {
		return selectedDate;
	}
	
	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}
	
	public List<Ride> getRides() {
		return rides;
	}
	
	public List<Ride> getSelectedRides() {
		return selectedRides;
	}
	
	public void setSelectedRides(List<Ride> selectedRides) {
		this.selectedRides = selectedRides;
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public void onDateSelect() {
		System.out.println("[DEBUG] RemoveRideBean - Date selected: " + selectedDate);
		loadRides();
	}
	
	public void loadRides() {
		System.out.println("[DEBUG] RemoveRideBean - Loading rides for date: " + selectedDate);
		message = "";
		success = false;
		rides.clear();
		selectedRides.clear();
		
		if (selectedDate == null) {
			return;
		}
		
		if (facadeBL != null) {
			try {
				String userEmail = login.getCurrentUser().getEmail();
				String role = login.getCurrentUser().getRole().getRoleName();
				
				// Get all rides
				List<Ride> allRides = facadeBL.getAllRides();
				System.out.println("[DEBUG] RemoveRideBean - getAllRides returned: " + (allRides != null ? allRides.size() : "null") + " rides");
				
				// Filter by date and user role
				System.out.println("[DEBUG] RemoveRideBean - Selected date for filtering: " + selectedDate);
				for (Ride ride : allRides) {
					// Check if ride date matches selected date (comparing only date, not time)
					if (isSameDay(ride.getDate(), selectedDate)) {
						// Admins can see all rides, drivers only see their own
						if ("admin".equals(role) || ride.getDriver().getEmail().equals(userEmail)) {
							rides.add(ride);
							System.out.println("[DEBUG]   Added ride: " + ride.getFrom() + " -> " + ride.getTo());
						}
					}
				}
				
				if (rides.isEmpty()) {
					message = "No rides found for the selected date.";
				}
			} catch (Exception e) {
				System.err.println("[ERROR] RemoveRideBean - Failed to load rides: " + e.getMessage());
				e.printStackTrace();
				message = "Error loading rides: " + e.getMessage();
			}
		}
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
	
	public List<Date> getAvailableDates() {
		if (facadeBL == null) {
			return new ArrayList<>();
		}
		
		try {
			String userEmail = login.getCurrentUser().getEmail();
			String role = login.getCurrentUser().getRole().getRoleName();
			
			List<Ride> allRides = facadeBL.getAllRides();
			List<Date> dates = new ArrayList<>();
			
			// Collect unique dates for rides the user can remove
			for (Ride ride : allRides) {
				if ("admin".equals(role) || ride.getDriver().getEmail().equals(userEmail)) {
					// Check if this date is already in the list
					boolean dateExists = false;
					for (Date d : dates) {
						if (isSameDay(d, ride.getDate())) {
							dateExists = true;
							break;
						}
					}
					if (!dateExists) {
						dates.add(ride.getDate());
					}
				}
			}
			
			return dates;
		} catch (Exception e) {
			System.err.println("[ERROR] RemoveRideBean - Failed to get available dates: " + e.getMessage());
			return new ArrayList<>();
		}
	}
	
	public String getAvailableDatesJson() {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		List<Date> dates = getAvailableDates();
		StringBuilder json = new StringBuilder("[");
		for (int i = 0; i < dates.size(); i++) {
			if (i > 0) json.append(",");
			json.append("\"").append(sdf.format(dates.get(i))).append("\"");
		}
		json.append("]");
		return json.toString();
	}
	
	public void setAvailableDatesJson(String json) {
		// This setter is required by JSF but we don't need to parse the JSON back
	}
	
	public String removeSelectedRides() {
		System.out.println("[DEBUG] RemoveRideBean - removeSelectedRides() called");
		message = "";
		success = false;
		
		if (selectedRides == null || selectedRides.isEmpty()) {
			message = "Please select at least one ride to remove.";
			return "";
		}
		
		if (facadeBL != null) {
			try {
				String userEmail = login.getCurrentUser().getEmail();
				String role = login.getCurrentUser().getRole().getRoleName();
				
				int successCount = 0;
				int failCount = 0;
				
				for (Ride ride : selectedRides) {
					String rideDriverEmail = ride.getDriver().getEmail();
					
					System.out.println("[DEBUG] RemoveRideBean - Attempting to remove ride: " + ride.getFrom() + " -> " + ride.getTo() + " on " + ride.getDate() + " by " + rideDriverEmail);
					
					// Admins can remove any ride, drivers can only remove their own
					if (!"admin".equals(role) && !rideDriverEmail.equals(userEmail)) {
						System.out.println("[DEBUG] RemoveRideBean - Permission denied: not admin and not owner");
						failCount++;
						continue;
					}
					
					boolean removed = facadeBL.removeRide(
						ride.getFrom(), 
						ride.getTo(), 
						ride.getDate(), 
						rideDriverEmail
					);
					
					System.out.println("[DEBUG] RemoveRideBean - Remove result: " + removed);
					
					if (removed) {
						successCount++;
					} else {
						failCount++;
					}
				}
				
				if (successCount > 0) {
					message = successCount + " ride(s) removed successfully!";
					if (failCount > 0) {
						message += " " + failCount + " ride(s) could not be removed.";
					}
					success = true;
					
					// Refresh the rides list
					loadRides();
					selectedRides.clear();
				} else {
					message = "Failed to remove any rides.";
				}
			} catch (Exception e) {
				System.err.println("[ERROR] RemoveRideBean - Failed to remove rides: " + e.getMessage());
				e.printStackTrace();
				message = "Error removing rides: " + e.getMessage();
			}
		} else {
			message = "System error: Business logic not available.";
		}
		
		return "";
	}
	
}
