package com.flightapp.androidproject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import bookingApp.Client;
import bookingApp.Database;
import bookingApp.Flight;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * An activity which contains an Admin's options.
 */
public class AdminOptions extends NavDrawerActivity {
	
    private Database startDb;
    private Intent intent;
    private View button;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_options);
		
		// Get the database from intent.
		intent = getIntent();
		startDb = (Database) intent.getSerializableExtra(Constants.DATABASE);
	}
	
	/**
	 * Uploads a client file into the database.
	 * @param view The button object which uploads on click.
	 */
	public void uploadClients(View view){
		
		try {
			// Reads the clients file and stores in database
			FileInputStream clientsStream = openFileInput(Constants.CLIENT_FILE);
			InputStreamReader clientReader = new InputStreamReader(clientsStream);
			startDb.readClients(clientReader);
			clientsStream.close();
			clientReader.close();
			
			// If it is successful:
			Toast.makeText(getApplicationContext(), "Uploaded clients!",
					Toast.LENGTH_SHORT).show();
			
		} catch (IOException e) {
			// If upload fails:
			Toast.makeText(getApplicationContext(), "Could not upload clients to application.",
					Toast.LENGTH_SHORT).show();
		}
		
	}
	
	/**
	 * Uploads a flight file into the database.
	 * @param view The button object which uploads on click.
	 */
	public void uploadFlights(View view){
		
		try {
			// Reads the flights file and stores in database
			FileInputStream flightsStream = openFileInput(Constants.FLIGHT_FILE);
			InputStreamReader flightReader = new InputStreamReader(flightsStream);
			startDb.readFlights(flightReader);
			flightsStream.close();
			flightReader.close();
			
			// If read is successful:
			Toast.makeText(getApplicationContext(), "Uploaded flights!",
					Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			
			// If the upload fails:
			Toast.makeText(getApplicationContext(), "Could not upload flights to application.",
					Toast.LENGTH_SHORT).show();
		}
		
	}
	
	/**
	 * Goes to a Client's account so that an admin may change any stored info
	 * they may wish, such as booked flights and payment information.
	 * @param view The button object which submits this method on click.
	 */
	public void modifyClient(View view){
		
		// Need to get the email of a client from an EditText
		EditText clientEditText = (EditText) findViewById(R.id.ClientEditText);
		String clientUsername = clientEditText.getText().toString();
		if (clientUsername.length() > 0){
			
			if (startDb.getClientList().size() == 0) {
				Toast.makeText(getApplicationContext(), 
						"Please upload a client file first - must be " +
						"named clientfile.csv in internal storage " +
						"directory /files/", Toast.LENGTH_LONG).show();
			} else {
				for (String[] user : startDb.getUserLogins()) {
					if (user[0].equals(clientUsername)) {
						Intent intent2 = new Intent(this, ClientOptions.class);

						// Creates the intent and starts the next activity
						intent2.putExtra(Constants.DATABASE, startDb);
						intent2.putExtra(Constants.USERNAME, clientUsername);
						startActivity(intent2);
						
						Toast.makeText(getApplicationContext(), 
								"You are now logged in as "  + clientUsername,
								Toast.LENGTH_SHORT).show();
						finish();
					}
				}
			}
			
		} else {
			Toast.makeText(getApplicationContext(), 
					"Enter a client email.", Toast.LENGTH_SHORT).show();

		}
	}
	
	/**
	 * Lets an Admin modify a given flight. The admin can change the flight's
	 * information, such as destination, cost and duration.
	 * @param view The button object which submits this method on click.
	 */
	public void modifyFlight(View view){
		
		// Need to get the flight number from an EditText
		EditText flightEditText = (EditText) findViewById(R.id.FlightEditText);
		String flightString = flightEditText.getText().toString();
		if (flightString.length() > 0){
			
			if (startDb.getFlightList().size() == 0) {
				Toast.makeText(getApplicationContext(), 
						"Please upload a flight file first - must be " +
						"named flightfile.csv in internal storage " +
						"directory /files/", Toast.LENGTH_LONG).show();
			} else {
				
				for (Flight f : startDb.getFlightList()) {
					if (f.getFlightNumber().equals(flightString)) {
						// Creates the intent and starts the next activity
						Intent intent2 = new Intent(this, UpdateFlightActivity.class);
						intent2.putExtra(Constants.DATABASE, startDb);
						intent2.putExtra(Constants.FLIGHT_NUM, f);
						startActivity(intent2);
						finish();
						}
					}
				}
			} else {
			Toast.makeText(getApplicationContext(), 
					"Enter a flight number", Toast.LENGTH_SHORT).show();
		}
	}
	
    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
    	// Updates the data in this activity when a change is made.
        if (requestCode == 1) {
             if(resultCode == RESULT_OK) {
              startDb = (Database) data.getSerializableExtra(Constants.DATABASE);
              uploadFlights(button);
              uploadClients(button);
              }
        }
    }
    
    /**
     * Logs the admin out of the application.
     * @param view The button that was clicked.
     */
    public void onClickLogout(View view) {
    	
    	Intent intent3 = new Intent(this, MainActivity.class);
    	intent3.putExtra(Constants.DATABASE, startDb);
    	startActivity(intent3);
    	finish();
    }

}