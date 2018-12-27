package com.flightapp.androidproject;

import java.util.ArrayList;
import java.util.List;

import bookingApp.Client;
import bookingApp.Database;
import bookingApp.Itinerary;

import adapters.ResultAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Displays results of a given flight search. Also allows a user
 * to sort their results by increasing time or cost.
 *
 */
public class Results extends NavDrawerActivity {
	
	
	private Database startDb;
	private boolean flightOrItin;
	private String username;
	private String numSeats;
	private String origin;
	private String destination;
	private String date;
	private ListView resultList;
	private List<Itinerary> validResults;
	private ResultAdapter resultAdapt;
	private Client thisClient;
	private String password;
	private String accountType;
	
	
	/* (non-Javadoc)
	 * @see com.flightapp.androidproject.NavDrawerActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get information from last activity
		Intent intent = getIntent();
		startDb = (Database) intent.getSerializableExtra(Constants.DATABASE);
		username = intent.getStringExtra(Constants.USERNAME);
		password = intent.getStringExtra(Constants.PASSWORD);
		flightOrItin = intent.getBooleanExtra(Constants.RESULTS, false);
		origin = intent.getStringExtra(Constants.FROM);
		destination = intent.getStringExtra(Constants.TO);
		date = intent.getStringExtra(Constants.DATE);
		numSeats = intent.getStringExtra(Constants.NUMBER_OF_SEATS);
		thisClient = (Client) intent.getSerializableExtra(Constants.CLIENT_OBJECT);
		accountType = intent.getStringExtra(Constants.ADMIN_OR_CLIENT);
		
		// Set the drawer to display user info.
		TextView drawerName = (TextView) findViewById(R.id.drawer_user_name);
		drawerName.setText(thisClient.getFirstName() + " " + thisClient.getLastName());
		
		TextView drawerEmail = (TextView) findViewById(R.id.drawer_user_email);
		drawerEmail.setText(thisClient.getEmail());
		
		
		// Go to this activity's view
		ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
		getLayoutInflater().inflate(R.layout.activity_results, content, true);
		
		List<Itinerary> allResults = getResults();
		validResults = seatsAvailable(allResults, Integer.parseInt(numSeats));
		validResults = startDb.sortItinByTime(validResults);
		
		// Create an adapter to display search results.
		resultList = (ListView) findViewById(R.id.results_listview);
		resultAdapt = new ResultAdapter(this, validResults);
		resultList.setAdapter(resultAdapt);
		
		// If a result is clicked, go to it's summary page.
		resultList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				Intent nextIntent = new Intent(Results.this, DisplayResultActivity.class);
				nextIntent.putExtra(Constants.DATABASE, startDb);
				nextIntent.putExtra(Constants.USERNAME, username);
				nextIntent.putExtra(Constants.PASSWORD, password);
				nextIntent.putExtra(Constants.CLIENT_OBJECT, thisClient);
				nextIntent.putExtra(Constants.SINGLE_RESULT, validResults.get(position));
				nextIntent.putExtra(Constants.NUMBER_OF_SEATS, numSeats);
				nextIntent.putExtra(Constants.ADMIN_OR_CLIENT, accountType);
    			Intent intent = new Intent();
    			intent.putExtra(Constants.DATABASE, startDb);
    			intent.putExtra(Constants.USERNAME, username);
    			intent.putExtra(Constants.PASSWORD, password);
    			intent.putExtra(Constants.CLIENT_OBJECT, thisClient);
				intent.putExtra(Constants.ADMIN_OR_CLIENT, accountType);
    			setResult(RESULT_OK, intent);
				startActivity(nextIntent);
				finish();
			}
		});
		
		// The button which sorts results.
		RadioGroup sortSwitch = (RadioGroup) findViewById(R.id.sort_switches);
		
		// If no results are found, display a message to the user.
		if (!(validResults.size() > 0)) {
			TextView noResultText = (TextView) findViewById(R.id.no_result_textview);
			noResultText.setVisibility(View.VISIBLE);
			sortSwitch.setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	 * Gets a list of search results.
	 * @return The list of search results.
	 */
	public List<Itinerary> getResults() {
		
		// Get all itineraries from database
		List<Itinerary> itinResults = startDb.getItineraries(date, origin, destination);
		
		// Find only the valid itineraries and return a list of them.
		if (flightOrItin) {
			List<Itinerary> singleFlights = new ArrayList<Itinerary>();
			
			for (Itinerary i : itinResults) {
				
				if (i.getFlightArray().size() == 1) {
					singleFlights.add(i);
				}
			}
			itinResults = singleFlights;	
		}
		
		return itinResults;
	}
	
	/**
	 * Sorts the results by time.
	 */
	public void sortTime() {
		
		// Use database methods for this.
		validResults = startDb.sortItinByTime(validResults);
		resultAdapt.updateResults(validResults);
	}
	
	/**
	 * Sorts the results by cost.
	 */
	public void sortCost() {
		
		// Use database methods for this.
		validResults = startDb.sortItinByCost(validResults);
		resultAdapt.updateResults(validResults);
	}
	
	/**
	 * Does a sort by time or cost on click of the button.
	 * @param view The button which was clicked.
	 */
	public void onSortSwitchClick(View view) {
		
		// Check if the button was clicked.
		boolean checked = ((RadioButton) view).isChecked();
		
        switch(view.getId()) {
        
        // Sort by time.
        case R.id.sort_time_switch:
            if (checked)
            	sortTime();
            break;
        
        // Sort by cost
        case R.id.sort_cost_switch:
            if (checked)
            	sortCost();
            break;
        }
	}
	
    /* (non-Javadoc)
     * @see com.flightapp.androidproject.NavDrawerActivity#onItemClick(android.widget.AdapterView, android.view.View, int, long)
     */
    @Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
    	
    	// Drawer list items.
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
    	
    	// Passes back information if any data is changed.
        if (requestCode == 1) {
             if(resultCode == RESULT_OK){
              startDb = (Database) data.getSerializableExtra(Constants.DATABASE);
              thisClient = (Client) data.getSerializableExtra(Constants.CLIENT_OBJECT);
              username = data.getStringExtra(Constants.USERNAME);
              password = data.getStringExtra(Constants.PASSWORD);
              accountType = data.getStringExtra(Constants.ADMIN_OR_CLIENT);
              
              // Resets the results to display new search results if a 
              // flight is changed.
              List<Itinerary> allResults = getResults();
              validResults = seatsAvailable(allResults, Integer.parseInt(numSeats));
              sortTime();
              ((RadioButton) findViewById(R.id.sort_time_switch)).setChecked(true);
              ((RadioButton) findViewById(R.id.sort_time_switch)).setChecked(false);
              }
        }
    }
    
    /**
     * Returns a modified list of flight results which have the available number of
     * seats.
     * @param resultList The list of results.
     * @param numSeats The number of seats to book.
     * @return A list of flight results which contain at least the number of
     * required seats.
     */
    public List<Itinerary> seatsAvailable(List<Itinerary> resultList, int numSeats) {
    	
    	// Create a new list of Itineraries
    	List<Itinerary> validList = new ArrayList<Itinerary>(); 
    	
    	// Add to our list if the itinerary only contains one flight.
    	for (Itinerary i : resultList) {
    		if (i.checkSeats(numSeats)) {
    			validList.add(i);
    		}
    	}
    	return validList;
    	
    }
	
}
