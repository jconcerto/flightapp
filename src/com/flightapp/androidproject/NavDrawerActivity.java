package com.flightapp.androidproject;

import adapters.NavAdapter;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.UserManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * An activity which creates a navigation drawer at the top left of the screen.
 * It allows a user to select a page they may which to see.
 *
 */
public class NavDrawerActivity extends ActionBarActivity implements OnItemClickListener {

    protected DrawerLayout drawerLayout;
    private ListView drawerList;
    private NavAdapter myAdapter;
    private ActionBarDrawerToggle drawerListener;

	/* (non-Javadoc)
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Hide the default action bar.
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		
		setContentView(R.layout.activity_nav_drawer);
		
		// Set a ListView for the drawer items.
		drawerList = (ListView) findViewById(R.id.left_drawer);
		
		View hview = getLayoutInflater().inflate(R.layout.drawer_userinfo, null);
		drawerList.addHeaderView(hview, null, false);
		
		// Create an Adapter to make the ListView.
        myAdapter = new NavAdapter(this);
        drawerList.setAdapter(myAdapter);
		
        // Set each list item in the drawer ListView
        drawerList.setOnItemClickListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        // When the drawer icon is clicked, open or close the navigation drawer.
        drawerListener = new ActionBarDrawerToggle(
        		this, drawerLayout, 
        		R.drawable.ic_drawer, 
        		R.string.drawer_open,
        		R.string.drawer_close) {
        	@Override
        	public void onDrawerClosed(View drawerView) {
        		super.onDrawerClosed(drawerView);
        	}
        	@Override
        	public void onDrawerOpened(View drawerView) {
        		super.onDrawerOpened(drawerView);
        	}
        };
        
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nav_drawer, menu);
		return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (drawerListener.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// Select the item that was clicked.
		selectItem(position);
	}

	/**
	 * The item which is clicked.
	 * @param position The index position of the clicked item.
	 */
	public void selectItem(int position) {
		drawerList.setItemChecked(position, true);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onPostCreate(android.os.Bundle)
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerListener.syncState();
	}
	
	/* (non-Javadoc)
	 * @see android.support.v7.app.ActionBarActivity#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerListener.onConfigurationChanged(newConfig);
	}
	
	/**
	 * Sets the user's name and email at the top of the drawer.
	 * @param name The name of the logged in user.
	 * @param email The email of the logged in user.
	 */
	public void setHeaderText(String name, String email) {
		
		setContentView(R.layout.drawer_userinfo);
		
		// Get TextViews and set the name and email.
		TextView drawerName = (TextView) findViewById(R.id.drawer_user_name);
		drawerName.setText(name);
		
		TextView drawerEmail = (TextView) findViewById(R.id.drawer_user_email);
		drawerEmail.setText(email);
		
		setContentView(R.layout.activity_nav_drawer);
		
		View hview = getLayoutInflater().inflate(R.layout.drawer_userinfo, null);
		drawerList.addHeaderView(hview, null, false);
	}
	
	/**
	 * Gets the drawer layout of this class.
	 * @return The drawerLayout
	 */
	public DrawerLayout getDrawerLayout() {
		return drawerLayout;
	}
	
	/**
	 * Opens or closes the drawer on click.
	 * @param view The button which clicks the drawer.
	 */
	public void openCloseDrawer(View view) {
		if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
			drawerLayout.closeDrawer(Gravity.LEFT);
		} else {
			drawerLayout.openDrawer(Gravity.LEFT);
		}
	}
	
	/**
	 * Makes EditText focus disappear on background click.
	 * This makes it easier to see the screen after a person
	 * is finished entering info into EditText.
	 * @param view The view which was clicked.
	 */
	public void loseFocus(View view) {
		ViewGroup background = (ViewGroup) findViewById(R.id.background);
		background.requestFocus();
		InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(this.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		view.setSoundEffectsEnabled(false);
	}

}
