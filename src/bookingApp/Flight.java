/**
 * 
 */
package bookingApp;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A class which represents a flight.
 */
public class Flight implements Serializable {
	
	/**
	 * Stores the generated ID for serialization.
	 */
	private static final long serialVersionUID = -186486982240722684L;

	/** Stores the flight number. */
	private String flightNumber;
	
	/** Stores the departure date and time for the flight. */
	private String departureDT;
	
	/** Stores the arrival date and time for the flight. */
	private String arrivalDT;
	
	/** Stores the airline of the flight. */
	private String airline;
	
	/** Stores the origin for the flight. */
	private String origin;
	
	/** Stores the destination for the flight.*/
	private String destination;
	
	/** Stores the price of the flight as a double. */
	private double cost;
	
	/** Stores the number of seats available for the flight as an int. */
	private int numSeats;
	
	/** Stores the duration of the flight in minutes. */
	private long duration;

	/**
	 * Creates new Flight with the given flight number, departure date and
	 * time, arrival date and time, airline, origin, destination, price and
	 * number of available seats.
	 * @param flightNumber Flight number of the flight.
	 * @param departureDT Departure date and time of the flight.
	 * @param arrivalDT Arrival date and time of the flight.
	 * @param airline Airline company which the flight is owned by.
	 * @param origin The place of origin the flight is leaving from.
	 * @param destination The place of destination the flight is going to.
	 * @param numSeats The number of available seats for the flight.
	 */
	public Flight(String flightNumber, String departureDT, 
			String arrivalDT, String airline, String origin, 
			String destination, String cost, String numSeats) {
		
		this.flightNumber = flightNumber;
		this.departureDT = departureDT;
		this.arrivalDT = arrivalDT;
		this.airline = airline;
		this.origin = origin;
		this.destination = destination;
		this.cost = Double.parseDouble(cost);
		this.numSeats = Integer.parseInt(numSeats);
		duration = 0;
	}
	/**
	 * Gets the flight number.
	 * @return The flight number.
	 */
	public String getFlightNumber() {
		return flightNumber;
	}
	/**
	 * Sets the flight number.
	 * @param flightNumber The flight number to set.
	 */
	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}
	/**
	 * Gets the departure date and time.
	 * @return The departure date and time in the format "YYYY-MM-DD HH:MM".
	 */
	public String getDepartureDT() {
		return departureDT;
	}
	/**
	 * Sets the departure date and time.
	 * @param departureDT The departure date and time to set, in the format
	 * "YYYY-MM-DD HH:MM"
	 */
	public void setDepartureDT(String departureDT) {
		this.departureDT = departureDT;
	}
	/**
	 * Gets the arrival date and time.
	 * @return The arrival date and time in the format "YYYY-MM-DD HH:MM".
	 */
	public String getArrivalDT() {
		return arrivalDT;
	}
	/**
	 * Sets the arrival date and time.
	 * @param arrivalDT The arrival date and time to set, in the format
	 * "YYYY-MM-DD HH:MM".
	 */
	public void setArrivalDT(String arrivalDT) {
		this.arrivalDT = arrivalDT;
	}
	/**
	 * Gets the airline of the flight.
	 * @return The airline of the flight.
	 */
	public String getAirline() {
		return airline;
	}
	/**
	 * Sets the airline of the flight.
	 * @param airline The airline of the flight to set.
	 */
	public void setAirline(String airline) {
		this.airline = airline;
	}
	/**
	 * Gets the place of origin of the flight.
	 * @return The place of origin of the flight.
	 */
	public String getOrigin() {
		return origin;
	}
	/**
	 * Sets the place of origin of the flight.
	 * @param origin The place of origin of the flight to set.
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	/**
	 * Gets the place of destination of the flight.
	 * @return The place of destination of the flight.
	 */
	public String getDestination() {
		return destination;
	}
	/**
	 * Sets the place of destination of the flight.
	 * @param destination The place of destination of the flight to set.
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}
	/**
	 * Gets the price of the flight.
	 * @return The price of the flight as a double.
	 */
	public double getCost() {
		return cost;
	}
	/**
	 * Sets the price of the flight.
	 * @param cost The flight's price to set, as a double.
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	/**
	 * Gets the number of available seats for the flight.
	 * @return The number of available seats for the flight.
	 */
	public int getNumSeats() {
		return numSeats;
	}
	
	/**
	 * Sets the number of available seats for the flight.
	 * @param numSeats The number of available seats for the flight to set.
	 */
	public void setNumSeats(int numSeats) {
		this.numSeats = numSeats;
	}
	
	/**
	 * Books a single seat of the Flight, decreasing the total number of
	 * available seats by 1.
	 */
	public void bookSeat() {
		if (getNumSeats() > 0) {
			setNumSeats(getNumSeats() - 1);
		}
	}
	
	/**
	 * Books a chosen number of seats for a Flight.
	 * @param number The number of seats to book.
	 */
	public void bookSeats(int number) {
		if (getNumSeats() >= number) {
			setNumSeats(getNumSeats() - number);
		}
	}
	
	/**
	 * Checks to see if one Flight is equal to another Flight.
	 * @param flight The Flight to be compared.
	 * @return True if both Flights are equal, False if they are not.
	 */
	public boolean equalsTo(Flight flight) {
		return (getFlightNumber().equals(flight.getFlightNumber()));
	}
	
	/**
	 * Gets a string representation of the price of this flight.
	 * @return A string representation of the price, to two decimals.
	 */
	public String getPriceString() {
		return String.format("%.2f", getCost());
	}
	
	/**
	 * Calculates the duration of a Flight.
	 * @return The total time the flight takes, in the format "HH:MM".
	 */
	public long getDuration() {
		
		// Get the date and time of the Flight's departure.
		String[] dateStrDept = getDepartureDT().split(" ")[0].split("-");
		String[] timeStrDept = getDepartureDT().split(" ")[1].split(":");
		
		// Create a new Calendar object with the Flight's departure
		// date and time.
		Calendar departureCal = new GregorianCalendar(
				Integer.parseInt(dateStrDept[0]),
				Integer.parseInt(dateStrDept[1]), 
				Integer.parseInt(dateStrDept[2]), 
				Integer.parseInt(timeStrDept[0]), 
				Integer.parseInt(timeStrDept[1]));
		
		// Get the date and time of the Flight's arrival.
		String[] dateStrArriv = getArrivalDT().split(" ")[0].split("-");
		String[] timeStrArriv = getArrivalDT().split(" ")[1].split(":");
		
		// Create a new Calendar object with the Flights' arrival
		// date and time.
		Calendar arrivalCal = new GregorianCalendar(
				Integer.parseInt(dateStrArriv[0]),
				Integer.parseInt(dateStrArriv[1]), 
				Integer.parseInt(dateStrArriv[2]), 
				Integer.parseInt(timeStrArriv[0]), 
				Integer.parseInt(timeStrArriv[1]));
		
		// Find the time difference between both Calendar objects and
		// return the result. Since the duration is in milliseconds,
		// divide it by 60000 to get the time in minutes.
		duration = (arrivalCal.getTimeInMillis() - 
				departureCal.getTimeInMillis()) / 60000;
		return duration;
	}
	
	/**
	 * Gets the string representation of this Flight's duration
	 * @return A String representation of this Flights duration, in the
	 * format HH:MM.
	 */
	public String getDurationString() {
		
		long hours = getDuration() / 60;
		long minutes = getDuration() % 60;
		return String.format("%01d:%02d", hours, minutes);
	}
	
	/**
	 * String representation of a Flight.
	 * @return A String representation of a Flight. The string contains: 
	 * the flight number, departure and arrival date and time, place of
	 * origin and destination, price of the flight and number of available
	 * seats.
	 */
	@Override
	public String toString() {
		String priceFormat = String.format("%.2f", cost);
		return  flightNumber + "," + departureDT + "," + arrivalDT + ","
				+ airline + "," + origin + "," + destination + ","
				+ priceFormat + "," + numSeats;
	}
}
