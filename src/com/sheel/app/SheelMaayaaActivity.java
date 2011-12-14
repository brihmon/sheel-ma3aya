package com.sheel.app;

import com.sheel.webservices.FacebookWebservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Connector activity used as a welcome page for the application
 * 
 * @author passant
 *
 */
public class SheelMaayaaActivity extends Activity {

	/**
	 * It handles all requests to facebook API. Moreover, it has
	 * all related info to logged in user and session
	 */
	private FacebookWebservice fbService = new FacebookWebservice();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connector_welcome_page);		
	}// end onCreate
	
	@Override 
	protected void onSaveInstanceState(Bundle outState) {
		
		//out
	}// end onSaveInstanceState
	
	/**
	 * Called when (register) button is clicked
	 * @param v
	 * 		Clicked button
	 */
	public void onClick_button_register(View v){
		Toast.makeText(this, "Hello Register", Toast.LENGTH_SHORT).show();
		startActivity(new Intent(this, NewUserActivity.class));
		
	}// end onClick_button_register
	
	/**
	 * Called when (login) button is clicked
	 * @param v
	 * 		Clicked button
	 */
	public void onClick_button_login(View v){
		startActivity(new Intent(this,ConnectorUserActionsActivity.class));
	}// end onClick_button_login
	
	/**
	 * Returns class responsible for handling all requests to facebook API. 
	 * Moreover, it has all related info to logged in user and session.
	 * <br><br>
	 * Note: 
	 * <ul>
	 * 		<li>In case you want details of logged in, use 
	 * 		{@link FacebookWebservice#getFacebookUser()} method.</li>
	 *      <li>In case you want details about session, use 
	 * 		{@link FacebookWebservice#isSessionValid()} method.</li>
	 * </ul>
	 * 
	 * @return
	 * 		Object used for interacting with facebook
	 */
	public FacebookWebservice getFacebookService(){
		return this.fbService;
	}// end getFacebookService
}// end class
