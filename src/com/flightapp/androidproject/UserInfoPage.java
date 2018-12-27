package com.flightapp.androidproject;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import bookingApp.Client;
import bookingApp.Database;
import bookingApp.Flight;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Display's user information in this Activity. Also
 * allows the user to change any of their own personal info.
 *
 */
public class UserInfoPage extends NavDrawerActivity {

	private String username;
	private String password;
	private Database startDb;
	private Client thisClient;
	private String accountType;
	private String emailString;
	private String passwordString;
	private String firstNameString;
	private String lastNameString;
	private String addressString;
	private String ccNumString;
	private String expiryDateString;
	private EditText editPassword;
	private EditText editEmail;
	private EditText editFirstName;
	private EditText editLastName;
	private EditText editAddress;
	private EditText editCCNum;
	private EditText editExpDate;

	
	/* (non-Javadoc)
	 * @see com.flightapp.androidproject.NavDrawerActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//get last intent
		Intent intent = getIntent();
		username = intent.getStringExtra(Constants.USERNAME);
		password = intent.getStringExtra(Constants.PASSWORD);
		startDb = (Database) intent.getSerializableExtra(Constants.DATABASE);
		thisClient = (Client) intent.getSerializableExtra(Constants.CLIENT_OBJECT);
		
		// Set the drawer to display user info.
		TextView drawerName = (TextView) findViewById(R.id.drawer_user_name);
		drawerName.setText(thisClient.getFirstName() + " " + thisClient.getLastName());
		
		TextView drawerEmail = (TextView) findViewById(R.id.drawer_user_email);
		drawerEmail.setText(thisClient.getEmail());
		
		ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
		getLayoutInflater().inflate(R.layout.activity_user_info_page, content, true);
		
		// Find EditText boxes of the user's information.
		editEmail = (EditText) findViewById(R.id.ui_email_edittext);
		editPassword = (EditText) findViewById(R.id.ui_password_edittext);
		editFirstName = (EditText) findViewById(R.id.ui_first_name);
		editLastName = (EditText) findViewById(R.id.ui_last_name);
		editAddress = (EditText) findViewById(R.id.ui_address_edittext);
		editCCNum = (EditText) findViewById(R.id.ui_cc_num_edittext);
		editExpDate = (EditText) findViewById(R.id.ui_date_edittext);
		
		// Set the EditTexts to contain the user's info.
		setUserInfo();
	}
	
	/**
	 * Sets a user's information from given EditText boxes.
	 */
	private void setUserInfo() {

		editEmail.setText(thisClient.getEmail());
		
		editPassword.setText(thisClient.getPassword());

		editFirstName.setText(thisClient.getFirstName());

		editLastName.setText(thisClient.getLastName());

		editAddress.setText(thisClient.getAddress());

		editCCNum.setText(thisClient.getCCnumber());

		String[] stringED = thisClient.getExpiryDate().split("-");
		editExpDate.setText(stringED[2] + "-" + stringED[1] + "-" + stringED[0]);
		
	}
	
	/**
	 * Sets the Client object with the correct info.
	 */
	private void setClientObject() {
		
		thisClient.setFirstName(editFirstName.getText().toString());
		thisClient.setLastName(editLastName.getText().toString());
		thisClient.setEmail(editEmail.getText().toString());
		thisClient.setPassword(editPassword.getText().toString());
		thisClient.setAddress(editAddress.getText().toString());
		thisClient.setCCnumber(editCCNum.getText().toString());
		
		String[] eDString= editExpDate.getText().toString().split("-");
		thisClient.setExpiryDate(eDString[2] + "-" + eDString[1] + "-" + eDString[0]);
	}
	
	/**
	 * Submits a new info change, if one was made.
	 * @param view The view which was clicked.
	 */
	public void submitNewInfo(View view) {
		
		emailString = editEmail.getText().toString();
		passwordString = editPassword.getText().toString();
		firstNameString = editFirstName.getText().toString();
		lastNameString = editLastName.getText().toString();
		addressString = editAddress.getText().toString();
		ccNumString = editCCNum.getText().toString();
		expiryDateString = editExpDate.getText().toString();
		
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
    	} else if (!(expiryDateString.length() > 0)) {
    		Toast.makeText(getApplicationContext(), "Please enter a credit card expiry date.", Toast.LENGTH_SHORT).show();
    	} else {
    		
    		// Sets the Client with new information.
    		setClientObject();
    		setUserInfo();
    		
    		try {
    			
    			// Writes to client file.
    			FileOutputStream clientStream = openFileOutput(Constants.CLIENT_FILE, MODE_PRIVATE);
    			OutputStreamWriter clientWriter = new OutputStreamWriter(clientStream);
    			BufferedWriter bWriter = new BufferedWriter(clientWriter);
    			
    			for (Client c : startDb.getClientList()) {
    				
    				if (c.getEmail().equals(username)) {
    					bWriter.write("\r\n" + thisClient.toString());
    				} else {
    					bWriter.write("\r\n" + c.toString());
    					}
    				}
				bWriter.close();
    			clientWriter.close();
    			clientStream.close();
    			
    			// Writes to password file.
    			FileOutputStream clientStream2 = openFileOutput(Constants.PASSWORD_FILE, MODE_PRIVATE);
    			OutputStreamWriter clientWriter2 = new OutputStreamWriter(clientStream2);
    			BufferedWriter bWriter2 = new BufferedWriter(clientWriter2);
    			
    			for (String[] s : startDb.getUserLogins()) {
    				
    				if (s[1].equals(username)) {
    					bWriter2.write("\r\n" + emailString + "," + 
    				passwordString + "," + s[2]);
    					s[1] = emailString;
    					s[2] = passwordString;
    					
    				} else {
    					bWriter2.write("\r\n" + s[0] + "," + s[1] + "," + s[2]);
    				}
    			}
    			
				bWriter2.close();
    			clientWriter2.close();
    			clientStream2.close();
    			
				username = emailString;
				password = passwordString;
				
    			// Update the database
    			FileInputStream cStream = openFileInput(Constants.CLIENT_FILE);
    			InputStreamReader cReader = new InputStreamReader(cStream);
    			startDb.readClients(cReader);
    			cStream.close();
    			cReader.close();
				
				// Go to next activity after changes are done.
    			Intent intent = new Intent();
    			intent.putExtra(Constants.DATABASE, startDb);
    			intent.putExtra(Constants.USERNAME, username);
    			intent.putExtra(Constants.PASSWORD, password);
    			intent.putExtra(Constants.CLIENT_OBJECT, thisClient);
    			intent.putExtra(Constants.ADMIN_OR_CLIENT, accountType);
    			setResult(RESULT_OK, intent);
        		Toast.makeText(getApplicationContext(), "Changes submitted.", Toast.LENGTH_SHORT).show();
    		} catch (IOException e) {
    			
        		Toast.makeText(getApplicationContext(), "Could not write to client file.", Toast.LENGTH_SHORT).show();
    		}
    	}
    	
	}
	/**
	 * Resets information to what it was originally.
	 * Goes back to the Client Options page.
	 * @param view
	 */
	public void cancelUserInfo(View view) {
		
		setUserInfo();
		Intent intent = new Intent(this, ClientOptions.class);
		intent.putExtra(Constants.DATABASE, startDb);
		intent.putExtra(Constants.PASSWORD, password);
		intent.putExtra(Constants.CLIENT_OBJECT, thisClient);
		intent.putExtra(Constants.USERNAME, username);
		startActivity(intent);
		finish();
		setUserInfo();
	}
	
    /* (non-Javadoc)
     * @see com.flightapp.androidproject.NavDrawerActivity#onItemClick(android.widget.AdapterView, android.view.View, int, long)
     */
    @Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
    	
    	if (position == 1) {
		    drawerLayout.closeDrawer(Gravity.LEFT);
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
