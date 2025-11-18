import java.io.Serializable;
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
		// List<Date> resultDates = facadeBL.getThisMonthDatesWithRides(from, to, date);
		// Process resultDates as needed
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
	
}
