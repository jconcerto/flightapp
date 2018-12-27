package com.flightapp.androidproject;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bookingApp.Client;
import bookingApp.Database;
import bookingApp.Flight;
import bookingApp.Itinerary;
import adapters.DetailAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Displays more detailed information about each flight in an
 * Itinerary result. When a user clicks on an Itinerary from the
 * result activity, this activity is shown. In addition to all
 * the flights inside this Itinerary, it also displays the total
 * cost and duration of all flights contained.
 * 
 */
public class DisplayResultActivity extends NavDrawerActivity {

	private Database startDb;
	private String username;
	private String password;
	private String numSeats;
	private Client thisClient;
	private ListView resultList;
	private DetailAdapter resultAdapt;
	private Itinerary singleResult;
	private String accountType;
	private Map<Itinerary, Integer> bookingMap;
	
	/* (non-Javadoc)
	 * @see com.flightapp.androidproject.NavDrawerActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Gets the last intent and it's information.
		Intent intent = getIntent();
		startDb = (Database) intent.getSerializableExtra(Constants.DATABASE);
		username = intent.getStringExtra(Constants.USERNAME);
		password = intent.getStringExtra(Constants.PASSWORD);
		numSeats = intent.getStringExtra(Constants.NUMBER_OF_SEATS);
		singleResult = (Itinerary) intent.getSerializableExtra(Constants.SINGLE_RESULT);
		thisClient = (Client) intent.getSerializableExtra(Constants.CLIENT_OBJECT);
		accountType = intent.getStringExtra(Constants.ADMIN_OR_CLIENT);
		
		// Set the drawer to display user info.
		TextView drawerName = (TextView) findViewById(R.id.drawer_user_name);
		drawerName.setText(thisClient.getFirstName() + " " + thisClient.getLastName());
		
		TextView drawerEmail = (TextView) findViewById(R.id.drawer_user_email);
		drawerEmail.setText(thisClient.getEmail());
		
		// Go to this activity's view
		ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
		getLayoutInflater().inflate(R.layout.activity_display_result, content, true);
		
		// Gets the list of flights to display.
		List<Flight> flightArray = singleResult.getFlightArray();
		resultList = (ListView) findViewById(R.id.summary_result_list);
		
		// Sets an adapter to the listview to dynamically generate results.
		resultAdapt = new DetailAdapter(this, flightArray);
		resultList.setAdapter(resultAdapt);
		
		// Set TextView text to display relevent information.
		TextView fromToText = (TextView) findViewById(R.id.info_from_to);
		fromToText.setText(singleResult.getOrigin() + " to " + singleResult.getDestination());
		
		TextView departOn = (TextView) findViewById(R.id.info_depart_date);
		departOn.setText("Departing on " + singleResult.getDepartDT());
		
		TextView itinCost = (TextView) findViewById(R.id.total_itin_cost);
		itinCost.setText("$" + singleResult.getCostString());
		
		TextView numPerson = (TextView) findViewById(R.id.total_num_person);
		numPerson.setText("Booking for " + numSeats + " person(s)");
		
		TextView entireCost = (TextView) findViewById(R.id.total_entire_cost);
		
		double costValue = 0;
		double singleCost = singleResult.getTotalCost();
		for (int i = 0; i < Integer.parseInt(numSeats); i++) {
			costValue += singleCost;
		}
		entireCost.setText("$" + String.format("%.2f", costValue));
	}
	
	/**
	 * Books the Itinerary being displayed.
	 * @param view The button which submits the booking on click.
	 */
	public void onClickBookItin(View view) {
		
		// Check available seats.
		if (!singleResult.checkSeats(Integer.parseInt(numSeats))) {
			Toast.makeText(getApplicationContext(), "Cannot book itinerary: not enough seats", Toast.LENGTH_SHORT).show();
		} else {
			
			// Decrease the number of seats in each flight.
			singleResult.bookSeats(Integer.parseInt(numSeats));
			
			try {
				
				// Add the booking to the Client object.
				thisClient.bookItinSeats(singleResult, Integer.parseInt(numSeats));
				
				updateBooking(singleResult, Integer.parseInt(numSeats));
				
				// Update the number of seats in the stored flight file.
				FileOutputStream flightsStream = openFileOutput(Constants.FLIGHT_FILE, MODE_PRIVATE);
				OutputStreamWriter oWriter = new OutputStreamWriter(flightsStream);
				BufferedWriter bWriter = new BufferedWriter(oWriter);
				
				// Writes the new flight data to the file.
				for (Flight f : startDb.getFlightList()) {
					if (checkSameFlight(singleResult, f)) {
						bWriter.write("\r\n" + (f.toString()).substring(0, f.toString().lastIndexOf(",") + 1) 
								+ (f.getNumSeats() - Integer.parseInt(numSeats)));
					} else {
						bWriter.write("\r\n" + f.toString());
						}
					}
				
				bWriter.close();
				oWriter.close();
				flightsStream.close();
				Toast.makeText(getApplicationContext(), "Itinerary booked!", Toast.LENGTH_SHORT).show();

				// Go to view bookings activity.
		    	Intent intent = new Intent(this, ViewBookings.class);
		    	intent.putExtra(Constants.USERNAME, username);
		    	intent.putExtra(Constants.PASSWORD, password);
				intent.putExtra(Constants.DATABASE, startDb);
				intent.putExtra(Constants.CLIENT_OBJECT, thisClient);
				intent.putExtra(Constants.ADMIN_OR_CLIENT, accountType);
				Intent intent2 = new Intent();
				intent2.putExtra(Constants.DATABASE, startDb);
				intent2.putExtra(Constants.USERNAME, username);
				intent2.putExtra(Constants.PASSWORD, password);
				intent2.putExtra(Constants.CLIENT_OBJECT, thisClient);
				intent2.putExtra(Constants.ADMIN_OR_CLIENT, accountType);
				setResult(RESULT_OK, intent2);
				startActivity(intent);
				finish();
				
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(), "Could not write to booking file", Toast.LENGTH_SHORT).show();
			}
			
		}
	}
	
    /* (non-Javadoc)
     * @see com.flightapp.androidproject.NavDrawerActivity#onItemClick(android.widget.AdapterView, android.view.View, int, long)
     */
    @Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
    	
    	// Navigation drawer links.
    	if (position == 1) {
	    	Intent intent = new Intent(this, UserInfoPage.class);
	    	intent.putExtra(Constants.USERNAME, username);
	    	intent.putExtra(Constants.PASSWORD, password);
			intent.putExtra(Constants.DATABASE, startDb);
			intent.putExtra(Constants.CLIENT_OBJECT, thisClient);
			intent.putExtra(Constants.ADMIN_OR_CLIENT, accountType);
			startActivity(intent);
		}
		else if (position == 2) {
	    	Intent intent = new Intent(this, ClientOptions.class);
	    	intent.putExtra(Constants.USERNAME, username);
	    	intent.putExtra(Constants.PASSWORD, password);
			intent.putExtra(Constants.DATABASE, startDb);
			intent.putExtra(Constants.CLIENT_OBJECT, thisClient);
			intent.putExtra(Constants.ADMIN_OR_CLIENT, accountType);
			startActivity(intent);
		}
		else if (position == 3) {
	    	Intent intent = new Intent(this, ViewBookings.class);
	    	intent.putExtra(Constants.USERNAME, username);
	    	intent.putExtra(Constants.PASSWORD, password);
			intent.putExtra(Constants.DATABASE, startDb);
			intent.putExtra(Constants.CLIENT_OBJECT, thisClient);
			intent.putExtra(Constants.ADMIN_OR_CLIENT, accountType);
			startActivity(intent);
		}
		else if (position == 4) {
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra(Constants.DATABASE, startDb);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
		selectItem(position);
	}
    
    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
    	// Updates the data in this activity when a change is made.
        if (requestCode == 1) {
             if(resultCode == RESULT_OK){
              startDb = (Database) data.getSerializableExtra(Constants.DATABASE);
              thisClient = (Client) data.getSerializableExtra(Constants.CLIENT_OBJECT);
              username = data.getStringExtra(Constants.USERNAME);
              password = data.getStringExtra(Constants.PASSWORD);
              accountType = data.getStringExtra(Constants.ADMIN_OR_CLIENT);
              }
        }
    }
    
    /**
     * Checks to see if a itinerary contains a given flight.
     * @param itinerary The itinerary to check.
     * @param flight The flight to check.
     * @return True if the itinerary contains the flight, False otherwise.
     */
    private boolean checkSameFlight(Itinerary itinerary, Flight flight) {
    	
    	List<Flight> itinList = itinerary.getFlightArray();
    	for (Flight f : itinList) {
    		if (f.getFlightNumber().equals(flight.getFlightNumber())) {
    			return true;
    			}
    		}
    	return false;
    }
    
	/**
	 * Creates a booking file to store the client's bookings into internal storage.
	 * @param bookMap The Map of Itnerary and number of seats to book for
	 * the client.
	 */
	@SuppressWarnings("unchecked")
	public void updateBooking(Itinerary itin, int numSeats) {
    	
		try {
			FileInputStream file = openFileInput(username + Constants.BOOKING_FILE);
			ObjectInputStream input = new ObjectInputStream(file);
			
			bookingMap = (HashMap<Itinerary, Integer>) input.readObject();
			
		} catch (StreamCorruptedException e) {
			bookingMap = new HashMap<Itinerary, Integer>();
		} catch (IOException e) {
			bookingMap = new HashMap<Itinerary, Integer>();
		} catch (ClassNotFoundException e) {
			bookingMap = new HashMap<Itinerary, Integer>();
		}
		
		try {
			// Write the Map to the booking file.
			if (bookingMap.get(itin) == null) {
				bookingMap.put(itin, numSeats);
			} else {
				int curSeats = bookingMap.get(itin);
				bookingMap.put(itin, curSeats + numSeats);
			}
			
			FileOutputStream bookingsStream = openFileOutput(username + Constants.BOOKING_FILE, MODE_PRIVATE);
		   	ObjectOutputStream bookingsWriter = new ObjectOutputStream(bookingsStream);
		   	bookingsWriter.writeObject(bookingMap);
		   	
		   	bookingsWriter.close();
		   	bookingsWriter.close();
		   	bookingsStream.close();
		} catch (FileNotFoundException e) {
			Toast.makeText(getApplicationContext(), "Booking file not found!",Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), "Could not book Itinerary!",Toast.LENGTH_SHORT).show();
		}
    }
}
