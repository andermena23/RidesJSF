package businessLogic;

import java.util.Date;
import java.util.List;

import domain.Ride;
import domain.User;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.RideAlreadyExistException;

//import javax.jws.WebMethod;
//import javax.jws.WebService;

/**
 * Interface that specifies the business logic.
 */
// @WebService
public interface BLFacade {

	/**
	 * This method authenticates a user
	 * 
	 * @param username the username
	 * @param password the password
	 * @return the authenticated user or null if authentication fails
	 */
	// @WebMethod
	public User authenticateUser(String username, String password);
	
	/**
	 * This method registers a new user
	 * 
	 * @param username the username
	 * @param password the password
	 * @param email the email
	 * @param role the role (driver, admin, or traveler)
	 * @return the created user
	 */
	// @WebMethod
	public User registerUser(String username, String password, String email, String role);
	
	/**
	 * This method registers a new user with additional name parameter
	 * 
	 * @param username the username
	 * @param password the password
	 * @param email the email
	 * @param role the role (driver, admin, or traveler)
	 * @param name the name (used for drivers)
	 * @return the created user
	 */
	// @WebMethod
	public User registerUser(String username, String password, String email, String role, String name);

	/**
	 * This method returns all the cities where rides depart
	 * 
	 * @return collection of cities
	 */
	// @WebMethod
	public List<String> getDepartCities();

	/**
	 * This method returns all the arrival destinations, from all rides that depart
	 * from a given city
	 * 
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */
	// @WebMethod
	public List<String> getDestinationCities(String from);

	/**
	 * This method creates a ride for a driver
	 * 
	 * @param from    the origin location of a ride
	 * @param to      the destination location of a ride
	 * @param date    the date of the ride
	 * @param nPlaces available seats
	 * @param driver  to which ride is added
	 * 
	 * @return the created ride, or null, or an exception
	 * @throws RideMustBeLaterThanTodayException if the ride date is before today
	 * @throws RideAlreadyExistException         if the same ride already exists for
	 *                                           the driver
	 */
	// @WebMethod
	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail)
			throws RideMustBeLaterThanTodayException, RideAlreadyExistException;

	/**
	 * This method retrieves all rides
	 * 
	 * @return collection of all rides
	 */
	// @WebMethod
	public List<Ride> getAllRides();
	
	/**
	 * This method retrieves the rides from two locations on a given date
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date the date of the ride
	 * @return collection of rides
	 */
	// @WebMethod
	public List<Ride> getRides(String from, String to, Date date);

	/**
	 * This method retrieves from the database the dates a month for which there are
	 * events
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date of the month for which days with rides want to be retrieved
	 * @return collection of rides
	 */
	// @WebMethod
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date);

	/**
	 * This method removes a ride for a driver
	 * 
	 * @param from        the origin location of a ride
	 * @param to          the destination location of a ride
	 * @param date        the date of the ride
	 * @param driverEmail the email of the driver
	 * @return true if the ride was removed, false otherwise
	 */
	// @WebMethod
	public boolean removeRide(String from, String to, Date date, String driverEmail);

	/**
	 * This method calls the data access to initialize the database with some events
	 * and questions.
	 * It is invoked only when the option "initialize" is declared in the tag
	 * dataBaseOpenMode of resources/config.xml file
	 */
	// @WebMethod
	public void initializeBD();

}
