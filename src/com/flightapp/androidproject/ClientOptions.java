package com.flightapp.androidproject;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import bookingApp.Client;
import bookingApp.Database;
import android.text.InputType;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An activity which displays a Client's options page. They are able to search
 * and book flights, change their own information, and view booked flights.
 *
 */
public class ClientOptions extends NavDrawerActivity {
	
	Calendar myCalendar = Calendar.getInstance();
	
    private EditText dateText;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat dateFormatter2;
    
    private Database startDb;
    private String dateString;
    private boolean flightOrItin = false;
    private String username;
    private String password;
    private int numSeats;
    private Client thisClient;
    private String accountType;
    
    private TextView numPicker;
	
	/* (non-Javadoc)
	 * @see com.flightapp.androidproject.NavDrawerActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get message from the intent
		Intent intent = getIntent();
		username = intent.getStringExtra(Constants.USERNAME);
		startDb = (Database) intent.getSerializableExtra(Constants.DATABASE);
		password = intent.getStringExtra(Constants.PASSWORD);
		accountType = intent.getStringExtra(Constants.ADMIN_OR_CLIENT);
		
		// Set the navigation drawer header text
		TextView drawerEmail = (TextView) findViewById(R.id.drawer_user_email);
		drawerEmail.setText(username);
		TextView drawerName = (TextView) findViewById(R.id.drawer_user_name);
		
		thisClient = new Client("Name", "Client", "Email", "Address", "CC Number", "Expiry Date");
		thisClient.setPassword(password);
		// Read clients file
		try {
			FileInputStream clientStream = openFileInput(Constants.CLIENT_FILE);
			InputStreamReader clientReader = new InputStreamReader(clientStream);
			startDb.readClients(clientReader);
			
			for (Client c : startDb.getClientList()) {
				if (c.getEmail().equals(username)) {
					thisClient = c;
					thisClient.setPassword(password);
					c = thisClient;
				}
			}
			clientReader.close();
		    clientStream.close();
		    
			drawerName.setText(thisClient.getFirstName() + " " + thisClient.getLastName());
		    
		} catch (IOException e) {
			// If the file cannot be read:
    		Toast.makeText(getApplicationContext(), "Client csv file could not be read", Toast.LENGTH_LONG).show();
    		drawerName.setText(Constants.CLIENT_STRING);
		}
		
		
		// Go to this activity's layout
		ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
		getLayoutInflater().inflate(R.layout.activity_client_options, content, true);
		
		
		// Get Buttons for the number picker.
		numSeats = 1;
		Button plus = (Button) findViewById(R.id.plus_button);
		Button minus = (Button) findViewById(R.id.minus_button);
		numPicker = (TextView) findViewById(R.id.num_seat_edittext);
		
		
		// Formatting for the date text box.
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US); //$NON-NLS-1$
        dateFormatter2 = new SimpleDateFormat("yyyy-MM-dd", Locale.US); //$NON-NLS-1$
        
        // Set the date on click.
        dateText = (EditText) findViewById(R.id.dateEditText);
        dateText.setInputType(InputType.TYPE_NULL);
        setDateTimeField();
        
        
	}
    
    /**
     * Sets the date and time inside the EditText when a date is chosen.
     */
    private void setDateTimeField() {
    	
    	// When this EditText is clicked, open a Date Picker.
    	dateText.setOnFocusChangeListener(new OnFocusChangeListener() {
    		
    		@Override
        	public void onFocusChange(View v, boolean hasFocus) {
        	    if(hasFocus){
        	    	datePickerDialog.show();
        	    	}
        	    }
    		});
    	
    	// Set the date given from the Date picker.
    	Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
 
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateText.setText(dateFormatter.format(newDate.getTime()));
                dateString = (dateFormatter2.format(newDate.getTime()));
            }
 
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        
    }
    
    /**
     * A checkbox which lets the user select a non stop flight search.
     * @param view The item which selects the checkbox.
     */
    public void onCheckboxClicked(View view) {
    	// Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        
        if (checked) {
        	flightOrItin = true;
        } else {
        	flightOrItin = false;
        }
    }
    
    /**
     * Searches for possible flights from user enter data.
     * @param view The button which submits the search on click.
     */
    public void submitGetFlights(View view) {
    	
		try {
			// Feads the flight file and stores in database
			FileInputStream flightStream = openFileInput(Constants.FLIGHT_FILE);
			InputStreamReader flightReader = new InputStreamReader(flightStream);
			startDb.readFlights(flightReader);
			flightReader.close();
			flightStream.close();
			
		} catch (IOException e) {
			// If the read fails:
			Toast.makeText(getApplicationContext(), "Flight csv file could not be read", Toast.LENGTH_SHORT).show();
		}
		
		// Create new intent
		Intent intent;
		
		// Get EditText boxes for the user entered data.
		EditText fromEditText = (EditText) findViewById(R.id.fromEditText);
		String fromText = fromEditText.getText().toString().trim();
		
		EditText toEditText = (EditText) findViewById(R.id.toEditText);
		String toText = toEditText.getText().toString().trim();
		
		EditText numSeatsBox = (EditText) findViewById(R.id.num_seat_edittext);
		String numSeatText = numSeatsBox.getText().toString();
		
		EditText dateEditText = (EditText) findViewById(R.id.dateEditText);
		String dateText = dateEditText.getText().toString();
		
		// Check to see if all EditTextes are filled
		if (!(fromText.length() > 0)) {
			Toast.makeText(getApplicationContext(), "Please enter a departure location.", Toast.LENGTH_SHORT).show();
		} else if (!(toText.length() > 0)) {
			Toast.makeText(getApplicationContext(), "Please enter a destination.", Toast.LENGTH_SHORT).show();
		} else if (!(dateText.length() > 0)) {
			Toast.makeText(getApplicationContext(), "Please enter a departure date.", Toast.LENGTH_SHORT).show();
		} else {
			
			// Pass the info to the result page.
			intent = new Intent(this, Results.class);
			
			intent.putExtra(Constants.CLIENT_OBJECT, thisClient);
			intent.putExtra(Constants.FROM, fromText);
			intent.putExtra(Constants.TO, toText);
			intent.putExtra(Constants.DATE, dateString);
			intent.putExtra(Constants.NUMBER_OF_SEATS, numSeatText);
			intent.putExtra(Constants.RESULTS, flightOrItin);
			intent.putExtra(Constants.DATABASE, startDb);
	    	intent.putExtra(Constants.USERNAME, username);
	    	intent.putExtra(Constants.PASSWORD, password);
			intent.putExtra(Constants.ADMIN_OR_CLIENT, accountType);
			startActivity(intent);
		}
    }
    
    /**
     * Increments the number box by 1.
     * @param view The view which increments this box on click.
     */
    public void onPlusClick(View view) {
    	
    	EditText numberText = (EditText) findViewById(R.id.num_seat_edittext);
    	int currentNum = Integer.parseInt(numberText.getText().toString());
    	
    	if (currentNum < 10) {
    		numSeats++;
    		numPicker.setText( "" + numSeats);
    		}
    	}
    
	/**
	 * Decreases the number box by 1.
	 * @param view The view which decreases this box on click.
	 */
	public void onMinusClick(View view) {
		
		EditText numberText = (EditText) findViewById(R.id.num_seat_edittext);
		int currentNum = Integer.parseInt(numberText.getText().toString());
		
		if (currentNum > 1) {
			numSeats--;
			numPicker.setText( "" + numSeats);
			}
		}
    
    /* (non-Javadoc)
     * @see com.flightapp.androidproject.NavDrawerActivity#onItemClick(android.widget.AdapterView, android.view.View, int, long)
     */
    @Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
    	
    	// Goes to the specified page in the navigation drawer.
    	if (position == 1) {
	    	Intent intent = new Intent(this, UserInfoPage.class);
	    	intent.putExtra(Constants.USERNAME, username);
	    	intent.putExtra(Constants.PASSWORD, password);
			intent.putExtra(Constants.DATABASE, startDb);
			intent.putExtra(Constants.CLIENT_OBJECT, thisClient);
			intent.putExtra(Constants.ADMIN_OR_CLIENT, accountType);
			startActivityForResult(intent, 1);
		}
		else if (position == 2) {
		    drawerLayout.closeDrawer(Gravity.LEFT);
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
    
    /**
     * Shows the date picker on click.
     * @param view The view which was clicked.
     */
    public void onDateEditClick(View view) {
    	datePickerDialog.show(); 
    }
    
    // Passes back data to this activity so that it remains updated.
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
}
