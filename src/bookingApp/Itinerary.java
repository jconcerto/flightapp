/**
 * 
 */
package bookingApp;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * A class which represents an Itinerary. Stores a list of Flights
 * as well as their total price and duration.
 */
public class Itinerary implements Serializable {
	
	/**
	 * Stores the generated ID for serialization.
	 */
	private static final long serialVersionUID = 8452545116856747078L;

	/** Stores all of the Flights. */
	private List<Flight> flightArray;
	
	/** Stores the total cost of all Flights. */
	private double totalCost;
	
	/** Stores the total duration of all Flights. */
	private long totalDuration;
	
	private int numSeats;
	
	/**
	 * @return the numSeats
	 */
	public int getNumSeats() {
		return numSeats;
	}

	/**
	 * @param numSeats the numSeats to set
	 */
	public void setNumSeats(int numSeats) {
		this.numSeats = numSeats;
	}

	/**
	 * Creates a new Itinerary and initializes a new empty flight array.
	 */
	public Itinerary () {
		flightArray = new ArrayList<Flight>();
		numSeats = 1;
	}

	/**
	 * Gets the List of flights for the Itinerary.
	 * @return The List of flights stored.
	 */
	public List<Flight> getFlightArray() {
		return flightArray;
	}
	
	/**
	 * Adds a Flight to the Itinerary.
	 * @param flight The Flight to be added.
	 */
	public void addFlight(Flight flight) {
		flightArray.add(flight);
	}
	
	/**
	 * Removes a Flight from the Itinerary.
	 * @param flight The Flight to be removed.
	 */
	public void removeFlight(Flight flight) {
		if (flightArray.contains(flight)) {
			flightArray.remove(flight);
		}
	}
	
	/**
	 * Gets the first Flight's place of origin from Itinerary.
	 * @return The place of origin of the flight itinerary.
	 */
	public String getOrigin() {
		
		// Check if the flightArray is empty first.
		if (flightArray.size() > 0) {
			
			// Return the first Flight's origin.
			return flightArray.get(0).getOrigin();
			
		// If it's empty, then there is no origin as of yet.
		} else {
			return null;
		}
	}
	
	/**
	 * Gets the last Flight's destination from Itinerary.
	 * @return The final destination of the flight itinerary.
	 */
	public String getDestination() {
		
		// Check to see if flightArray is empty first.
		if (flightArray.size() > 0) {
			
			// Return the last Flight's destination.
			return flightArray.get(flightArray.size() - 1).getDestination();
		
		// If its's empty, then there is no destination as of yet.
		} else {
			return null;
		}
	}
	
	/**
	 * Gets the total cost of all Flights in the Itinerary.
	 * @return The total cost of all Flights.
	 */
	public double getTotalCost() {
		totalCost = 0;
		for (Flight flight : flightArray) {
			totalCost += flight.getCost();
		}
		return totalCost;
	}
	
	/**
	 * Gets the departure date of the first Flight in the Itinerary.
	 * @return The departure date of the first Flight in the Itinerary,
	 * in the format "YYYY-MM-DD".
	 */
	public String getDepartureDate() {
		if (flightArray.size() > 0) {
			return flightArray.get(0).getDepartureDT().split(" ")[0];
		} else {
			return null;
		}
	}
	
	/**
	 * Gets the departure date and time of the first Flight in the Itinerary.
	 * @return The departure date and time of the first Flight in the
	 * Itinerary, in the format "YYYY-MM-DD HH:MM".
	 */
	public String getDepartDT() {
		if (flightArray.size() > 0) {
			return flightArray.get(0).getDepartureDT();
		} else {
			return null;
		}
	}
	
	/**
	 * Gets the arrival date and time of the first Flight in the Itinerary.
	 * @return The arrival date and time of the first Flight in the
	 * Itinerary, in the format "YYYY-MM-DD HH:MM".
	 */
	public String getArrivDT() {
		if (flightArray.size() > 0) {
			return flightArray.get(flightArray.size() - 1).getArrivalDT();
		} else {
			return null;
		}
	}
	
	/**
	 * Gets a string representation of the total cost of this Itinerary,
	 * to two decimal places.
	 * @return String representation of the total cost, to two decimals.
	 */
	public String getCostString() {
		
		return String.format("%.2f", getTotalCost());
	}
	
	/**
	 * Checks to see if this Itinerary contains only one flight.
	 * @return True if only one Flight exists, False if more than one exists.
	 */
	public boolean getFlightType() {
		
		// A single flight is true, more than one is false.
		if (flightArray.size() == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks to see if all flights in this Itinerary have at least the
	 * given number of seats.
	 * @return True if all flights have enough seats, False if they do not.
	 */
	public boolean checkSeats(int number) {
		
		for (Flight f : getFlightArray()) {
			if ((f.getNumSeats() < number) ||
					(number == 0)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Books the given number of seats for all flights in this Itinerary.
	 * @param number The number of seats to book.
	 */
	public void bookSeats(int number) {
		
		for (Flight f : getFlightArray()) {
			f.bookSeats(number);
		}
	}
	
	
	/**
	 * Gets the total duration of the Flights in the itinerary, from the
	 * time of departure to time of arrival.
	 * @return The total duration of the the itinerary.
	 */
	public long getTotalDuration() {
		
		// Need to store the total duration in a long since the time is
		// given in milliseconds, which could result in very large numbers.
		totalDuration = 0;
		if (getFlightArray().size() > 0) {
			
			// Get the dates and times of the first flight's departure and
			// last flight's arrival.
			String firstFlightDT = getFlightArray().get(0).getDepartureDT();
			String lastFlightDT = getFlightArray().get(
					getFlightArray().size() - 1).getArrivalDT();
			
			String[] firstDateStr = firstFlightDT.split(" ")[0].split("-");
			String[] firstTimeStr = firstFlightDT.split(" ")[1].split(":");
			
			String[] lastDateStr = lastFlightDT.split(" ")[0].split("-");
			String[] lastTimeStr = lastFlightDT.split(" ")[1].split(":");
			
			// Create Calendar objects for both dates.
			Calendar departureCal = new GregorianCalendar(
					Integer.parseInt(firstDateStr[0]),
					Integer.parseInt(firstDateStr[1]), 
					Integer.parseInt(firstDateStr[2]), 
					Integer.parseInt(firstTimeStr[0]), 
					Integer.parseInt(firstTimeStr[1]));
			
			Calendar arrivalCal = new GregorianCalendar(
					Integer.parseInt(lastDateStr[0]),
					Integer.parseInt(lastDateStr[1]), 
					Integer.parseInt(lastDateStr[2]), 
					Integer.parseInt(lastTimeStr[0]), 
					Integer.parseInt(lastTimeStr[1]));
			
			// Find the difference between the dates and return the result
			// in minutes.
			totalDuration = ((arrivalCal.getTimeInMillis() - 
					departureCal.getTimeInMillis()) / 60000);
		}
		return totalDuration;
	}
	
	/**
	 * Gets a string representation of the total duration of this itinerary,
	 * in the format HH:MM.
	 * @return The total duration of the the itinerary in the format HH:MM.
	 */
	public String getDurationString() {
		
		String s = "";
		long hours = getTotalDuration() / 60;
		long minutes = getTotalDuration() % 60;
		s += String.format("%01d:%02d", hours, minutes);
		return s;
		
	}

	/**
	 * A String representation of an Itinerary.
	 * @returns A String representation of an Itinerary. The string will
	 * contain each flight and respective flight data, as well as the total
	 * price of the itinerary and a time in the format "HH:MM" representing
	 * the total duration of the Itinerary.
	 */
	@Override
	public String toString() {
		
		String s = "";
		
		// Make each flight it's own line with respective flight data.
		for (Flight flight : flightArray) {
			
			// Price and number of seats for a Flight are not needed in this
			// part of the string, so create a substring of Flight's toString
			// up to the index of the second last comma.
			int lastCommaIndex = flight.toString().lastIndexOf(",");
			String minSeats = flight.toString().substring(0, lastCommaIndex);
			int secLastCommaIndex = minSeats.lastIndexOf(",");
			s += (flight.toString().substring(0, secLastCommaIndex) + "\n");
		}
		
		// Get the total duration in hours:minutes and the price of the
		// itinerary.
		long hours = getTotalDuration() / 60;
		long minutes = getTotalDuration() % 60;
		s += String.format("%.2f\n%02d:%02d", getTotalCost(), hours, 
				minutes);
		return s;
	}

}