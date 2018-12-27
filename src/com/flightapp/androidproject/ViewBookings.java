package com.flightapp.androidproject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bookingApp.Client;
import bookingApp.Database;
import bookingApp.Itinerary;
import adapters.DetailExpandAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a Client's bookings.
 * Reads itineraries from a saved file in internal storage,
 * and gets all flight information for a Client's booked flights.
 *
 */
public class ViewBookings extends NavDrawerActivity {
	
	private String bookings;
	private String username;
	private Database startDb;
	private String password;
	private Client thisClient;
	private Map<Itinerary, Integer> bookingMap;
	private ArrayList<Itinerary> bookingList;
	private String accountType;
	
	/* (non-Javadoc)
	 * @see com.flightapp.androidproject.NavDrawerActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get last intent.
		Intent intent = getIntent();
		username = intent.getStringExtra(Constants.USERNAME);
		startDb = (Database) intent.getSerializableExtra(Constants.DATABASE);
		password = intent.getStringExtra(Constants.PASSWORD);
		thisClient = (Client) intent.getSerializableExtra(Constants.CLIENT_OBJECT);
		
		// Find the client's booked itinerary if there exists any.
		bookingMap = thisClient.getBookedItinSeats();
		bookingList = new ArrayList<Itinerary>(bookingMap.keySet());
		accountType = intent.getStringExtra(Constants.ADMIN_OR_CLIENT);
		
		// Set the drawer to display user info.
		TextView drawerName = (TextView) findViewById(R.id.drawer_user_name);
		drawerName.setText(thisClient.getFirstName() + " " + thisClient.getLastName());
		
		TextView drawerEmail = (TextView) findViewById(R.id.drawer_user_email);
		drawerEmail.setText(thisClient.getEmail());
		
		ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
		getLayoutInflater().inflate(R.layout.activity_view_bookings, content, true);
		
		// Read bookings from a file.
		readBookings();
		
		// Create an adapter ListView to display the bookings.
		ExpandableListView expandList = (ExpandableListView) findViewById(R.id.booking_expand_list);
		DetailExpandAdapter expandAdapter = new DetailExpandAdapter(this, bookingList, bookingMap);
		expandList.setAdapter(expandAdapter);
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
		    drawerLayout.closeDrawer(Gravity.LEFT);
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
     * Read the bookings file for a client and gets the information to display.
     */
    @SuppressWarnings("unchecked")
	public void readBookings() {
    	
		try {
			FileInputStream file = openFileInput(username + Constants.BOOKING_FILE);
			ObjectInputStream input = new ObjectInputStream(file);
			
			// Bookings are stored in a map, where each key is an Itinerary
			// and each value is the number of booked seats.
			bookingMap = (HashMap<Itinerary, Integer>) input.readObject();
			bookingList = new ArrayList<Itinerary>(bookingMap.keySet());
			
		} catch (StreamCorruptedException e) {
			Toast.makeText(getApplicationContext(), "Could not read booking file.", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), "Booking file has the wrong format name.", Toast.LENGTH_SHORT).show();
		} catch (ClassNotFoundException e) {
			Toast.makeText(getApplicationContext(), "No bookings found.", Toast.LENGTH_SHORT).show();
		}
    	
    }
	
	
	
}