import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import businessLogic.*;

@Named("queryRides")
@SessionScoped
public class QueryRidesBean implements Serializable{

	static {
		System.out.println("[DEBUG] ==================== QueryRidesBean CLASS LOADED ====================");
	}

	@jakarta.inject.Inject
	@Named("businessLogic")
	private BLFacade facadeBL;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<String> destinationCities = new ArrayList<>();
	private List<String> departingCities = new ArrayList<>();
	private String selectedFrom = "";
	private String selectedTo = "";
	private Date selectedDate = new Date();
	private List<Date> availableDates = new ArrayList<>();
	
	public QueryRidesBean() {
		System.out.println("[DEBUG] QueryRidesBean - Constructor called");
		System.out.println("[DEBUG] QueryRidesBean - facadeBL at construction: " + (facadeBL != null ? "injected" : "NULL (will be injected after construction)"));
	}
	
	public String loadDestinations() {
		System.out.println("[DEBUG] QueryRidesBean - loadDestinations() called with selectedFrom: '" + selectedFrom + "'");
		System.out.println("[DEBUG] QueryRidesBean - facadeBL is " + (facadeBL != null ? "available" : "NULL"));
		destinationCities = facadeBL.getDestinationCities(selectedFrom);
		System.out.println("[DEBUG] QueryRidesBean - Loaded destinations (" + destinationCities.size() + " cities): " + destinationCities);
		return "";
	}

	public String loadDepartingCities() {
		System.out.println("[DEBUG] QueryRidesBean - loadDepartingCities() called");
		System.out.println("[DEBUG] QueryRidesBean - facadeBL is " + (facadeBL != null ? "available" : "NULL"));
		departingCities = facadeBL.getDepartCities();
		System.out.println("[DEBUG] QueryRidesBean - Loaded departing cities (" + departingCities.size() + " cities): " + departingCities);
		return "";
	}
	
	public List<String> getDestinationCities() {
		System.out.println("[DEBUG] QueryRidesBean - getDestinationCities() called, size: " + destinationCities.size());
		return destinationCities;
	}
	
	public void setDestinationCities(List<String> destinationCities) {
		System.out.println("[DEBUG] QueryRidesBean - setDestinationCities() called with size: " + (destinationCities != null ? destinationCities.size() : "NULL"));
		this.destinationCities = destinationCities;
	}
	
	public String getSelectedFrom() {
		System.out.println("[DEBUG] QueryRidesBean - getSelectedFrom() called, value: '" + selectedFrom + "'");
		return selectedFrom;
	}
	
	public void setSelectedFrom(String selectedFrom) {
		System.out.println("[DEBUG] QueryRidesBean - setSelectedFrom() called with: '" + selectedFrom + "'");
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
		System.out.println("[DEBUG] QueryRidesBean - getDepartingCities() called, current size: " + departingCities.size());
		if (departingCities.isEmpty()) {
			System.out.println("[DEBUG] QueryRidesBean - departingCities is empty, triggering loadDepartingCities()");
			loadDepartingCities();
		}
		return departingCities;
	}
	
	public void setDepartingCities(List<String> departingCities) {
		System.out.println("[DEBUG] QueryRidesBean - setDepartingCities() called with size: " + (departingCities != null ? departingCities.size() : "NULL"));
		this.departingCities = departingCities;
	}
	
	public String getRides(String from, String to, Date date) {
		System.out.println("[DEBUG] QueryRidesBean - getRides() called with from: '" + from + "', to: '" + to + "', date: " + date);
		System.out.println("[DEBUG] QueryRidesBean - facadeBL is " + (facadeBL != null ? "available" : "NULL"));
		// List<Ride> resultRides = facadeBL.getRides(from, to, date);
		// Process resultRides as needed
		System.out.println("[DEBUG] QueryRidesBean - getRides completed");
		return "";
	}
	
	public String getThisMonthDatesWithRides(String from, String to, Date date) {
		System.out.println("[DEBUG] QueryRidesBean - getThisMonthDatesWithRides() called with from: '" + from + "', to: '" + to + "', date: " + date);
		System.out.println("[DEBUG] QueryRidesBean - facadeBL is " + (facadeBL != null ? "available" : "NULL"));
		
		if (facadeBL != null && from != null && !from.isEmpty() && to != null && !to.isEmpty()) {
			List<Date> result = facadeBL.getThisMonthDatesWithRides(from, to, date);
			System.out.println("[DEBUG] QueryRidesBean - facadeBL returned: " + (result != null ? result.size() + " dates" : "NULL"));
			if (result != null) {
				availableDates = result;
				System.out.println("[DEBUG] QueryRidesBean - Loaded available dates (" + availableDates.size() + " dates): " + availableDates);
			} else {
				availableDates = new ArrayList<>();
				System.out.println("[DEBUG] QueryRidesBean - facadeBL returned null, initialized empty list");
			}
		} else {
			availableDates.clear();
			System.out.println("[DEBUG] QueryRidesBean - Cleared available dates (missing parameters)");
		}
		
		System.out.println("[DEBUG] QueryRidesBean - getThisMonthDatesWithRides completed");
		return "";
	}
	
	public String queryDestinationCities(String from){
		System.out.println("[DEBUG] QueryRidesBean - queryDestinationCities() called with from: '" + from + "'");
		System.out.println("[DEBUG] QueryRidesBean - facadeBL is " + (facadeBL != null ? "available" : "NULL"));
		// List<String> resultDestinations = facadeBL.getDestinationCities(from);
		// Process resultDestinations as needed
		System.out.println("[DEBUG] QueryRidesBean - queryDestinationCities completed");
		return "";
	}
	
	public String getSelectedTo() {
		System.out.println("[DEBUG] QueryRidesBean - getSelectedTo() called, value: '" + selectedTo + "'");
		return selectedTo;
	}
	
	public void setSelectedTo(String selectedTo) {
		System.out.println("[DEBUG] QueryRidesBean - setSelectedTo() called with: '" + selectedTo + "'");
		this.selectedTo = selectedTo;
		// Automatically load available dates when destination changes
		if (selectedTo != null && !selectedTo.isEmpty() && selectedFrom != null && !selectedFrom.isEmpty()) {
			loadAvailableDates();
		} else {
			availableDates.clear();
		}
	}
	
	public Date getSelectedDate() {
		System.out.println("[DEBUG] QueryRidesBean - getSelectedDate() called, value: " + selectedDate);
		return selectedDate;
	}
	
	public void setSelectedDate(Date selectedDate) {
		System.out.println("[DEBUG] QueryRidesBean - setSelectedDate() called with: " + selectedDate);
		this.selectedDate = selectedDate;
	}
	
	public void loadAvailableDates() {
		System.out.println("[DEBUG] QueryRidesBean - loadAvailableDates() wrapper called");
		// Delegate to the actual implementation method
		getThisMonthDatesWithRides(selectedFrom, selectedTo, selectedDate);
	}
	
	public void onDateSelect() {
		System.out.println("[DEBUG] QueryRidesBean - onDateSelect() called with date: " + selectedDate);
		System.out.println("[DEBUG] QueryRidesBean - Current availableDates size before reload: " + availableDates.size());
		// Reload available dates for the newly selected month
		if (selectedFrom != null && !selectedFrom.isEmpty() && selectedTo != null && !selectedTo.isEmpty()) {
			loadAvailableDates();
		} else {
			System.out.println("[DEBUG] QueryRidesBean - Cannot reload: from='" + selectedFrom + "', to='" + selectedTo + "'");
		}
	}
	
	public void onViewChange() {
		System.out.println("[DEBUG] QueryRidesBean - onViewChange() called with date: " + selectedDate);
		// viewChange event - reload dates for the newly displayed month
		if (selectedFrom != null && !selectedFrom.isEmpty() && selectedTo != null && !selectedTo.isEmpty()) {
			loadAvailableDates();
		}
	}
	
	public List<Date> getAvailableDates() {
		System.out.println("[DEBUG] QueryRidesBean - getAvailableDates() called, size: " + availableDates.size());
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
		System.out.println("[DEBUG] QueryRidesBean - getAvailableDatesJson() called, result: " + result);
		return result;
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
	
}
