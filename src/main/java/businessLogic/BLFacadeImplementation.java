package businessLogic;

import java.util.Date;
import java.util.List;

import jakarta.enterprise.inject.Vetoed;

import dataAccess.HibernateDataAccess;
import domain.Ride;
import domain.User;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.RideAlreadyExistException;

/**
 * It implements the business logic as a web service.
 */
// @WebService(endpointInterface = "businessLogic.BLFacade")
@Vetoed
public class BLFacadeImplementation implements BLFacade {
	private HibernateDataAccess dbManager;

	public BLFacadeImplementation() {
		System.out.println("Creating BLFacadeImplementation instance");
		dbManager = new HibernateDataAccess();
	}

	public BLFacadeImplementation(HibernateDataAccess da) {
		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
		dbManager = da;
	}

	/**
	 * {@inheritDoc}
	 */
	// @WebMethod
	public List<String> getDepartCities() {
		List<String> departLocations = null;
		try {
			dbManager.open();
			departLocations = dbManager.getDepartCities();
		} finally {
			dbManager.close();
		}
		return departLocations;
	}

	/**
	 * {@inheritDoc}
	 */
	// @WebMethod
	public List<String> getDestinationCities(String from) {
		List<String> targetCities = null;
		try {
			dbManager.open();
			targetCities = dbManager.getArrivalCities(from);
		} finally {
			dbManager.close();
		}
		return targetCities;
	}

	/**
	 * {@inheritDoc}
	 */
	// @WebMethod
	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail)
			throws RideMustBeLaterThanTodayException, RideAlreadyExistException {
		Ride ride = null;
		try {
			dbManager.open();
			ride = dbManager.createRide(from, to, date, nPlaces, price, driverEmail);
		} finally {
			dbManager.close();
		}
		return ride;
	}

	/**
	 * {@inheritDoc}
	 */
	// @WebMethod
	public List<Ride> getRides(String from, String to, Date date) {
		List<Ride> rides = null;
		try {
			dbManager.open();
			rides = dbManager.getRides(from, to, date);
		} finally {
			dbManager.close();
		}
		return rides;
	}

	/**
	 * {@inheritDoc}
	 */
	// @WebMethod
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		List<Date> dates = null;
		try {
			dbManager.open();
			dates = dbManager.getThisMonthDatesWithRides(from, to, date);
		} finally {
			dbManager.close();
		}
		return dates;
	}

	/**
	 * {@inheritDoc}
	 */
	// @WebMethod
	public void initializeBD() {
		try {
			dbManager.open();
			dbManager.initializeDB();
		} finally {
			dbManager.close();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	// @WebMethod
	public User authenticateUser(String username, String password) {
		User user = null;
		try {
			dbManager.open();
			user = dbManager.authenticateUser(username, password);
		} finally {
			dbManager.close();
		}
		return user;
	}
	
	/**
	 * {@inheritDoc}
	 */
	// @WebMethod
	public User registerUser(String username, String password, String email, String role) {
		return registerUser(username, password, email, role, null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	// @WebMethod
	public User registerUser(String username, String password, String email, String role, String name) {
		User user = null;
		try {
			dbManager.open();
			user = dbManager.registerUser(username, password, email, role, name);
		} finally {
			dbManager.close();
		}
		return user;
	}

}
