/**
 * 
 */
package bookingApp;

import java.io.IOException;
import java.util.List;

/**
 * A Driver class which implements methods needed in the design.
 * It is able to:
 * 
 * 1. Upload client and flight data from csv files into a database.
 * 2. Get a client's data from a given email.
 * 3. Get a list of valid flight itineraries from a given departure
 * date, destination and place of origin.
 * 
 */
public class Driver {
	
	/** Stores a database of Client and Flight data to be used. */
	private static Database database = new Database();
	
	/**
	 * Uploads Client info from a given csv file into application.
	 * @param path The path to the file.
	 * @throws IOException If the file not found.
	 */
	public static void uploadClientInfo(String path) {
		database.readClients(path);
	}
	
	/**
	 * Uploads Flight info from a given csv file into application.
	 * @param path The path to the file.
	 * @throws IOException If the file not found.
	 */
	public static void uploadFlightInfo(String path) {
		database.readFlights(path);
	}
	
	/**
	 * Finds a Client with a given email.
	 * @param email The email address of the Client needed to be found.
	 * @return String representation of the Client.
	 */
	public static String getClient(String email) {
		
		String result = "";
		
		// Search through the Client list and return the Client
		// with the matching email.
		for (Client client : database.getClientList()) {
			if (client.getEmail().equals(email)) {
				result = client.toString();
			}
		}
		return result;
	}
	
	/**
	 * Finds all Flights with the given date, origin, and destination
	 * @param date Departure date of the flight.
	 * @param origin Departure location of the flight.
	 * @param destination Arrival location of the flight.
	 * @return String representation of all flights with  the given 
	 * departure date, location of origin and destination.
	 */
	public static String getFlights(String date, String origin,
			String destination){
		
		String result = "";
		
		// Call the method getNeededFlights to get an ArrayList of flights
		// which meet the criteria.
		List<Flight> neededFlights = database.getNeededFlights(date, 
				origin, destination);
		
		// Create a string containing all the flights.
		for (Flight flight : neededFlights) {
			int lastCommaIndex = flight.toString().lastIndexOf(",");
			result += (flight.toString().substring(0, lastCommaIndex) + "\n");
		}
		return result;
	}
	
	/**
	 * Gets all possible Itineraries from a given departure date, 
	 * place of origin, and place of destination.
	 * 
	 * @param date The departure date of the first Flight.
	 * @param origin The place of origin of the first Flight.
	 * @param destination The destination of the last Flight.
	 * @return String representation of all itineraries with  
	 * the given departure date, origin, and destination.
	 */
	public static String getItineraries(String date, String origin, 
			String destination) {
		
		String result = "";
		
		// Use the method getItineraries in Database to find all
		// valid itineraries.
		List<Itinerary> validItin = 
				database.getItineraries(date, origin, destination);
		
		for (Itinerary itinerary : validItin) {
			// Add each result to the string.
			result += (itinerary.toString() + "\n");
		}
		return result;
	}
	
	/**
	 * Gets all possible Itineraries from a given departure date, 
	 * place of origin, and place of destination. The results are 
	 * sorted by non-decreasing cost.
	 * 
	 * @param date The departure date of the first Flight.
	 * @param origin The place of origin of the first Flight.
	 * @param destination The destination of the last Flight.
	 * @return String representation of all itineraries with given 
	 * departure date, origin, and destination. They are sorted by 
	 * non-decreasing cost.
	 */
	public static String getItinerariesSortedByCost(String date, 
			String origin, String destination) {
		
		String result = "";
		
		// Use the method getItineraries in Database to find all
		// valid itineraries.
		List<Itinerary> validItin = 
				database.getItineraries(date, origin, destination);
		
		// Use method sortByCost to sort the list of valid itineraries.
		List<Itinerary> sortedItin = database.sortItinByCost(validItin);
		
		// Add each valid Itinerary to the string.
		for (Itinerary itinerary : sortedItin) {
			result += (itinerary.toString() + "\n");
		}
		return result;
	}
	
	/**
	 * Gets all possible Itineraries from a given departure date, 
	 * place of origin, and place of destination. The results are 
	 * sorted by non-decreasing total duration.
	 * 
	 * @param date The departure date of the first Flight.
	 * @param origin The place of origin of the first Flight.
	 * @param destination The destination of the last Flight.
	 * @return String representation of all itineraries with given 
	 * departure date, origin, and destination. They are sorted by 
	 * non-decreasing total duration.
	 */
	public static String getItinerariesSortedByTime(String date, 
			String origin, String destination) {
		
		String result = "";
		
		// Use the method getItineraries in Database to find all
		// valid itineraries.
		List<Itinerary> validItin = 
				database.getItineraries(date, origin, destination);
		
		// Use method sortByCost to sort the list of valid itineraries.
		List<Itinerary> sortedItin = database.sortItinByTime(validItin);
		
		// Add each valid Itinerary to the string.
		for (Itinerary itinerary : sortedItin) {
			result += (itinerary.toString() + "\n");
		}
		return result;
	}
}
