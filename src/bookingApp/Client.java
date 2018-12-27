/**
 * 
 */
package bookingApp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * A class which represents a client and stores the client's personal info.
 *
 */
public class Client implements Serializable {
	
	/**
	 * Stores the generated ID for serialization.
	 */
	private static final long serialVersionUID = 3547683276913384367L;

	/** Stores the last name of the client. */
	private String lastName;
	
	/** Stores the first name of the client. */
	private String firstName;
	
	/** Stores the email address of the client. */
	private String email;
	
	/** Stores the home address of the client. */
	private String address;
	
	/** Stores the client's credit card number. */
	private String ccNumber;
	
	/** Stores the client's credit card expiry date. */
	private String expiryDate;
	
	/** Stores a map of the client's booked flight itineraries and seats. */
	private Map<Itinerary, Integer> bookedItinSeats;
	
	/** Stores the client's password. */
	private String password;
	
	/**
	 * @param lastName The last name of the client.
	 * @param firstName The first name of the client.
	 * @param email The email address of the client.
	 * @param address The home address of the client.
	 * @param ccNumber The client's credit card number.
	 * @param expiryDate The client's credit card expiry date.
	 */
	public Client(String lastName, String firstName, String email,
			String address, String ccNumber, String expiryDate) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.address = address;
		this.ccNumber = ccNumber;
		this.expiryDate = expiryDate;
		bookedItinSeats = new HashMap<Itinerary, Integer>();
		password = "";
	}
	/** 
	 * Gets the last name of the client.
	 * @return The client's last name.
	 */
	public String getLastName() {
		return lastName;
	}
	/** 
	 * Sets the last name of the client.
	 * @param lastName The client's last name to set.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/** 
	 * Gets the first name of the client.
	 * @return The client's first name.
	 */
	public String getFirstName() {
		return firstName;
	}
	/** 
	 * Sets the first name of the client.
	 * @param firstName The client's first name to set.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/** 
	 * Gets the email of the client.
	 * @return The client's email address.
	 */
	public String getEmail() {
		return email;
	}
	/** 
	 * Sets the email of the client.
	 * @param email The client's email address to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/** 
	 * Gets the home address of the client.
	 * @return The client's home address.
	 */
	public String getAddress() {
		return address;
	}
	/** 
	 * Sets the home address of the client
	 * @param address The client's home address to set.
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/** 
	 * Gets the client's credit card number.
	 * @return The client's credit card number.
	 */
	public String getCCnumber() {
		return ccNumber;
	}
	/** 
	 * Sets the client's credit card number.
	 * @param cCnumber The client's credit card number to set.
	 */
	public void setCCnumber(String cCnumber) {
		this.ccNumber = cCnumber;
	}
	/**
	 * Gets the client's credit card expiry date.
	 * @return The client's credit card expiry date.
	 */
	public String getExpiryDate() {
		return expiryDate;
	}
	/** 
	 * Sets the client's credit card expiry date.
	 * @param expiryDate The client's credit card expiry date to set.
	 */
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	/** 
	 * String representation of a client.
	 * @return A String representation of a client. The string contains: 
	 * the last name, first name, email, home address, credit card number,
	 * and credit card expiry date of the client.
	 */
	@Override
	public String toString() {
		return (lastName + "," + firstName + "," + email + "," + 
	address + "," + ccNumber + "," + expiryDate);
	}
	
	/** 
	 * Books an itinerary of flights to this client.
	 * @param itinerary The Itinerary to book.
	 * @param numSeats The number of seats to book.
	 */
	public void bookItinSeats(Itinerary itinerary, int numSeats) {
		if (bookedItinSeats.containsKey(itinerary)) {
			int bookedSeats = bookedItinSeats.get(itinerary);
			bookedItinSeats.put(itinerary, bookedSeats + numSeats);
		} else {
			bookedItinSeats.put(itinerary, numSeats);
		}
	}
	
	/** 
	 * Gets a Map of this Client's booked Itineraries and their
	 * respective number of seats which are booked.
	 * @return The Map of booked itineraries and their number of
	 * booked seats.
	 */
	public Map<Itinerary, Integer> getBookedItinSeats() {
		return bookedItinSeats;
	}
	
	/**
	 * Gets the client's password.
	 * @return The password of the Client.
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Sets the client's password.
	 * @param password The password of the Client to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
}



