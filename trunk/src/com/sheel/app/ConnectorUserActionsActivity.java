package com.sheel.app;

import com.sheel.webservices.FacebookWebservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * This activity displays the home page that will be displayed to the user after
 * registering
 * 
 * @author Nada Emad
 * 
 */
public class ConnectorUserActionsActivity extends UserSessionStateMaintainingActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connector_user_action);
		System.out.println("Register intergrate: " + "facebookservice: "+ getFacebookService() );
		
		if (getFacebookService() == null){
			setFacebookService(new FacebookWebservice());
			getFacebookService().login(this, false, false);
		}// end if : previous activity was welcome(login)

	}// end onCreate

	/**
	 * This is called when the declare button is clicked
	 * 
	 * @param v
	 */

	public void onClick_DeclareOffer(View v) {
		Toast.makeText(getApplicationContext(), "Ahmed: Declare Offers",
				Toast.LENGTH_SHORT).show();
		// TODO Mohsen uncomment the method and pass ur class
		//Intent intent =setSessionInformationBetweenActivities(typeOfNextActivity)
		//startActivity(intent);
	}// end onClick_DeclareOffer

	/**
	 * This is called when the search button is clicked
	 * 
	 * @param v
	 */

	public void onClick_SearchOffers(View v) {		
		Intent intent =setSessionInformationBetweenActivities(FilterPreferencesActivity.class);
		startActivity(intent);
	}// end onClick_SearchOffers
	
	public void onClick_test(View v){
		
		FacebookWebservice fbServ = getFacebookService();
		System.out.println("Register intergrate: " + "facebookservice after login: "+ fbServ );
		fbServ.getUserInformation(true);
		
	}

}
