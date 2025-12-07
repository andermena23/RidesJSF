package domain;

import java.io.*;
import java.util.Date;

import javax.persistence.*;

@Entity
public class RideReservation implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer reservationId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ride_id", nullable = false)
	private Ride ride;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "traveler_username", nullable = false)
	private Traveler traveler;
	
	@Column(nullable = false)
	private int seatsReserved;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date reservationDate;
	
	public RideReservation() {
		super();
	}
	
	public RideReservation(Ride ride, Traveler traveler, int seatsReserved) {
		super();
		this.ride = ride;
		this.traveler = traveler;
		this.seatsReserved = seatsReserved;
		this.reservationDate = new Date();
	}
	
	/**
	 * Get the reservation ID
	 * 
	 * @return the reservation ID
	 */
	public Integer getReservationId() {
		return reservationId;
	}
	
	/**
	 * Set the reservation ID
	 * 
	 * @param reservationId to be set
	 */
	public void setReservationId(Integer reservationId) {
		this.reservationId = reservationId;
	}
	
	/**
	 * Get the ride associated with this reservation
	 * 
	 * @return the ride
	 */
	public Ride getRide() {
		return ride;
	}
	
	/**
	 * Set the ride for this reservation
	 * 
	 * @param ride to be set
	 */
	public void setRide(Ride ride) {
		this.ride = ride;
	}
	
	/**
	 * Get the traveler who made this reservation
	 * 
	 * @return the traveler
	 */
	public Traveler getTraveler() {
		return traveler;
	}
	
	/**
	 * Set the traveler for this reservation
	 * 
	 * @param traveler to be set
	 */
	public void setTraveler(Traveler traveler) {
		this.traveler = traveler;
	}
	
	/**
	 * Get the number of seats reserved
	 * 
	 * @return the number of seats reserved
	 */
	public int getSeatsReserved() {
		return seatsReserved;
	}
	
	/**
	 * Set the number of seats reserved
	 * 
	 * @param seatsReserved to be set
	 */
	public void setSeatsReserved(int seatsReserved) {
		this.seatsReserved = seatsReserved;
	}
	
	/**
	 * Get the reservation date
	 * 
	 * @return the reservation date
	 */
	public Date getReservationDate() {
		return reservationDate;
	}
	
	/**
	 * Set the reservation date
	 * 
	 * @param reservationDate to be set
	 */
	public void setReservationDate(Date reservationDate) {
		this.reservationDate = reservationDate;
	}
	
	@Override
	public String toString() {
		return "RideReservation [reservationId=" + reservationId + ", ride=" + ride.getRideNumber() 
				+ ", traveler=" + traveler.getUsername() + ", seatsReserved=" + seatsReserved 
				+ ", reservationDate=" + reservationDate + "]";
	}
}
