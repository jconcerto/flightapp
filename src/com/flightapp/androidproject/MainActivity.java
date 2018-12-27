package com.flightapp.androidproject;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import bookingApp.Client;
import bookingApp.Database;

import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Login page activity which lets a user enter an email and password to
 * log in. An Admin will be taken to the Admin Options page, while
 * a Client will be taken to the Client Options page.
 *
 */
public class MainActivity extends NavDrawerActivity{
	
	private Database startDb;
	private String password;
	private Client thisClient;
	private String username;
	
	/* (non-Javadoc)
	 * @see com.flightapp.androidproject.NavDrawerActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Disable sound effects
		findViewById(R.id.background).setSoundEffectsEnabled(false);
		findViewById(R.id.login_background).setSoundEffectsEnabled(false);
		
		// Hide action bar
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		
		// Gets intent if it exists.
		Intent origIntent = getIntent();
		if (origIntent != null) {
			if (origIntent.getSerializableExtra(Constants.DATABASE) != null) {
				startDb = (Database) origIntent.getSerializableExtra(Constants.DATABASE);
			} else {
				startDb = new Database();
			}
		} else {
			startDb = new Database();
		}
		
		readPassFile();
	}
		
	
	/**
	 * Reads a password.txt file to store in the database.
	 */
	public void readPassFile() {

		try { 
			// reads the password file and store in database
			FileInputStream passwordStream = openFileInput(Constants.PASSWORD_FILE);
			InputStreamReader passwordReader = new InputStreamReader(passwordStream);
			startDb.readUserPass(passwordReader);
		    if (!startDb.checkAdminExists()) {
		    	
				try {
				// Creates a default Admin if none exist.
					String loginInfo = "\r\n" + Constants.DEFAULT_ADMIN;
					FileOutputStream passwordAdmin = openFileOutput(Constants.PASSWORD_FILE, MODE_APPEND);
					OutputStreamWriter passwordWriter = new OutputStreamWriter(passwordAdmin);
					BufferedWriter bWriter = new BufferedWriter(passwordWriter);
					bWriter.write(loginInfo);
					bWriter.close();
					passwordWriter.close();
					passwordAdmin.close();
					Toast.makeText(getApplicationContext(), "Created default Admin - " +
							"Username and password are: admin", Toast.LENGTH_SHORT).show(); 

				} catch (IOException e){
					Toast.makeText(getApplicationContext(), "Could not create a new Admin", Toast.LENGTH_SHORT).show(); 
				}
		    }
		} catch(IOException e){
			Toast.makeText(getApplicationContext(), "Password.txt file could not be read", Toast.LENGTH_SHORT).show(); 
			}
	}
	
	/**
	 * Logs in the user from the given email and password.
	 * @param view The button which submits the login.
	 */
	public void loginUser(View view) {
		
		readPassFile();
		
		Intent intent;
		
		// Get username and password from EditText
		EditText usernameText = (EditText) findViewById(R.id.username);
		username = usernameText.getText().toString();
		
		EditText passwordText = (EditText) findViewById(R.id.password);
		password = passwordText.getText().toString();
		
		int numUsers = startDb.getUserLogins().size();
		int userCount = 0;
		
		// Check each user for their login information from the database.
		for (String[] user : startDb.getUserLogins()) {
			
			userCount += 1;
			
			// Check username
			if (user[0].equals(username)) {
				
				userCount = 0;
				
				// Check password
				if (user[1].equals(password)) {
					
					// If user is admin
					if (user[2].equals(Constants.ADMIN_STRING)) {
						
						intent = new Intent(this, AdminOptions.class);
						intent.putExtra(Constants.ADMIN_OR_CLIENT, Constants.ADMIN_STRING);
						
					// If user is client
					} else {
						intent = new Intent(this, ClientOptions.class);
						intent.putExtra(Constants.ADMIN_OR_CLIENT, Constants.CLIENT_STRING);
					}
					
					// Creates the intent and starts the next activity
					intent.putExtra(Constants.USERNAME, username);
					intent.putExtra(Constants.PASSWORD, password);
					intent.putExtra(Constants.DATABASE, startDb);
					startActivity(intent);
					
				} else {
					Toast.makeText(getApplicationContext(), Constants.WRONG_PASS, Toast.LENGTH_SHORT).show();
				}
			}
		}
		if (numUsers == userCount) {
			Toast.makeText(getApplicationContext(), Constants.NO_USERNAME, Toast.LENGTH_SHORT).show();
			userCount = 0;
		}
	}
	
	/**
	 * Goes to a page which lets a user create a new client account.
	 * @param view The button which submits this method on click.
	 */
	public void createNewUser(View view) {
		Intent intent = new Intent(this, NewUserActivity.class);
		intent.putExtra(Constants.DATABASE, startDb);
		startActivity(intent);
	}
	
	
}
