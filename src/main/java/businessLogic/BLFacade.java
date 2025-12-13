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
	
	/**
	 * This method reserves a ride for a traveler
	 * 
	 * @param rideId the ID of the ride to reserve
	 * @param travelerUsername the username of the traveler
	 * @param seats the number of seats to reserve
	 * @return the created reservation, or null if the operation fails
	 */
	// @WebMethod
	public domain.RideReservation reserveRide(Integer rideId, String travelerUsername, int seats);
	
	/**
	 * This method deposits money into a traveler's wallet
	 * 
	 * @param travelerUsername the username of the traveler
	 * @param amount the amount to deposit
	 * @return true if the operation was successful, false otherwise
	 */
	// @WebMethod
	public boolean depositMoney(String travelerUsername, double amount);
	
	/**
	 * This method withdraws money from a traveler's wallet
	 * 
	 * @param travelerUsername the username of the traveler
	 * @param amount the amount to withdraw
	 * @return true if the operation was successful, false otherwise
	 */
	// @WebMethod
	public boolean withdrawMoney(String travelerUsername, double amount);
	
	/**
	 * This method gets the wallet balance of a traveler
	 * 
	 * @param travelerUsername the username of the traveler
	 * @return the wallet balance, or -1 if the user is not found or not a traveler
	 */
	// @WebMethod
	public double getWalletBalance(String travelerUsername);
	
	/**
	 * This method retrieves all reservations made by a traveler
	 * 
	 * @param travelerUsername the username of the traveler
	 * @return list of reservations made by the traveler
	 */
	// @WebMethod
	public java.util.List<domain.RideReservation> getTravelerReservations(String travelerUsername);
	
	/**
	 * This method retrieves all users in the system
	 * 
	 * @return list of all users
	 */
	// @WebMethod
	public java.util.List<User> getAllUsers();
	
	/**
	 * This method retrieves all drivers in the system
	 * 
	 * @return list of all drivers
	 */
	// @WebMethod
	public java.util.List<domain.Driver> getAllDrivers();
	
	/**
	 * This method retrieves all reservations for a specific ride
	 * 
	 * @param rideId the ID of the ride
	 * @return list of reservations for the ride
	 */
	// @WebMethod
	public java.util.List<domain.RideReservation> getRideReservations(Integer rideId);
	
	/**
	 * This method retrieves all rides created by a specific driver
	 * 
	 * @param driverEmail the email of the driver
	 * @return list of rides created by the driver
	 */
	// @WebMethod
	public java.util.List<Ride> getRidesByDriver(String driverEmail);

	//getRidesByDate
	public java.util.List<Ride> getRidesByDate(Date date);

}
