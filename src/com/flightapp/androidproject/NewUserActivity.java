package com.flightapp.androidproject;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import bookingApp.Client;
import bookingApp.Database;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

/**
 * An activity which allows one to create a new user account.
 * Saves the new user data into a password file and client file.
 *
 */
public class NewUserActivity extends NavDrawerActivity {
	
	Calendar myCalendar = Calendar.getInstance();

	private Database startDb;
	private Button dateButton;
	private String dateString;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat dateFormatter2;
    private EditText dayBox;
    private EditText monthBox;
    private EditText yearBox;
    private List<String> emailList;

	/* (non-Javadoc)
	 * @see com.flightapp.androidproject.NavDrawerActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_user);
		
		// Disable sound effects
		findViewById(R.id.background).setSoundEffectsEnabled(false);
		findViewById(R.id.header_layout).setSoundEffectsEnabled(false);
		findViewById(R.id.new_user_background).setSoundEffectsEnabled(false);
		
		Intent intent = getIntent();
		startDb = (Database) intent.getSerializableExtra(Constants.DATABASE);
		
		// Need a list of emails already existing on file.
		emailList = new ArrayList<String>();
		
		// Read client csv file
		try {
			FileInputStream clientStream = openFileInput(Constants.CLIENT_FILE);
			InputStreamReader clientReader = new InputStreamReader(clientStream);
			startDb.readClients(clientReader);
			
			for (Client c : startDb.getClientList()) {
				emailList.add(c.getEmail());
			}
			clientReader.close();
		    clientStream.close();
		    
		} catch (IOException e) {
    		Toast.makeText(getApplicationContext(), "Client csv file could not be read", Toast.LENGTH_SHORT).show();
		}
		
		// Variables for the date picker button.
		dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        dateFormatter2 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        
        dayBox = (EditText) findViewById(R.id.date_box_day);
        monthBox = (EditText) findViewById(R.id.date_box_month);
        yearBox = (EditText) findViewById(R.id.date_box_year);
        
        dateButton = (Button) findViewById(R.id.date_button);
        setDateTimeField();
        
	}

	/* (non-Javadoc)
	 * @see com.flightapp.androidproject.NavDrawerActivity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_user, menu);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.flightapp.androidproject.NavDrawerActivity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    /**
     * Sets the date and time field when the date picker
     * button is clicked.
     */
    private void setDateTimeField() {
    	
    	// Get a calendar instance to find the date.
    	Calendar newCalendar = Calendar.getInstance();
    	
    	// Listen if the date button was clicked and set the date
    	// if it was.
        datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
 
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                
            	// Set the date inside the EditText boxes.
            	Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String[] fullDateString = dateFormatter.format(newDate.getTime()).split("-");
                dayBox.setText(fullDateString[0]);
                monthBox.setText(fullDateString[1]);
                yearBox.setText(fullDateString[2]);
                
                dateString = (dateFormatter2.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
	
    
    /**
     * Shows the date picker when the calendar button is clicked.
     * @param view The view which was clicked.
     */
    public void onDateButtonClick(View view) {
    	
    	datePickerDialog.show();
    }
    
    /**
     * Creates a new user account from the given data.
     * @param view The button which clicks submit.
     */
    public void submitNewUser(View view) {
    	
    	// Get EditText data
    	EditText emailText = (EditText) findViewById(R.id.create_email);
    	String emailString = emailText.getText().toString();
    	
    	EditText passwordText = (EditText) findViewById(R.id.create_password);
    	String passwordString = passwordText.getText().toString();
    	
    	EditText firstNameText = (EditText) findViewById(R.id.create_first_name);
    	String firstNameString = firstNameText.getText().toString();
    	
    	EditText lastNameText = (EditText) findViewById(R.id.create_last_name);
    	String lastNameString = lastNameText.getText().toString();
    	
    	EditText addressText = (EditText) findViewById(R.id.create_address);
    	String addressString = addressText.getText().toString();
    	
    	EditText ccNumText = (EditText) findViewById(R.id.create_cc_num);
    	String ccNumString = ccNumText.getText().toString();
    	
    	String dayString = dayBox.getText().toString();
    	String monthString = monthBox.getText().toString();
    	String yearString = yearBox.getText().toString();
    	
    	// Check if all fields are valid.
    	if (!(emailString.length() > 0)) {
    		Toast.makeText(getApplicationContext(), "Please enter an email.", Toast.LENGTH_SHORT).show();
    	} else if (!(passwordString.length() > 0)) {
    		Toast.makeText(getApplicationContext(), "Please enter a password.", Toast.LENGTH_SHORT).show();
    	} else if (!(firstNameString.length() > 0)) {
    		Toast.makeText(getApplicationContext(), "Please enter your first name.", Toast.LENGTH_SHORT).show();
    	} else if (!(lastNameString.length() > 0)) {
    		Toast.makeText(getApplicationContext(), "Please enter your last name.", Toast.LENGTH_SHORT).show();
    	} else if (!(addressString.length() > 0)) {
    		Toast.makeText(getApplicationContext(), "Please enter your address.", Toast.LENGTH_SHORT).show();
    	} else if (!(ccNumString.length() > 0)) {
    		Toast.makeText(getApplicationContext(), "Please enter a credit card number.", Toast.LENGTH_SHORT).show();
    	} else if (!(dayString.length() > 0) ||
    			!(monthString.length() > 0) ||
    			!(yearString.length() > 0)) {
    		Toast.makeText(getApplicationContext(), "Please enter a credit card expiry date", Toast.LENGTH_SHORT).show();
    	} else if ((Integer.parseInt(dayString) < 1) ||
    			(Integer.parseInt(dayString) > 31) ||
    			(Integer.parseInt(monthString) < 1) ||
    			(Integer.parseInt(monthString) > 12)) {
    		Toast.makeText(getApplicationContext(), "Invalid date", Toast.LENGTH_SHORT).show();
    	} else {
    		
    		// A person cannot create a new account with an existing email.
    		if (emailList.contains(emailString)) {
        		Toast.makeText(getApplicationContext(), "Email already exists, please enter another email.", Toast.LENGTH_SHORT).show();
    		
    		} else {
    			
    			// Adds the user's info to the database by creating a string.
        		String newUserString = (lastNameString + "," + firstNameString 
        				+ "," + emailString + "," + addressString + "," 
        				+ ccNumString + "," + yearString + "-" + monthString 
        				+ "-" + dayString);
        		
        		String newLoginString = (emailString + "," + passwordString + ","
        				+ "Client");
        		
    			try {
    				// Writes it into clientfile.csv file
    				FileOutputStream clientStream = openFileOutput(
    						Constants.CLIENT_FILE, MODE_APPEND);
    				OutputStreamWriter clientWriter = new OutputStreamWriter(
    						clientStream);
    				BufferedWriter bwriter = new BufferedWriter(clientWriter);
    				bwriter.write("\r\n" + newUserString);
    				bwriter.close();
    				clientWriter.close();
    				clientStream.close();
    				
    				try {
    					// Writes it into password.txt file
    					FileOutputStream passwordStream = openFileOutput(
    							Constants.PASSWORD_FILE, MODE_APPEND);
    					OutputStreamWriter passwordWriter = new OutputStreamWriter(
    							passwordStream);
    					BufferedWriter bwriter2 = new BufferedWriter(passwordWriter);
    					bwriter2.write("\r\n" + newLoginString);
    					bwriter2.close();
    					passwordWriter.close();
    					passwordStream.close();
    					
    					Toast.makeText(getApplicationContext(), "Account Created",
    							Toast.LENGTH_SHORT).show();
    					finish();
    					
    				} catch (IOException e) {
    		    		Toast.makeText(getApplicationContext(), "Could not write to password.txt", Toast.LENGTH_SHORT).show();
    				}
    				
    			} catch (IOException e) {
    	    		Toast.makeText(getApplicationContext(), "Could not write to client file", Toast.LENGTH_SHORT).show();
    			}
    		}
    	}
    }
}
