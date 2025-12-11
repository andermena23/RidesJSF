package dataAccess;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import configuration.UtilDate;
import domain.AbstractUser;
import domain.Admin;
import domain.Driver;
import domain.Ride;
import domain.Traveler;
import domain.User;
import domain.UserRole;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

/**
 * It implements the data access to the database using Hibernate
 */
public class HibernateDataAccess {
	private EntityManager db;

	public HibernateDataAccess() {
		System.out.println("DataAccess created");
	}

	public HibernateDataAccess(EntityManager db) {
		this.db = db;
	}

	/**
	 * This is the data access method that initializes the database with some events
	 * and questions.
	 * This method is invoked by the business logic (constructor of
	 * BLFacadeImplementation) when the option "initialize" is declared in the tag
	 * dataBaseOpenMode of resources/config.xml file
	 */
	public void initializeDB() {
		try {
			db.getTransaction().begin();

			Calendar today = Calendar.getInstance();

			int month = today.get(Calendar.MONTH);
			int year = today.get(Calendar.YEAR);
			if (month == 12) {
				month = 1;
				year += 1;
			}

			// Create drivers
			Driver driver1 = new Driver("driver1", "password1", "driver1@gmail.com", "Aitor Fernandez");
			Driver driver2 = new Driver("driver2", "password2", "driver2@gmail.com", "Ane Gaztañaga");
			Driver driver3 = new Driver("driver3", "password3", "driver3@gmail.com", "Test driver");

			// Create rides
			driver1.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 4, 7);
			driver1.addRide("Donostia", "Gasteiz", UtilDate.newDate(year, month, 6), 4, 8);
			driver1.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25), 4, 4);
			driver1.addRide("Donostia", "Iruña", UtilDate.newDate(year, month, 7), 4, 8);

			driver2.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 3, 3);
			driver2.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25), 2, 5);
			driver2.addRide("Eibar", "Gasteiz", UtilDate.newDate(year, month, 6), 2, 5);

			driver3.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 14), 1, 3);

			db.persist(driver1);
			db.persist(driver2);
			db.persist(driver3);
			
			// Create test users
			Admin admin1 = new Admin("admin", "admin123", "admin@ridesjsf.com");
			Traveler traveler1 = new Traveler("traveler1", "traveler123", "traveler1@ridesJSF.com");
			Traveler traveler2 = new Traveler("traveler2", "traveler123", "traveler2@ridesJSF.com");
			
			db.persist(admin1);
			db.persist(traveler1);
			db.persist(traveler2);

			db.getTransaction().commit();
			System.out.println("Db initialized");
		} catch (Exception e) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			e.printStackTrace();
		}
	}

	/**
	 * This method returns all the cities where rides depart
	 * 
	 * @return collection of cities
	 */
	public List<String> getDepartCities() {
		TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.from FROM Ride r ORDER BY r.from", String.class);
		List<String> cities = query.getResultList();
		return cities;

	}

	/**
	 * This method returns all the arrival destinations, from all rides that depart
	 * from a given city
	 * 
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */
	public List<String> getArrivalCities(String from) {
		TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.to FROM Ride r WHERE r.from=?1 ORDER BY r.to",
				String.class);
		query.setParameter(1, from);
		List<String> arrivingCities = query.getResultList();
		return arrivingCities;

	}

	/**
	 * This method creates a ride for a driver
	 * 
	 * @param from        the origin location of a ride
	 * @param to          the destination location of a ride
	 * @param date        the date of the ride
	 * @param nPlaces     available seats
	 * @param driverEmail to which ride is added
	 * 
	 * @return the created ride, or null, or an exception
	 * @throws RideMustBeLaterThanTodayException if the ride date is before today
	 * @throws RideAlreadyExistException         if the same ride already exists for
	 *                                           the driver
	 */
	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail)
			throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
		System.out.println(">> DataAccess: createRide=> from= " + from + " to= " + to + " driver=" + driverEmail
				+ " date " + date);
		
		if (new Date().compareTo(date) > 0) {
			throw new RideMustBeLaterThanTodayException(
					ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday"));
		}
		
		try {
			db.getTransaction().begin();

			// Find driver by email (not by primary key username)
			TypedQuery<Driver> query = db.createQuery(
				"SELECT d FROM Driver d WHERE d.email=?1", Driver.class);
			query.setParameter(1, driverEmail);
			
			List<Driver> drivers = query.getResultList();
			if (drivers.isEmpty()) {
				System.out.println(">> DataAccess: createRide=> driver not found with email: " + driverEmail);
				db.getTransaction().rollback();
				return null;
			}
			
			Driver driver = drivers.get(0);
			System.out.println(">> DataAccess: createRide=> found driver: " + driver.getUsername());
			
			if (driver.doesRideExists(from, to, date)) {
				db.getTransaction().rollback();
				throw new RideAlreadyExistException(
						ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
			}
			
			System.out.println("crea addRide");
			Ride ride = driver.addRide(from, to, date, nPlaces, price);
			db.merge(driver);
			db.getTransaction().commit();

			return ride;
		} catch (RideAlreadyExistException e) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			throw e;
		} catch (Exception e) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * This method retrieves all rides
	 * 
	 * @return collection of all rides
	 */
	public List<Ride> getAllRides() {
		System.out.println(">> DataAccess: getAllRides");
		
		List<Ride> res = new ArrayList<Ride>();
		TypedQuery<Ride> query = db.createQuery("SELECT r FROM Ride r", Ride.class);
		List<Ride> rides = query.getResultList();
		for (Ride ride : rides) {
			res.add(ride);
		}
		return res;
	}
	
	/**
	 * This method retrieves all rides created by a specific driver
	 * 
	 * @param driverEmail the email of the driver
	 * @return collection of rides created by the driver
	 */
	public List<Ride> getRidesByDriver(String driverEmail) {
		System.out.println(">> DataAccess: getRidesByDriver=> driver email= " + driverEmail);
		
		List<Ride> res = new ArrayList<Ride>();
		try {
			// Find driver by email
			TypedQuery<Driver> driverQuery = db.createQuery(
				"SELECT d FROM Driver d WHERE d.email=?1", Driver.class);
			driverQuery.setParameter(1, driverEmail);
			
			List<Driver> drivers = driverQuery.getResultList();
			if (drivers.isEmpty()) {
				System.out.println(">> DataAccess: getRidesByDriver=> driver not found with email: " + driverEmail);
				return res;
			}
			
			Driver driver = drivers.get(0);
			System.out.println(">> DataAccess: getRidesByDriver=> found driver: " + driver.getUsername());
			
			// Query rides for this driver
			TypedQuery<Ride> rideQuery = db.createQuery(
				"SELECT r FROM Ride r WHERE r.driver.username=?1 ORDER BY r.date DESC", Ride.class);
			rideQuery.setParameter(1, driver.getUsername());
			res = rideQuery.getResultList();
			
			System.out.println(">> DataAccess: getRidesByDriver=> found " + res.size() + " rides");
		} catch (Exception e) {
			System.err.println(">> DataAccess: getRidesByDriver=> error: " + e.getMessage());
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * This method retrieves the rides from two locations on a given date
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date the date of the ride
	 * @return collection of rides
	 */
	public List<Ride> getRides(String from, String to, Date date) {
		System.out.println(">> DataAccess: getRides=> from= " + from + " to= " + to + " date " + date);

		List<Ride> res = new ArrayList<Ride>();
		TypedQuery<Ride> query = db.createQuery("SELECT r FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date=?3",
				Ride.class);
		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, date);
		List<Ride> rides = query.getResultList();
		for (Ride ride : rides) {
			res.add(ride);
		}
		return res;
	}

	/**
	 * This method retrieves from the database the dates a month for which there are
	 * events
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date of the month for which days with rides want to be retrieved
	 * @return collection of rides
	 */
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		System.out.println(">> DataAccess: getEventsMonth");
		List<Date> res = new ArrayList<Date>();

		Date firstDayMonthDate = UtilDate.firstDayMonth(date);
		Date lastDayMonthDate = UtilDate.lastDayMonth(date);

		TypedQuery<Date> query = db.createQuery(
				"SELECT DISTINCT r.date FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date BETWEEN ?3 and ?4",
				Date.class);

		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, firstDayMonthDate);
		query.setParameter(4, lastDayMonthDate);
		List<Date> dates = query.getResultList();
		for (Date d : dates) {
			res.add(d);
		}
		return res;
	}

	/**
	 * This method removes a ride
	 * 
	 * @param from        the origin location of a ride
	 * @param to          the destination location of a ride
	 * @param date        the date of the ride
	 * @param driverEmail the email of the driver
	 * @return true if the ride was removed, false otherwise
	 */
	public boolean removeRide(String from, String to, Date date, String driverEmail) {
		System.out.println(">> DataAccess: removeRide=> from= " + from + " to= " + to + " driver=" + driverEmail
				+ " date " + date);
		
		try {
			db.getTransaction().begin();

			// Find driver by email (not by primary key username)
			TypedQuery<Driver> query = db.createQuery(
				"SELECT d FROM Driver d WHERE d.email=?1", Driver.class);
			query.setParameter(1, driverEmail);
			
			List<Driver> drivers = query.getResultList();
			if (drivers.isEmpty()) {
				System.out.println(">> DataAccess: removeRide=> driver not found with email: " + driverEmail);
				db.getTransaction().rollback();
				return false;
			}
			
			Driver driver = drivers.get(0);
			System.out.println(">> DataAccess: removeRide=> found driver: " + driver.getUsername());
			
			Ride removedRide = driver.removeRide(from, to, date);
			if (removedRide != null) {
				System.out.println(">> DataAccess: removeRide=> ride removed from driver, committing");
				db.merge(driver);
				db.getTransaction().commit();
				return true;
			} else {
				System.out.println(">> DataAccess: removeRide=> ride not found in driver's rides");
				db.getTransaction().rollback();
				return false;
			}
		} catch (Exception e) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		}
	}

	public void open() {
		if (db == null || !db.isOpen()) {
			db = JPAUtil.getEntityManager();
		}
		System.out.println("DataAccess opened");
	}

	public void close() {
		if (db != null && db.isOpen()) {
			db.close();
		}
		System.out.println("DataAccess closed");
	}
	
	/**
	 * This method authenticates a user
	 * 
	 * @param username the username
	 * @param password the password
	 * @return the authenticated user or null if authentication fails
	 */
	public User authenticateUser(String username, String password) {
		System.out.println(">> DataAccess: authenticateUser=> username= " + username);
		
		try {
			TypedQuery<AbstractUser> query = db.createQuery(
				"SELECT u FROM AbstractUser u WHERE u.username=?1 AND u.password=?2", 
				AbstractUser.class);
			query.setParameter(1, username);
			query.setParameter(2, password);
			
			List<AbstractUser> users = query.getResultList();
			if (users.isEmpty()) {
				return null;
			}
			return users.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * This method registers a new user
	 * 
	 * @param username the username
	 * @param password the password
	 * @param email the email
	 * @param role the role (driver, admin, or traveler)
	 * @return the created user
	 */
	public User registerUser(String username, String password, String email, String role) {
		return registerUser(username, password, email, role, null);
	}
	
	/**
	 * This method registers a new user with optional name
	 * 
	 * @param username the username
	 * @param password the password
	 * @param email the email
	 * @param role the role (driver, admin, or traveler)
	 * @param name the name (used for drivers, can be null)
	 * @return the created user
	 */
	public User registerUser(String username, String password, String email, String role, String name) {
		System.out.println(">> DataAccess: registerUser=> username= " + username + ", role= " + role + ", name= " + name);
		
		try {
			db.getTransaction().begin();
			
			// Check if user already exists
			AbstractUser existingUser = db.find(AbstractUser.class, username);
			if (existingUser != null) {
				db.getTransaction().rollback();
				return null;
			}
			
			// Create appropriate user type based on role
			UserRole userRole = UserRole.fromString(role);
			User user;
			switch (userRole) {
				case DRIVER:
					String driverName = (name != null && !name.trim().isEmpty()) ? name : username;
					user = new Driver(username, password, email, driverName);
					break;
				case ADMIN:
					user = new Admin(username, password, email);
					break;
				case TRAVELER:
				default:
					user = new Traveler(username, password, email);
					break;
			}
			
			db.persist(user);
			db.getTransaction().commit();
			
			return user;
		} catch (Exception e) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * This method reserves a ride for a traveler
	 * 
	 * @param rideId the ID of the ride to reserve
	 * @param travelerUsername the username of the traveler
	 * @param seats the number of seats to reserve
	 * @return the created reservation, or null if the operation fails
	 */
	public domain.RideReservation reserveRide(Integer rideId, String travelerUsername, int seats) {
		System.out.println(">> DataAccess: reserveRide=> rideId= " + rideId + ", travelerUsername= " + travelerUsername + ", seats= " + seats);
		
		try {
			db.getTransaction().begin();
			
			// Find the ride
			Ride ride = db.find(Ride.class, rideId);
			if (ride == null) {
				System.out.println(">> DataAccess: reserveRide=> ride not found");
				db.getTransaction().rollback();
				return null;
			}
			
			// Find the traveler
			Traveler traveler = db.find(Traveler.class, travelerUsername);
			if (traveler == null) {
				System.out.println(">> DataAccess: reserveRide=> traveler not found");
				db.getTransaction().rollback();
				return null;
			}
			
			// Check if enough seats are available
			if (ride.getnPlaces() < seats) {
				System.out.println(">> DataAccess: reserveRide=> not enough seats available");
				db.getTransaction().rollback();
				return null;
			}
			
			// Calculate total cost
			double totalCost = ride.getPrice() * seats;
			System.out.println(">> DataAccess: reserveRide=> total cost= " + totalCost + ", traveler balance= " + traveler.getWallet());
			
			// Check if traveler has sufficient funds
			if (traveler.getWallet() < totalCost) {
				System.out.println(">> DataAccess: reserveRide=> insufficient funds");
				db.getTransaction().rollback();
				return null;
			}
			
			// Deduct cost from traveler's wallet
			boolean withdrawn = traveler.withdraw(totalCost);
			if (!withdrawn) {
				System.out.println(">> DataAccess: reserveRide=> failed to withdraw funds");
				db.getTransaction().rollback();
				return null;
			}
			
			// Create the reservation
			domain.RideReservation reservation = new domain.RideReservation(ride, traveler, seats);
			db.persist(reservation);
			
			// Update available seats in the ride
			ride.setBetMinimum((int)ride.getnPlaces() - seats);
			db.merge(ride);
			db.merge(traveler);
			
			db.getTransaction().commit();
			System.out.println(">> DataAccess: reserveRide=> reservation created successfully. New balance: " + traveler.getWallet());
			
			return reservation;
		} catch (Exception e) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * This method deposits money into a traveler's wallet
	 * 
	 * @param travelerUsername the username of the traveler
	 * @param amount the amount to deposit
	 * @return true if the operation was successful, false otherwise
	 */
	public boolean depositMoney(String travelerUsername, double amount) {
		System.out.println(">> DataAccess: depositMoney=> travelerUsername= " + travelerUsername + ", amount= " + amount);
		
		try {
			db.getTransaction().begin();
			
			// Find the traveler
			Traveler traveler = db.find(Traveler.class, travelerUsername);
			if (traveler == null) {
				System.out.println(">> DataAccess: depositMoney=> traveler not found");
				db.getTransaction().rollback();
				return false;
			}
			
			// Deposit money
			boolean success = traveler.deposit(amount);
			if (!success) {
				System.out.println(">> DataAccess: depositMoney=> invalid amount");
				db.getTransaction().rollback();
				return false;
			}
			
			db.merge(traveler);
			db.getTransaction().commit();
			System.out.println(">> DataAccess: depositMoney=> money deposited successfully. New balance: " + traveler.getWallet());
			
			return true;
		} catch (Exception e) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * This method withdraws money from a traveler's wallet
	 * 
	 * @param travelerUsername the username of the traveler
	 * @param amount the amount to withdraw
	 * @return true if the operation was successful, false otherwise
	 */
	public boolean withdrawMoney(String travelerUsername, double amount) {
		System.out.println(">> DataAccess: withdrawMoney=> travelerUsername= " + travelerUsername + ", amount= " + amount);
		
		try {
			db.getTransaction().begin();
			
			// Find the traveler
			Traveler traveler = db.find(Traveler.class, travelerUsername);
			if (traveler == null) {
				System.out.println(">> DataAccess: withdrawMoney=> traveler not found");
				db.getTransaction().rollback();
				return false;
			}
			
			// Withdraw money
			boolean success = traveler.withdraw(amount);
			if (!success) {
				System.out.println(">> DataAccess: withdrawMoney=> insufficient funds or invalid amount");
				db.getTransaction().rollback();
				return false;
			}
			
			db.merge(traveler);
			db.getTransaction().commit();
			System.out.println(">> DataAccess: withdrawMoney=> money withdrawn successfully. New balance: " + traveler.getWallet());
			
			return true;
		} catch (Exception e) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * This method gets the wallet balance of a traveler
	 * 
	 * @param travelerUsername the username of the traveler
	 * @return the wallet balance, or -1 if the user is not found or not a traveler
	 */
	public double getWalletBalance(String travelerUsername) {
		System.out.println(">> DataAccess: getWalletBalance=> travelerUsername= " + travelerUsername);
		
		try {
			// Find the traveler
			Traveler traveler = db.find(Traveler.class, travelerUsername);
			if (traveler == null) {
				System.out.println(">> DataAccess: getWalletBalance=> traveler not found");
				return -1;
			}
			
			double balance = traveler.getWallet();
			System.out.println(">> DataAccess: getWalletBalance=> balance= " + balance);
			return balance;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * This method retrieves all reservations made by a traveler
	 * 
	 * @param travelerUsername the username of the traveler
	 * @return list of reservations made by the traveler
	 */
	public java.util.List<domain.RideReservation> getTravelerReservations(String travelerUsername) {
		System.out.println(">> DataAccess: getTravelerReservations=> travelerUsername= " + travelerUsername);
		
		try {
			TypedQuery<domain.RideReservation> query = db.createQuery(
				"SELECT r FROM RideReservation r WHERE r.traveler.username = :username ORDER BY r.reservationDate DESC",
				domain.RideReservation.class);
			query.setParameter("username", travelerUsername);
			
			java.util.List<domain.RideReservation> reservations = query.getResultList();
			System.out.println(">> DataAccess: getTravelerReservations=> found " + reservations.size() + " reservations");
			
			return reservations;
		} catch (Exception e) {
			e.printStackTrace();
			return new java.util.ArrayList<>();
		}
	}
	
	/**
	 * This method retrieves all users in the system
	 * 
	 * @return list of all users
	 */
	public java.util.List<User> getAllUsers() {
		System.out.println(">> DataAccess: getAllUsers");
		
		try {
			TypedQuery<AbstractUser> query = db.createQuery(
				"SELECT u FROM AbstractUser u ORDER BY u.username",
				AbstractUser.class);
			
			java.util.List<AbstractUser> abstractUsers = query.getResultList();
			java.util.List<User> users = new java.util.ArrayList<>(abstractUsers);
			System.out.println(">> DataAccess: getAllUsers=> found " + users.size() + " users");
			
			return users;
		} catch (Exception e) {
			e.printStackTrace();
			return new java.util.ArrayList<>();
		}
	}
	
	/**
	 * This method retrieves all reservations for a specific ride
	 * 
	 * @param rideId the ID of the ride
	 * @return list of reservations for the ride
	 */
	public java.util.List<domain.RideReservation> getRideReservations(Integer rideId) {
		System.out.println(">> DataAccess: getRideReservations=> rideId= " + rideId);
		
		try {
			TypedQuery<domain.RideReservation> query = db.createQuery(
				"SELECT r FROM RideReservation r WHERE r.ride.rideNumber = :rideId ORDER BY r.reservationDate DESC",
				domain.RideReservation.class);
			query.setParameter("rideId", rideId);
			
			java.util.List<domain.RideReservation> reservations = query.getResultList();
			System.out.println(">> DataAccess: getRideReservations=> found " + reservations.size() + " reservations");
			
			return reservations;
		} catch (Exception e) {
			e.printStackTrace();
			return new java.util.ArrayList<>();
		}
	}

}
