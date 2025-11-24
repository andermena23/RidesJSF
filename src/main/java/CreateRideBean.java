import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import businessLogic.*;

@Named("createRide")
@SessionScoped
public class CreateRideBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@jakarta.inject.Inject
	@Named("businessLogic")
	private BLFacade facadeBL;
	
	private String driverEmail = "driver1@gmail.com";
	private String selectedFrom = "";
	private String selectedTo = "";
	private String customFromCity = "";
	private String customToCity = "";
	private Date selectedDate = new Date();
	private Integer nplaces = 0;
	private Double price = 0.0;
	
	private List<String> departingCities = new ArrayList<>();
	private List<String> destinationCities = new ArrayList<>();
	private String message = "";
	private boolean success = false;
	private boolean citiesLoaded = false;
	
	public CreateRideBean() {
		System.out.println("[DEBUG] CreateRideBean - Constructor called");
	}
	
	public String getDriverEmail() {
		return driverEmail;
	}
	
	public void setDriverEmail(String driverEmail) {
		this.driverEmail = driverEmail;
	}
	
	public String getSelectedFrom() {
		return selectedFrom;
	}
	
	public void setSelectedFrom(String selectedFrom) {
		this.selectedFrom = selectedFrom;
		// Clear custom field when changing selection
		if (!"Other".equals(selectedFrom)) {
			customFromCity = "";
		}
		// Reload destination cities excluding the selected departure
		loadDestinationCities();
		// Clear destination if it's the same as the new departure
		if (selectedFrom != null && selectedFrom.equals(selectedTo) && !"Other".equals(selectedFrom)) {
			selectedTo = "";
		}
	}
	
	public String getSelectedTo() {
		return selectedTo;
	}
	
	public void setSelectedTo(String selectedTo) {
		this.selectedTo = selectedTo;
		// Clear custom field when changing selection
		if (!"Other".equals(selectedTo)) {
			customToCity = "";
		}
		// Reload departure cities excluding the selected destination
		loadDepartingCities();
		// Clear departure if it's the same as the new destination
		if (selectedTo != null && selectedTo.equals(selectedFrom) && !"Other".equals(selectedTo)) {
			selectedFrom = "";
		}
	}
	
	public String getCustomFromCity() {
		return customFromCity;
	}
	
	public void setCustomFromCity(String customFromCity) {
		this.customFromCity = customFromCity;
	}
	
	public String getCustomToCity() {
		return customToCity;
	}
	
	public void setCustomToCity(String customToCity) {
		this.customToCity = customToCity;
	}
	
	public Date getSelectedDate() {
		return selectedDate;
	}
	
	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}
	
	public Integer getNplaces() {
		return nplaces;
	}
	
	public void setNplaces(Integer nplaces) {
		this.nplaces = nplaces;
	}
	
	public Double getPrice() {
		return price;
	}
	
	public void setPrice(Double price) {
		this.price = price;
	}
	
	public List<String> getDepartingCities() {
		System.out.println("[DEBUG] CreateRideBean - getDepartingCities() called, current size: " + departingCities.size());
		if (facadeBL != null && !citiesLoaded) {
			System.out.println("[DEBUG] CreateRideBean - Loading cities for the first time");
			loadAllCities();
			loadDepartingCities();
		} else if (facadeBL != null && citiesLoaded) {
			System.out.println("[DEBUG] CreateRideBean - Refreshing departing cities based on current selection");
			loadDepartingCities();
		}
		return departingCities;
	}
	
	public void setDepartingCities(List<String> departingCities) {
		this.departingCities = departingCities;
	}
	
	public List<String> getDestinationCities() {
		System.out.println("[DEBUG] CreateRideBean - getDestinationCities() called, current size: " + destinationCities.size());
		if (facadeBL != null && !citiesLoaded) {
			System.out.println("[DEBUG] CreateRideBean - Loading cities for the first time");
			loadAllCities();
			loadDestinationCities();
		} else if (facadeBL != null && citiesLoaded) {
			System.out.println("[DEBUG] CreateRideBean - Refreshing destination cities based on current selection");
			loadDestinationCities();
		}
		return destinationCities;
	}
	
	public void setDestinationCities(List<String> destinationCities) {
		this.destinationCities = destinationCities;
	}
	
	private List<String> allUniqueCities = new ArrayList<>();
	
	private void loadAllCities() {
		System.out.println("[DEBUG] CreateRideBean - loadAllCities() called - loading all unique cities");
		if (facadeBL != null) {
			try {
				// Get all unique cities from both departure and destination
				java.util.Set<String> allCitiesSet = new java.util.HashSet<>();
				List<String> departCities = facadeBL.getDepartCities();
				allCitiesSet.addAll(departCities);
				// Add cities from all destinations
				for (String city : departCities) {
					try {
						allCitiesSet.addAll(facadeBL.getDestinationCities(city));
					} catch (Exception ex) {
						// Ignore individual errors
					}
				}
				allUniqueCities = new ArrayList<>(allCitiesSet);
				java.util.Collections.sort(allUniqueCities);
				citiesLoaded = true;
				System.out.println("[DEBUG] CreateRideBean - Loaded and cached " + allUniqueCities.size() + " unique cities: " + allUniqueCities);
			} catch (Exception e) {
				System.err.println("[ERROR] CreateRideBean - Failed to load all cities: " + e.getMessage());
				e.printStackTrace();
				allUniqueCities = new ArrayList<>();
			}
		}
	}
	
	public String loadDepartingCities() {
		System.out.println("[DEBUG] CreateRideBean - loadDepartingCities() called");
		// Use cached cities and filter based on selection
		departingCities = new ArrayList<>(allUniqueCities);
		// Remove the selected destination city if it's not "Other"
		if (selectedTo != null && !selectedTo.isEmpty() && !"Other".equals(selectedTo)) {
			departingCities.remove(selectedTo);
		}
		// Add "Other" option at the end
		if (!departingCities.contains("Other")) {
			departingCities.add("Other");
		}
		System.out.println("[DEBUG] CreateRideBean - Filtered departing cities (" + departingCities.size() + " cities)");
		return "";
	}
	
	public String loadDestinationCities() {
		System.out.println("[DEBUG] CreateRideBean - loadDestinationCities() called");
		// Use cached cities and filter based on selection
		destinationCities = new ArrayList<>(allUniqueCities);
		// Remove the selected departure city if it's not "Other"
		if (selectedFrom != null && !selectedFrom.isEmpty() && !"Other".equals(selectedFrom)) {
			destinationCities.remove(selectedFrom);
		}
		// Add "Other" option at the end
		if (!destinationCities.contains("Other")) {
			destinationCities.add("Other");
		}
		System.out.println("[DEBUG] CreateRideBean - Filtered destination cities (" + destinationCities.size() + " cities)");
		return "";
	}
	
	public String createRide() {
		System.out.println("[DEBUG] CreateRideBean - createRide() called");
		System.out.println("[DEBUG] CreateRideBean - Driver: " + driverEmail + ", From: " + selectedFrom + 
				", To: " + selectedTo + ", Date: " + selectedDate + ", Places: " + nplaces + ", Price: " + price);
		
		// Reset message
		message = "";
		success = false;
		
		// Validation
		if (driverEmail == null || driverEmail.trim().isEmpty()) {
			message = "Driver email is required.";
			return "";
		}
		if (selectedFrom == null || selectedFrom.isEmpty()) {
			message = "Please select a departure city.";
			return "";
		}
		if ("Other".equals(selectedFrom) && (customFromCity == null || customFromCity.trim().isEmpty())) {
			message = "Please enter a custom departure city.";
			return "";
		}
		if (selectedTo == null || selectedTo.isEmpty()) {
			message = "Please select a destination city.";
			return "";
		}
		if ("Other".equals(selectedTo) && (customToCity == null || customToCity.trim().isEmpty())) {
			message = "Please enter a custom destination city.";
			return "";
		}
		if (selectedDate == null) {
			message = "Please select a date.";
			return "";
		}
		if (nplaces == null || nplaces <= 0) {
			message = "Number of places must be greater than 0.";
			return "";
		}
		if (price == null || price <= 0) {
			message = "Price must be greater than 0.";
			return "";
		}
		
		if (facadeBL != null) {
			try {
				// Determine actual city names (use custom if "Other" selected)
				String fromCity = "Other".equals(selectedFrom) ? customFromCity : selectedFrom;
				String toCity = "Other".equals(selectedTo) ? customToCity : selectedTo;
				
				// Call the business logic facade to create the ride
				facadeBL.createRide(fromCity, toCity, selectedDate, nplaces.intValue(), price.floatValue(), driverEmail);
				System.out.println("[DEBUG] CreateRideBean - Ride created successfully!");
				
				message = "Ride created successfully!";
				success = true;
				
				// Clear the form after successful creation
				driverEmail = "driver1@gmail.com";
				selectedFrom = "";
				selectedTo = "";
				customFromCity = "";
				customToCity = "";
				selectedDate = new Date();
				nplaces = 0;
				price = 0.0;
				destinationCities.clear();
				// Reset flag to reload cities with new data
				citiesLoaded = false;
				allUniqueCities.clear();
				
			} catch (Exception e) {
				System.err.println("[ERROR] CreateRideBean - Failed to create ride: " + e.getMessage());
				e.printStackTrace();
				message = "Error creating ride: " + e.getMessage();
			}
		} else {
			System.err.println("[ERROR] CreateRideBean - Business logic facade is not available");
			message = "System error: Business logic not available.";
		}
		
		return "";
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
}
