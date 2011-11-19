package com.sheel.app;

import com.sheel.datastructures.enums.SharedValuesBetweenActivities;
import com.sheel.webservices.FacebookWebservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Base class for all activities inside the app to maintain critical
 * information of user and session that must be passed between all 
 * activities
 * 
 * @author passant
 *
 */
public class UserSessionStateMaintainingActivity extends Activity {

	/**
	 * It handles all requests to facebook API. Moreover, it has
	 * all related info to logged in user and session
	 */
	private FacebookWebservice fbService = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// read needed parameters from previous activity
		getSessionInformationBetweenActivities();
		
		// do any logic you want
	}// end onCreate
	
	@Override 
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
    	getFacebookService().authorizeCallback(requestCode, resultCode, data);
    }// end onActivityResult
      
	
	
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
	
	/**
	 * IMPORTANT: Before leaving any activity, you must call this  method to pass
	 * the important details about session and user 
	 *	 
	 * @param typeOfNextActivity
	 * 		Class type of new activity you intend to navigate to. Write activity name
	 * 		then call .getClass() method
	 * 
	 * @return
	 * 		Intent containing data needed for the next activity. Use 
	 * 		{@link Activity#startActivity(Intent)} to start the new activity
	 */
	public Intent setSessionInformationBetweenActivities (Class<?> typeOfNextActivity){

		   Intent mIntent = new Intent(this, typeOfNextActivity);
			// Pass variable to detailed view activity using the intent
			mIntent.putExtra(SharedValuesBetweenActivities.userFacebookId.name(), fbService.getFacebookUser().getUserId());
			mIntent.putExtra(SharedValuesBetweenActivities.userAccessToken.name(), fbService.getUserAccessToken());
			mIntent.putExtra(SharedValuesBetweenActivities.accessTokenExpiry.name(), fbService.getUserAccessTokenExpiryTime());
			
			return mIntent;
	}// end SetSessionInformationBetweenActivities
	
	/**
	 * IMPORTANT: used to pass necessary information about current session and 
	 * user between all activities	
	 */
	private void getSessionInformationBetweenActivities (){
		System.out.println("integration: getSessionInformationBetweenActivities: activity: " + this);
		Intent actvIntent = this.getIntent();
		Bundle extras = actvIntent.getExtras();
		
		if (extras != null){
			String key_id = SharedValuesBetweenActivities.userFacebookId.name();
			String key_token= SharedValuesBetweenActivities.userAccessToken.name();
			String key_expiry = SharedValuesBetweenActivities.accessTokenExpiry.name();
			if (extras.containsKey(key_id) && extras.containsKey(key_token) && extras.containsKey(key_expiry)){
				fbService= new FacebookWebservice(extras.getString(key_id), extras.getString(key_token), extras.getLong(key_expiry));
			}// end if : access token + expiry time + fb ID are there -> create new service
		}// end if : extras has info		
	}// end getSessionInformationBetweenActivities
	
	public void setFacebookService(FacebookWebservice fbService){
		this.fbService = fbService;
	}
	
}// end class
