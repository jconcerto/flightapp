/**
 * 
 */
package bookingApp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flightapp.androidproject.Constants;

/**
 * A class which stores and uploads a list of all clients, flights 
 * and itineraries. It is responsible for:
 * 
 * 1. Reading a list of clients and flights from csv files and saving the data.
 * 2. Searching through flights and finding all other flights which connect.
 * 3. Creating flight itineraries for any origin and destination.
 * 4. Searching through flight itineraries which meet given criteria.
 * 5. Sorting flight itineraries by total cost or time.
 * 6. Searching through clients which match a given email.
 * 
 */
public class Database implements Serializable{

	/**
	 * Stores the generated ID for serialization.
	 */
	private static final long serialVersionUID = -8918094051072313759L;

	/** Stores a list of all available flights. */
	private List<Flight> flights;
	
	/** Stores a list of all available clients. */
	private List<Client> clients;
	
	/** Stores a Map of flights and their connecting flights. */
	private Map<Flight, List<Flight>> flightMap;
	
	/** Stores a List of usernames */
	private List<String[]> userLogins;
	
	/** The maximum layover time allowed in a flight itinerary, in minutes. */
	public static final int MAX_LAYOVER_TIME = 360; // The default is 6 hours.
	
	
	/**
	 * Creates a new Database and initializes a new list of 
	 * Clients and Flights, as well as a map of flights which link
	 * to one another.
	 */
	public Database() {
		flights = new ArrayList<Flight>();
		clients = new ArrayList<Client>();
		userLogins = new ArrayList<String[]>();
		flightMap = new HashMap<Flight, List<Flight>>();
	}
	
	/**
	 * Creates a new HashMap where each key is a Flight and the value of
	 * each key is an ArrayList containing other Flights which connect to it.
	 */
	public void createFlightMap() {
		
		// Create a new HashMap to store all flights and connecting flights.
		Map<Flight, List<Flight>> linkedFlightMap = 
				new HashMap<Flight, List<Flight>>();
		// Use a list to store the connecting flights.
		List<Flight> connectingFlights;
		
		for (Flight originFlight : flights) {
			
				// Create a new ArrayList to store all connecting flights.
				connectingFlights = new ArrayList<Flight>();
				
				// Get the date and time of the origin flight.
				String[] firstArrivDT = originFlight.getArrivalDT().split(" ");
				String[] firstArrivDate = firstArrivDT[0].split("-");
				String[] firstArrivTime = firstArrivDT[1].split(":");
				
				// Creates a calendar object with the origin flight's arrival
				// date and time.
				Calendar firstDeptCal = new GregorianCalendar(
						Integer.parseInt(firstArrivDate[0]), 
						Integer.parseInt(firstArrivDate[1]), 
						Integer.parseInt(firstArrivDate[2]),
						Integer.parseInt(firstArrivTime[0]), 
						Integer.parseInt(firstArrivTime[1]));
				
				for (Flight linkedFlight : flights) {
					
					// Do not link the flights if they are the same Flight
					// or has no available seats.
					if (!linkedFlight.equalsTo(originFlight)) {
						
						// Get the date and time of the Flight to be linked.
						String[] linkedDeptDT = 
								linkedFlight.getDepartureDT().split(" ");
						String[] linkedDeptDate = linkedDeptDT[0].split("-");
						String[] linkedDeptTime = linkedDeptDT[1].split(":");
						
						// Creates a calendar object with the connecting 
						// Flight's departure date and time.
						Calendar linkedOrigCal = new GregorianCalendar(
								Integer.parseInt(linkedDeptDate[0]), 
								Integer.parseInt(linkedDeptDate[1]), 
								Integer.parseInt(linkedDeptDate[2]),
								Integer.parseInt(linkedDeptTime[0]), 
								Integer.parseInt(linkedDeptTime[1]));
						
						// Finds the difference in time between the two 
						// Flights in minutes.
						long timeDiff = (linkedOrigCal.getTimeInMillis() - 
								firstDeptCal.getTimeInMillis()) / 60000;
						
						// If the given flight is a valid connecting flight, 
						//then adds it to the ArrayList.
						if ((timeDiff <= MAX_LAYOVER_TIME) && (timeDiff > 0)) {
							if (originFlight.getDestination().equals
									(linkedFlight.getOrigin()) && 
									(linkedFlight.getNumSeats() > 0)) {
								connectingFlights.add(linkedFlight);
							}
						}
				}
				// Add the flight key and List of linked flights to HashMap.
				linkedFlightMap.put(originFlight, connectingFlights);
			}
		}
		flightMap = linkedFlightMap;
	}
	
	/**
	 * Gets a map of flights and their respective connecting flights.
	 * @return The HashMap of flight keys and ArrayList flight values
	 * which connect to each flight.
	 */
	public Map<Flight, List<Flight>> getFlightMap() {
		return flightMap;
	}
	
	/**
	 * Reads a csv file and saves all client data found in the file inside
	 * an ArrayList.
	 * @param path The path of the file to read.
	 */
	public void readClients(Object path) {
		
		// Create a new ArrayList of clients.
		clients = new ArrayList<Client>();
		
		try {
			
			BufferedReader csvFile = null;
			
			if (path instanceof String) {
				csvFile = new BufferedReader(new FileReader((String) path));
				
			} else if (path instanceof InputStreamReader) {
				csvFile = new BufferedReader((InputStreamReader) path);
			}
			
			String lineOfText;
			
			// Read each line of the file.
			while ((lineOfText = csvFile.readLine()) != null) {
				
				if (lineOfText.length() > 0) {
					
					// Split the line by the commas to get each data field.
					String[] infoArray = lineOfText.split(",");
					
					// Get each individual data field from the array.
					String lastName = infoArray[0];
					String firstName = infoArray[1];
					String email = infoArray[2];
					String address = infoArray[3];
					String CCnumber = infoArray[4];
					String expiryDate = infoArray[5];
					
					// Create a new Client object and add it to the 
					// ArrayList of Clients.
					clients.add(new Client(lastName, firstName, email, 
							address, CCnumber, expiryDate));
				}
			}
			csvFile.close();
			
		// Print an error message if the file cannot be found.
		} catch(IOException fileError) {
			System.out.println(fileError);
		}
	}
	
	/**
	 * Reads a csv file and saves all flight data found in the file inside
	 * an ArrayList.
	 * @param path The path of the file to read.
	 */
	public void readFlights(Object path) {
		
		// Create a new ArrayList of flights.
		flights = new ArrayList<Flight>();
		
		try {
			
			BufferedReader csvFile = null;
			
			if (path instanceof String) {
				csvFile = new BufferedReader(new FileReader((String) path));
				
			} else if (path instanceof InputStreamReader) {
				csvFile = new BufferedReader((InputStreamReader) path);
			}
			
			String lineOfText;
			
			// Read each line of the file.
			while ((lineOfText = csvFile.readLine()) != null) {
				
				if (lineOfText.length() > 0) {
					
					// Split the line by the commas to get each data field.
					String[] infoArray = lineOfText.split(",");
					
					// Get each individual data field from the array.
					String flightNum = infoArray[0];
					String departureDT = infoArray[1];
					String arrivalDT = infoArray[2];
					String airline = infoArray[3];
					String origin = infoArray[4];
					String destination = infoArray[5];
					String cost = infoArray[6];
					String numSeats = infoArray[7];
					
					// Create a new Flight object and add it to the 
					// ArrayList of Flights.
					flights.add(new Flight(flightNum, departureDT, 
							arrivalDT, airline, origin, destination, 
							cost, numSeats));
				}
			}
			csvFile.close();
			
		// Print an error message if the file cannot be found.
		} catch (IOException fileError) {
			System.out.println(fileError);
		}
	}
	
	/**
	 * Gets a list of all Clients.
	 * @return The list of all Clients.
	 */
	public List<Client> getClientList() {
		return clients;
	}
	
	/**
	 * Gets a list of all Flights.
	 * @return The list of all Flights.
	 */
	public List<Flight> getFlightList() {
		return flights;
	}
	
	/**
	 * Checks to see if a Flight already exists in an ArrayList.
	 * @param flightList The list of Flights to check.
	 * @param flight The Flight to check.
	 * @return True if the Flight exists in flightList, False if not.
	 */
	private boolean isRepeat(List<Flight> flightList, Flight flight) {
		for (Flight f : flightList) {
			if (f.equalsTo(flight)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Creates an Itinerary object from a given ArrayList of Flights.
	 * @param flightList The list of Flights to create an Itinerary from.
	 * @return The Itinerary made from the Flights in flightList.
	 */
	private Itinerary createItinerary(List<Flight> flightList) {
		
		// Create a new Itinerary object.
		Itinerary itinerary = new Itinerary();
		
		// For each Flight in the flightList, add it to the Itinerary.
		for (Flight f : flightList) {
			itinerary.addFlight(f);
		}
		return itinerary;
	}
	
	/**
	 * Checks to see if a flight goes back to a location already visited.
	 * This method is used to make sure that an Itinerary does not contain
	 * a list of linked flights that go around in a circle, for example.
	 * @param list The list of Flights to check.
	 * @param flight The Flight to be checked.
	 * @return True if the Flight is valid and does not return to a
	 * previously visited location, False otherwise.
	 */
	private boolean checkRepeatFlight(List<Flight> list, Flight flight) {
		
		if (list.size() > 1) {
			for (Flight checkFlight : list) {
				if (!flight.equalsTo(checkFlight)) {
					
					// If the Flight's destination is the same as the origin
					// of any Flight in the list, then it is not valid.
					if (flight.getDestination().equals
							(checkFlight.getOrigin())) {
						return false;
					}
					
					// If the Flight's origin is the same as the origin of
					// any Flight in the list, then it is also not valid.
					else if (flight.getOrigin().equals
							(checkFlight.getOrigin())) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Makes a list of all possible valid Itinerary objects from a HashMap 
	 * of flights and their connecting flights. This method is recursive 
	 * and adds all possible itineraries into an ArrayList.
	 * @param flight The flight currently to be added to a new Itinerary.
	 * @param flightArray The list of flights representing an itinerary.
	 * @param allIT The list of all possible itineraries.
	 */
	private void makeItineraryList(Flight flight, List<Flight> flightArray,
			List<Itinerary> allItin) {
		
		// If there are no flights at all in the flightMap, then there are
		// no possible itineraries.
		if (getFlightMap().isEmpty()) {
			allItin = null;
		}
		
		// Base case: If a Flight has no connecting flights, add it to the
		// flightArray.
		else if (getFlightMap().get(flight).isEmpty()) {
			
			// Check to make sure the Flight is not repeated.
			if (!isRepeat(flightArray, flight) && 
					checkRepeatFlight(flightArray, flight)) {
				
				// Add the Flight to the flightArray.
				flightArray.add(flight);
				
				// Create a new Itinerary from the list.
				allItin.add(createItinerary(flightArray));
				
				// We are recursing through all Flights, so remove this 
				// Flight from the List itinerary once we are done with it.
				flightArray.remove(flight);
			}
		}
		
		else {
			
			// If flight has connecting Flights, recurse through all it's
			// linked flights.
			for (Flight f : getFlightMap().get(flight)) {
				
				// Only add the Flight to a itinerary if it is not repeated.
				if (!isRepeat(flightArray, flight) && 
						checkRepeatFlight(flightArray, flight)) {
					flightArray.add(flight);
					allItin.add(createItinerary(flightArray));
				} 
				makeItineraryList(f, flightArray, allItin);
				
				// Remove the Flight from the List once we are done.
				flightArray.remove(f);
			}
		}
	}
	
	/**
	 * Gets a list of all possible itineraries made from a stored list of
	 * flights and their connecting paths.
	 * @return The list of all possible itineraries.
	 */
	public List<Itinerary> getAllItineraries() {
		
		// Initializes a flight map of Flights and their connecting flights.
		createFlightMap();
		
		// Create a new ArrayList to contain all Itinerary objects.
		List<Itinerary> itineraryList = new ArrayList<Itinerary>();
		
		// For each Flight, use the helper method makeItineraryList which
		// recurses and finds all the itineraries for the flights which
		// connect to it.
		for (Flight flight : flights) {
			List<Flight> addItinerary = new ArrayList<Flight>();
			makeItineraryList(flight, addItinerary, itineraryList);
		}
		return itineraryList;
	}
	
	/**
	 * Gets a list of all itineraries on a specified date, origin 
	 * and destination.
	 * @param date The departure date of each itinerary needed.
	 * @param origin The place of origin of each itinerary needed.
	 * @param destination The destination place of each itinerary needed.
	 * @return An ArrayList of itineraries which take place on the specified
	 * date, place of origin and destination.
	 */
	public List<Itinerary> getItineraries(String date, String origin, 
			String destination) {
		
		// Call the method getAllItineraries to find every itinerary
		// possibility and create a new ArrayList to store valid itineraries.
		List<Itinerary> allItineraries = getAllItineraries();
		List<Itinerary> validItins = new ArrayList<Itinerary>();
		
		for (Itinerary itinerary : allItineraries) {
			
			// If the date, origin and destination of each Itinerary
			// match the needed date, origin and destination, 
			// add it to the ArrayList.
			if (itinerary.getOrigin().equals(origin) && 
					itinerary.getDestination().equals(destination) && 
					itinerary.getDepartureDate().equals(date)) {
				validItins.add(itinerary);
			}
		}
		// Return the ArrayList containing all valid itineraries.
		return validItins;
	}
	
	/**
	 * Sorts a given list of itineraries by non-decreasing total duration.
	 * Uses the quicksort algorithm to sort the list in order.
	 * @param itinList The list of itineraries to be sorted.
	 * @return A list of itineraries sorted by duration.
	 */
	public List<Itinerary> sortItinByTime(List<Itinerary> itinList) {
		
		// Base case: if the list has one element or is empty, it is sorted.
		if (itinList.size() <= 1) {
			return itinList;
		}
		
		// Set the pivot to an itinerary in the middle of the list.
		Itinerary pivot = itinList.get((int)(itinList.size() / 2));
		
		// Create lists for itineraries less than, greater than and equal
		// to the pivot in total duration.
		List<Itinerary> lessThan = new ArrayList<Itinerary>();
		List<Itinerary> greaterThan = new ArrayList<Itinerary>();
		List<Itinerary> same = new ArrayList<Itinerary>();
		
		// Create a list to store the final sorted list of itineraries.
		List<Itinerary> finalList = new ArrayList<Itinerary>();
		
		for(int i = 0; i < itinList.size(); i++) {
			
			// If an itinerary's duration is longer than the pivot, 
			// add it to the greaterThan list.
			if (itinList.get(i).getTotalDuration() 
					> pivot.getTotalDuration()) {
				greaterThan.add(itinList.get(i));
				
			// If an itinerary's duration is shorter than the pivot, 
			// add it to the lessThan list.
			} else if (itinList.get(i).getTotalDuration() 
					< pivot.getTotalDuration()) {
				lessThan.add(itinList.get(i));
			
			// If an itinerary's duration is equal to the pivot, 
			// add it to the same list.
			} else {
				same.add(itinList.get(i));
			}
		}
		
		// Recurse through both lessThan and greaterThan lists to sort them.
		lessThan = sortItinByTime(lessThan);
		greaterThan = sortItinByTime(greaterThan);
		
		// Add the three parts to the final list and return the result.
		finalList.addAll(lessThan);
		finalList.addAll(same);
		finalList.addAll(greaterThan);
		
		return finalList;
	}
	
	/**
	 * Sorts a given list of itineraries by non-decreasing total cost.
	 * Uses the quicksort algorithm to sort the list in order.
	 * @param itinList The list of itineraries to be sorted.
	 * @return A list of itineraries sorted by cost.
	 */
	public List<Itinerary> sortItinByCost(List<Itinerary> itinList) {
		
		// Base case: if the list has one element or is empty, it is sorted.
		if (itinList.size() <= 1) {
			return itinList;
		}
		
		// Set the pivot to an itinerary in the middle of the list.
		Itinerary pivot = itinList.get((int)(itinList.size() / 2));
		
		// Create lists for itineraries less than, greater than and equal
		// to the pivot in total cost.
		List<Itinerary> lessThan = new ArrayList<Itinerary>();
		List<Itinerary> greaterThan = new ArrayList<Itinerary>();
		List<Itinerary> same = new ArrayList<Itinerary>();
		
		// Create a list to store the final sorted list of itineraries.
		List<Itinerary> finalList = new ArrayList<Itinerary>();
		
		for(int i = 0; i < itinList.size(); i++) {
			
			// If an itinerary's cost is greater than the pivot, 
			// add it to the greaterThan list.
			if (itinList.get(i).getTotalCost() > pivot.getTotalCost()) {
				greaterThan.add(itinList.get(i));
				
			// If an itinerary's cost is less than the pivot, 
			// add it to the lessThan list.
			} else if (itinList.get(i).getTotalCost() < pivot.getTotalCost()) {
				lessThan.add(itinList.get(i));
			
			// If an itinerary's cost is equal to the pivot, 
			// add it to the same list.
			} else {
				same.add(itinList.get(i));
			}
		}
		
		// Recurse through both lessThan and greaterThan lists to sort them.
		lessThan = sortItinByTime(lessThan);
		greaterThan = sortItinByTime(greaterThan);
		
		// Add the three parts to the final list and return the result.
		finalList.addAll(lessThan);
		finalList.addAll(same);
		finalList.addAll(greaterThan);
		
		return finalList;
	}
	
	/** 
	 * Finds a list of flights with the given date, origin, and destination.
	 * @param date Date of the flight.
	 * @param origin Departure location of the flight.
	 * @param destination Arrival location of the flight.
	 * @return ArrayList containing flights which meet the criteria.
	 */
	public List<Flight> getNeededFlights(String date, String origin,
			String destination) {
		
		// Create an ArrayList to store the flights.
		List<Flight> flightList = new ArrayList<Flight>();
		
		// Find the date of each Flight.
		for (Flight flight : flights) {
			String[] dateTime = flight.getDepartureDT().split(" ");
			
			// If the date, origin and destination of a flight is the same
			// as the given date, origin and destination, add it the the list.
			if ((dateTime[0].equals(date))
					&& (flight.getOrigin().equals(origin))
					&& (flight.getDestination().equals(destination))){
				flightList.add(flight);
			}
		}
		return flightList;
	}
	
	/** 
	 * Sorts a given list of Flights by non-decreasing total cost.
	 * Uses the quicksort algorithm to sort the list in order.
	 * @param flightSortList The list of Flights to be sorted.
	 * @return A list of Flights sorted by cost.
	 */
	public List<Flight> sortFlightsByCost(List<Flight> flightSortList) {
		
		// Base case: if the list has one element or is empty, it is sorted.
		if (flightSortList.size() <= 1){
			return flightSortList;
		}
		
		// Set the pivot to a Flight in the middle of the list.
		Flight pivot = flightSortList.get((int)(flightSortList.size() / 2));
		
		// Create lists for flights less than, greater than and equal
		// to the pivot in total cost.
		List<Flight> lessThan = new ArrayList<Flight>();
		List<Flight> greaterThan = new ArrayList<Flight>();
		List<Flight> same = new ArrayList<Flight>();
		
		// Create a list to store the final sorted list of flights.
		List<Flight> finalList = new ArrayList<Flight>();
		
		for (int i = 0; i < flightSortList.size(); i++){
			
			// If a Flight's cost is greater than the pivot, 
			// add it to the greaterThan list.
			if (flightSortList.get(i).getCost() > pivot.getCost()){
				greaterThan.add(flightSortList.get(i));
			
			// If a Flight's cost is less than the pivot, 
			// add it to the greaterThan list.
			} else if(flightSortList.get(i).getCost() < pivot.getCost()){
				lessThan.add(flightSortList.get(i));
				
			// If an itinerary's cost is equal to the pivot, 
			// add it to the same list.
			} else {
				same.add(flightSortList.get(i));
			}
		}
		
		// Recurse through both lessThan and greaterThan lists to sort them.
		lessThan = sortFlightsByCost(lessThan);
		greaterThan = sortFlightsByCost(greaterThan);
		
		// Add the three parts to the final list and return the result.
		finalList.addAll(lessThan);
		finalList.addAll(same);
		finalList.addAll(greaterThan);
		
		return finalList;
	}
	
	/** 
	 * Sorts a given list of Flights by non-decreasing total duration.
	 * Uses the quicksort algorithm to sort the list in order.
	 * @param flightSortList The list of Flights to be sorted.
	 * @return A list of Flights sorted by duration.
	 */
	public List<Flight> sortFlightsByTime(List<Flight> flightSortList) {
		
		// Base case: if the list has one element or is empty, it is sorted.
		if (flightSortList.size() <= 1){
			return flightSortList;
		}
		
		// Set the pivot to a Flight in the middle of the list.
		Flight pivot = flightSortList.get((int)(flightSortList.size() / 2));
		
		// Create lists for flights less than, greater than and equal
		// to the pivot in total duration.
		List<Flight> lessThan = new ArrayList<Flight>();
		List<Flight> greaterThan = new ArrayList<Flight>();
		List<Flight> same = new ArrayList<Flight>();
		
		// Create a list to store the final sorted list of flights.
		List<Flight> finalList = new ArrayList<Flight>();
		
		for (int i = 0; i < flightSortList.size(); i++){
			
			// If a Flight's duration is longer than the pivot, 
			// add it to the greaterThan list.
			if (flightSortList.get(i).getDuration() > pivot.getDuration()){
				greaterThan.add(flightSortList.get(i));
			
			// If a Flight's duration is shorter than the pivot, 
			// add it to the greaterThan list.
			} else if(flightSortList.get(i).getDuration() < pivot.getDuration()){
				lessThan.add(flightSortList.get(i));
				
			// If an itinerary's duration is equal to the pivot, 
			// add it to the same list.
			} else {
				same.add(flightSortList.get(i));
			}
		}
		
		// Recurse through both lessThan and greaterThan lists to sort them.
		lessThan = sortFlightsByTime(lessThan);
		greaterThan = sortFlightsByTime(greaterThan);
		
		// Add the three parts to the final list and return the result.
		finalList.addAll(lessThan);
		finalList.addAll(same);
		finalList.addAll(greaterThan);
		
		return finalList;
	}
	
	/**
	 * Reads a csv file and saves all client data found in the file inside
	 * an ArrayList.
	 * @param path The path of the file to read.
	 */
	public void uploadToClientFile(String path, String lastName, 
			String firstName, String email, String address, 
			String CCnumber, String expiryDate) {
			
		String[] newClient = {lastName, firstName, email, address, CCnumber, expiryDate};
		
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path, true)));
		    out.println(newClient.toString().substring(1, newClient.toString().length() - 1));
		    out.close();
		    
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Reads a password.txt file and stores it in this database object.
	 * @param path The path of the file to read.
	 */
	public void readUserPass(Object path) {
		
		// Create a new list of login strings.
		userLogins = new ArrayList<String[]>();
		
		// Read the file.
		try {
			BufferedReader passwordFile = null;
			
			if (path instanceof String) {
				passwordFile = new BufferedReader(new FileReader((String) path));
				
			} else if (path instanceof InputStreamReader) {
				passwordFile = new BufferedReader((InputStreamReader) path);
			}
			
			String lineOfText;
			
			while ((lineOfText = passwordFile.readLine()) != null) {
				
				if (lineOfText.length() > 0) {
					
					// Split the line by the commas to get each data field.
					String[] userInfo = lineOfText.split(",");
					
					userLogins.add(userInfo);
				}
			}
			passwordFile.close();
			
		// Print an error message if the file cannot be found.
		} catch (IOException fileError) {
			System.out.println(fileError);
		}
	}
	
	/**
	 * Gets a list of user logins.
	 * @return The list of user logins, in an string array.
	 */
	public List<String[]> getUserLogins() {
		return userLogins;
	}
	
	/**
	 * Gets the password of a given user email.
	 * @param email The email of the user to find the password of.
	 * @return The needed password.
	 */
	public String getUserPassword(String email) {
		// Check the stored list of logins until the right one is found.
		for (String[] s : getUserLogins()) {
			if (s[0].equals(email)) {
				return s[1];
			}
		}
		return "";
	}
	
	/**
	 * Checks to see if an email exists in this database.
	 * @param email The email to check.
	 * @return True if it exists, False if not.
	 */
	public boolean checkEmailExists(String email) {
		
		for (Client client : getClientList()) {
			if (client.getEmail().equals(email)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks to see if at least one admin exists in this database.
	 * @return True if one admin is found, false otherwise.
	 */
	public boolean checkAdminExists() {
		
		if (getUserLogins().size() > 0) {
			for (String[] login : getUserLogins()) {
				if (login[2].equals(Constants.ADMIN_STRING)) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}
}
