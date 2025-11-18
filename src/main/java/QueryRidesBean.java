import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import businessLogic.*;
import dataAccess.DataAccess;
import domain.Ride;

@Named("queryRides")
@SessionScoped
public class QueryRidesBean implements Serializable{

	private BLFacade facadeBL;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<String> destinationCities = new ArrayList<>();
	private List<String> departingCities = new ArrayList<>();
	private String selectedFrom = "";
	
	public QueryRidesBean() {
		
	}
	
	@PostConstruct
	public void init() {
		getFacade().initializeBD();
		System.out.println("Database initialized");
	}
	
	private BLFacade getFacade() {
		if (facadeBL == null) {
			facadeBL = new BLFacadeImplementation(new DataAccess());
		}
		return facadeBL;
	}
	
	public void loadDestinations() {
		destinationCities = getFacade().getDestinationCities(selectedFrom);
		System.out.println("Loaded destinations: " + destinationCities);
	}

	public void loadDepartingCities() {
		departingCities = getFacade().getDepartCities();
		System.out.println("Loaded departing cities: " + departingCities);
	}
	
	public List<String> getDestinationCities() {
		return destinationCities;
	}
	
	public void setDestinationCities(List<String> destinationCities) {
		this.destinationCities = destinationCities;
	}
	
	public String getSelectedFrom() {
		return selectedFrom;
	}
	
	public void setSelectedFrom(String selectedFrom) {
		this.selectedFrom = selectedFrom;
	}
	
	public List<String> getDepartingCities() {
		if (departingCities.isEmpty()) {
			loadDepartingCities();
		}
		return departingCities;
	}
	
	public void setDepartingCities(List<String> departingCities) {
		this.departingCities = departingCities;
	}
	
	public String getRides(String from, String to, Date date) {
		List<Ride> resultRides = getFacade().getRides(from, to, date);
		System.out.println(resultRides);
		return "";
	}
	
	public String getThisMonthDatesWithRides(String from, String to, Date date) {
		List<Date> resultDates = getFacade().getThisMonthDatesWithRides(from, to, date);
		System.out.println(resultDates);
		return "";
	}
	
	public String queryDestinationCities(String from){
		List<String> resultDestinations = getFacade().getDestinationCities(from);
		System.out.println(resultDestinations);
		return "";
	}
	
}
