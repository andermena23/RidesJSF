package domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@DiscriminatorValue("DRIVER")
public class Driver extends AbstractUser {

	private static final long serialVersionUID = 1L;
	
	private String name;
	
	@OneToMany(mappedBy = "driver", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Ride> rides;

	public Driver() {
		super();
		this.rides = new HashSet<>();
	}

	public Driver(String username, String password, String email, String name) {
		super(username, password, email);
		this.name = name;
		this.rides = new HashSet<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public UserRole getRole() {
		return UserRole.DRIVER;
	}

	@Override
	public String toString() {
		return getEmail() + ";" + name + rides;
	}

	/**
	 * This method creates a bet with a question, minimum bet ammount and percentual
	 * profit
	 * 
	 * @param question   to be added to the event
	 * @param betMinimum of that question
	 * @return Bet
	 */
	public Ride addRide(String from, String to, Date date, int nPlaces, float price) {
		Ride ride = new Ride(from, to, date, nPlaces, price, this);
		rides.add(ride);
		return ride;
	}

	/**
	 * This method checks if the ride already exists for that driver
	 * 
	 * @param from the origin location
	 * @param to   the destination location
	 * @param date the date of the ride
	 * @return true if the ride exists and false in other case
	 */
	public boolean doesRideExists(String from, String to, Date date) {
		if (rides == null) {
			return false;
		}
		for (Ride r : rides)
			if ((java.util.Objects.equals(r.getFrom(), from)) && (java.util.Objects.equals(r.getTo(), to))
					&& (java.util.Objects.equals(r.getDate(), date)))
				return true;

		return false;
	}

	@Override
	public boolean equals(Object obj) {
		// Use the parent class equals which compares by username
		return super.equals(obj);
	}

	public Ride removeRide(String from, String to, Date date) {
		Ride rideToRemove = null;
		for (Ride r : rides) {
			if ((java.util.Objects.equals(r.getFrom(), from)) && (java.util.Objects.equals(r.getTo(), to))
					&& (java.util.Objects.equals(r.getDate(), date))) {
				rideToRemove = r;
				break;
			}
		}

		if (rideToRemove != null) {
			rides.remove(rideToRemove);
			return rideToRemove;
		} else {
			return null;
		}
	}

}
