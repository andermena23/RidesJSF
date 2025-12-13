import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;

import businessLogic.BLFacade;
import domain.Ride;
import domain.Driver;
import domain.User;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("driverSelectBean")
@SessionScoped
public class DriverSelectBean implements Serializable {

    @Inject
	@Named("businessLogic")
	private BLFacade facadeBL;

    private Driver selectedDriver;
    private String selectedDriverEmail;
    private List<Driver> drivers = new ArrayList<>();
    private List<Ride> rides = new ArrayList<>();

    @PostConstruct
    public void init() {
        loadDrivers();
    }

    private void loadDrivers() {
        drivers = facadeBL.getAllDrivers();
    }

    public List<Ride> getRides() {
		return rides;
	}
	
	public void setRides(List<Ride> rides) {
		this.rides = rides;
	}

    public Driver getSelectedDriver() {
        return selectedDriver;
    }

    public void setSelectedDriver(Driver selectedDriver) {
        this.selectedDriver = selectedDriver;
    }

    public String getSelectedDriverEmail() {
        return selectedDriverEmail;
    }

    public void setSelectedDriverEmail(String selectedDriverEmail) {
        this.selectedDriverEmail = selectedDriverEmail;
    }

    public String loadRidesByDriverEmail() {
        if (selectedDriverEmail != null && !selectedDriverEmail.isEmpty()) {
            rides = facadeBL.getRidesByDriver(selectedDriverEmail);
        }
        return "DriverSelectShow";
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<Driver> drivers) {
        this.drivers = drivers;
    }

}