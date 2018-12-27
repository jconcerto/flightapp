package com.flightapp.androidproject;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import bookingApp.Client;
import bookingApp.Database;
import bookingApp.Flight;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Lets an Admin modify a Flight's information. Will write the
 * changed flight information to a file in internal storage.
 *
 */
public class UpdateFlightActivity extends NavDrawerActivity {

	
	private Database startDb;
	private Flight flight;
	
	private EditText numbOfFlight;
	private EditText airline;
	private EditText origin;
	private EditText destination;
	private EditText departDT;
	private EditText arrivDT;
	private EditText price;
	private EditText numSeats;
	private String numbOfFlightString;
	private String airlineString;
	private String originString;
	private String destinationString;
	private String departDTString;
	private String arrivDTString;
	private String priceString;
	private String numSeatsString;
	
	/* (non-Javadoc)
	 * @see com.flightapp.androidproject.NavDrawerActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get last intent
		Intent intent = getIntent();
		startDb = (Database) intent.getSerializableExtra(Constants.DATABASE);
		flight = (Flight) intent.getSerializableExtra(Constants.FLIGHT_NUM);
		
		ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
		getLayoutInflater().inflate(R.layout.activity_update_flight, content, true);
		
		// Get EditText boxes for this activity.
		numbOfFlight = (EditText) findViewById(R.id.flight_number_edit);
		airline = (EditText) findViewById(R.id.flight_airline_edit);
		origin = (EditText) findViewById(R.id.flight_fromedit);
		destination = (EditText) findViewById(R.id.flight_destedit);
		departDT = (EditText) findViewById(R.id.flight_DepartTedit);
		arrivDT = (EditText) findViewById(R.id.flight_arriveedit);
		price = (EditText) findViewById(R.id.flight_priceedit);
		numSeats = (EditText) findViewById(R.id.flight_numseatedit);
		
		// Set each EditText with its respective flight information.
		setFlightInfo();
	}
	
	/**
	 * Set the EditText boxes with the flight's information.
	 */
	private void setFlightInfo() {
		
		numbOfFlight.setText(flight.getFlightNumber());

		airline.setText(flight.getAirline());

		origin.setText(flight.getOrigin());

		destination.setText(flight.getDestination());

		departDT.setText(flight.getDepartureDT());

		arrivDT.setText(flight.getArrivalDT());
		
		price.setText(flight.getPriceString());
		
		numSeats.setText("" + flight.getNumSeats());
		
	}
	
	/**
	 * Sets the Flight object to contain the correct information.
	 */
	private void setFlightObject() {
		
		flight.setAirline(airline.getText().toString());
		flight.setFlightNumber(numbOfFlight.getText().toString());
		flight.setOrigin(origin.getText().toString());
		flight.setDestination(destination.getText().toString());
		flight.setDepartureDT(departDT.getText().toString());
		flight.setArrivalDT(arrivDT.getText().toString());
		flight.setCost(Double.parseDouble(price.getText().toString()));
		flight.setNumSeats(Integer.parseInt(numSeats.getText().toString()));
		
	}
	
	/**
	 * Submits a change in flight information if one was made.
	 * @param view The view which was clicked.
	 */
	public void submitNewFlightInfo(View view) {
		
		// Get the string of each EditText box.
		numbOfFlightString = numbOfFlight.getText().toString();
		airlineString = airline.getText().toString();
		originString = origin.getText().toString();
		destinationString = destination.getText().toString();
		departDTString = departDT.getText().toString();
		arrivDTString = arrivDT.getText().toString();
		priceString = price.getText().toString();
		numSeatsString = numSeats.getText().toString();
		
		// Checks to see if EditTexes are valid.
    	if (!(numbOfFlightString.length() > 0)) {
    		Toast.makeText(getApplicationContext(), "Please enter a flight number", Toast.LENGTH_SHORT).show();
    	} else if (!(airlineString.length() > 0)) {
    		Toast.makeText(getApplicationContext(), "Please enter an airline", Toast.LENGTH_SHORT).show();
    	} else if (!(originString.length() > 0)) {
    		Toast.makeText(getApplicationContext(), "Please enter an origin", Toast.LENGTH_SHORT).show();
    	} else if (!(destinationString.length() > 0)) {
    		Toast.makeText(getApplicationContext(), "Please enter a destination.", Toast.LENGTH_SHORT).show();
    	} else if (!(departDTString.length() > 0)) {
    		Toast.makeText(getApplicationContext(), "Please enter a departure date and time", Toast.LENGTH_SHORT).show();
    	} else if (!(arrivDTString.length() > 0)) {
    		Toast.makeText(getApplicationContext(), "Please enter an arrival date and time", Toast.LENGTH_SHORT).show();
    	} else if (!(priceString.length() > 0)) {
    		Toast.makeText(getApplicationContext(), "Please enter a price", Toast.LENGTH_SHORT).show();
    	} else if (!(numSeatsString.length() > 0)) {
    		Toast.makeText(getApplicationContext(), "Please enter the number of seats", Toast.LENGTH_SHORT).show();
    	} else {
    		
    		// Change the flight's info.
    		setFlightObject();
    		setFlightInfo();
    		
    		try {
    			// Writes the new flight to file.
    			FileOutputStream flightStream = openFileOutput(Constants.FLIGHT_FILE, MODE_PRIVATE);
    			OutputStreamWriter flightWriter = new OutputStreamWriter(flightStream);
    			BufferedWriter bWriter = new BufferedWriter(flightWriter);
    			for (Flight f : startDb.getFlightList()) {
    				if (f.getFlightNumber().equals(flight.getFlightNumber())) {
        				bWriter.write("\r\n" + flight.toString());
        				f = flight;
    				} else {
    					bWriter.write("\r\n" + f.toString());
    				}
    			}

				bWriter.close();
    			flightWriter.close();
    			flightStream.close();
    			
    			// Update the database
    			FileInputStream flightsStream = openFileInput(Constants.FLIGHT_FILE);
    			InputStreamReader flightReader = new InputStreamReader(flightsStream);
    			startDb.readFlights(flightReader);
    			flightsStream.close();
    			flightReader.close();
    			
    			// Go back to Admin Options.
    			Intent intent = new Intent();
    			intent.putExtra(Constants.DATABASE, startDb);
    			setResult(RESULT_OK, intent);
    			Intent intent2 = new Intent(this, AdminOptions.class);
    			intent2.putExtra(Constants.DATABASE, startDb);
    			startActivity(intent2);
    			finish();
        		Toast.makeText(getApplicationContext(), "Changes submitted.", Toast.LENGTH_SHORT).show();
    		} catch (FileNotFoundException e) {
        		Toast.makeText(getApplicationContext(), "Could not write to flight file.", Toast.LENGTH_SHORT).show();
        		
    		} catch (IOException e) {
        		Toast.makeText(getApplicationContext(), "Could not write to flight file.", Toast.LENGTH_SHORT).show();
			}
    	}
    	
	}
	/**
	 * Resets the flight information to what it was originally
	 * and returns to the admin page.
	 * @param view The view which was clicked.
	 */
	public void cancelFlightInfo(View view) {
		setFlightInfo();
		Intent intent = new Intent(this, AdminOptions.class);
		intent.putExtra(Constants.DATABASE, startDb);
		startActivity(intent);
		finish();
	}

}
