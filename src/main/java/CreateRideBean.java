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
	
	private String driverName = "";
	private String selectedFrom = "";
	private String selectedTo = "";
	private Date selectedDate = new Date();
	private Integer nplaces = 0;
	private Double price = 0.0;
	
	private List<String> departingCities = new ArrayList<>();
	private List<String> destinationCities = new ArrayList<>();
	
	public CreateRideBean() {
		System.out.println("[DEBUG] CreateRideBean - Constructor called");
	}
	
	public String getDriverName() {
		return driverName;
	}
	
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	
	public String getSelectedFrom() {
		return selectedFrom;
	}
	
	public void setSelectedFrom(String selectedFrom) {
		this.selectedFrom = selectedFrom;
		// Automatically load destinations when departure city changes
		if (selectedFrom != null && !selectedFrom.isEmpty()) {
			loadDestinations();
		} else {
			destinationCities.clear();
			selectedTo = "";
		}
	}
	
	public String getSelectedTo() {
		return selectedTo;
	}
	
	public void setSelectedTo(String selectedTo) {
		this.selectedTo = selectedTo;
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
		if (departingCities.isEmpty() && facadeBL != null) {
			System.out.println("[DEBUG] CreateRideBean - departingCities is empty, triggering loadDepartingCities()");
			loadDepartingCities();
		}
		return departingCities;
	}
	
	public void setDepartingCities(List<String> departingCities) {
		this.departingCities = departingCities;
	}
	
	public List<String> getDestinationCities() {
		return destinationCities;
	}
	
	public void setDestinationCities(List<String> destinationCities) {
		this.destinationCities = destinationCities;
	}
	
	public String loadDepartingCities() {
		System.out.println("[DEBUG] CreateRideBean - loadDepartingCities() called");
		System.out.println("[DEBUG] CreateRideBean - facadeBL is " + (facadeBL != null ? "available" : "NULL"));
		if (facadeBL != null) {
			try {
				departingCities = facadeBL.getDepartCities();
				System.out.println("[DEBUG] CreateRideBean - Loaded departing cities (" + departingCities.size() + " cities): " + departingCities);
			} catch (Exception e) {
				System.err.println("[ERROR] CreateRideBean - Failed to load departing cities: " + e.getMessage());
				departingCities = new ArrayList<>();
			}
		} else {
			System.out.println("[DEBUG] CreateRideBean - facadeBL is NULL, cannot load cities");
			departingCities = new ArrayList<>();
		}
		return "";
	}
	
	public String loadDestinations() {
		System.out.println("[DEBUG] CreateRideBean - loadDestinations() called with selectedFrom: '" + selectedFrom + "'");
		System.out.println("[DEBUG] CreateRideBean - facadeBL is " + (facadeBL != null ? "available" : "NULL"));
		if (facadeBL != null) {
			try {
				destinationCities = facadeBL.getDestinationCities(selectedFrom);
				System.out.println("[DEBUG] CreateRideBean - Loaded destinations (" + destinationCities.size() + " cities): " + destinationCities);
			} catch (Exception e) {
				System.err.println("[ERROR] CreateRideBean - Failed to load destinations: " + e.getMessage());
				destinationCities = new ArrayList<>();
			}
		} else {
			System.out.println("[DEBUG] CreateRideBean - facadeBL is NULL, cannot load destinations");
			destinationCities = new ArrayList<>();
		}
		return "";
	}
	
	public String createRide() {
		System.out.println("[DEBUG] CreateRideBean - createRide() called");
		System.out.println("[DEBUG] CreateRideBean - Driver: " + driverName + ", From: " + selectedFrom + 
				", To: " + selectedTo + ", Date: " + selectedDate + ", Places: " + nplaces + ", Price: " + price);
		// TODO: Implement ride creation logic using facadeBL
		return "";
	}
}
