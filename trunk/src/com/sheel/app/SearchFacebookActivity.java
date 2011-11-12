package com.sheel.app;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.sheel.datastructures.FacebookUser;
import com.sheel.webservices.FacebookWebservice;

public class SearchFacebookActivity extends Activity {

	/**
	 * Constant used for tracing purposes "class name (package name)"
	 */
	private final String TAG_CLASS_PACKAGE = "SearchFacebookActivity (com.sheel.app): ";
	/**
	 * Constant identifying the app ID in facebook
	 */
	private final String APP_ID = "301637916526853";
	
	/* Not grouped in an enumeration to avoid writing code for 
	 * passing their values. (class implementation)
	 * Check URL: 
	 * http://javahowto.blogspot.com/2006/10/custom-string-values-for-enum.html
	 */
	
	/**
	 * Shared preferences key for user access token
	 */
	private final String SP_ACCESS_TOKEN = "access_token";
	/**
	 * Shared preferences key for user access token expiry duration
	 */
	private final String SP_ACCESS_TOKEN_EXPIRY = "access_expiry";

	
	/**
	 * Instance of facebook for accessing all the features
	 */
	Facebook facebook = new Facebook(APP_ID);
	/**
	 * Asynchronous API requests to avoid blocking the UI thread
	 */
	AsyncFacebookRunner asyncFacebookRunner = new AsyncFacebookRunner(facebook);
	/**
	 * Different settings saved for different apps in android settings
	 */
	SharedPreferences sharedPreferences;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Log.e(TAG_CLASS_PACKAGE + "onCreate: ", "start");

		//login();
		
		FacebookWebservice fbService = new FacebookWebservice();
		// will automatically login and get the first set of info
		fbService.login(this);
		
		//while(!fbService.isInitializedUser){
			
		//}
		
		//fbService.getUserFriends();
		
	}// end onCreate

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e(TAG_CLASS_PACKAGE, "onActivityResult: start");
		facebook.authorizeCallback(requestCode, resultCode, data);
		Log.e(TAG_CLASS_PACKAGE, "onActivityResult: end");
	}

	// -----------------------------------------------------------

	/**
	 * Used to show dialog box for user to log in facebook and in to the app
	 */
	private void login() {

		/*
		 * ########################################################## 
		 * # If the user is already logged in -> use his credentials#
		 * ##########################################################
		 */

		// Get the shared preferences of the user
		sharedPreferences = getPreferences(MODE_PRIVATE);

		Log.e(TAG_CLASS_PACKAGE, "login: sharedPreferences: " + sharedPreferences);

		// Search for user access token and its expiry duration
		String accessToken = sharedPreferences.getString(SP_ACCESS_TOKEN, null);
		long accessTokenExpiry = sharedPreferences.getLong(
				SP_ACCESS_TOKEN_EXPIRY, -1);

		Log.e(TAG_CLASS_PACKAGE, "login: accessToken( " + accessToken + ")  expiry("
				+ accessTokenExpiry + ")");

		if (accessToken != null && accessTokenExpiry >= 0) {
			Log.e(TAG_CLASS_PACKAGE, "login: " + "FB session is working");
			facebook.setAccessToken(accessToken);
			facebook.setAccessExpires(accessTokenExpiry);
			Log.e(TAG_CLASS_PACKAGE, "login: " + "FB token setting is done");
			
			getUserInformation();
		}// end if : user already logged in -> pass token + expiry

		/*
		 * ##############################################################
		 * # If the user is not logged in (session expired) -> request  #
		 * # authorization 												#
		 * ##############################################################
		 */
		if (!facebook.isSessionValid()) {
			Log.e(TAG_CLASS_PACKAGE, "login: " + "FB session expired");

			facebook.authorize(this, new DialogListener() {
				// @Override
				public void onComplete(Bundle values) {

					/*
					 * Called when the loginning in for FB + APP is successful
					 * -> basic data of user is there
					 */
					Log.e(TAG_CLASS_PACKAGE, "onComplete");
					// Log.e(ERROR_TAG,"Welcome "+name);

					/*
					 * Edit the shared preferences and add to them the user
					 * access token and its expiry date
					 */
					// TODO why do we need this
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString(SP_ACCESS_TOKEN, facebook.getAccessToken());
					editor.putLong(SP_ACCESS_TOKEN_EXPIRY,
							facebook.getAccessExpires());
					editor.commit();
					
					getUserInformation();

				}// end onComplete:

				// @Override
				public void onFacebookError(FacebookError error) {
					Log.e(TAG_CLASS_PACKAGE, "onFacebookError");
				}

				// @Override
				public void onError(DialogError e) {
					Log.e(TAG_CLASS_PACKAGE, "onError " + e.getMessage());
				}

				// @Override
				public void onCancel() {
					Log.e(TAG_CLASS_PACKAGE, "onCancel");
				}
			});

		}// end if : facebook session expired -> login
	}// end login

	private Bundle getUserInformation(){
		// Result
		Bundle values = new Bundle();
		Log.e(TAG_CLASS_PACKAGE,"getUserInformation(): Start");
						
		asyncFacebookRunner.request("me", new RequestListener() {
		//	@Override
			public void onComplete(String response, Object state) {
				// TODO Auto-generated method stub
				Log.e(TAG_CLASS_PACKAGE,"getUserInformation(): onComplete: response("+response+")  state("+state+")");
				
				// Create a data structure from the provided NEEDED info
				FacebookUser fbUser = new FacebookUser(response);
				// TESTING purpose
				System.out.println(fbUser);
								
			}// end onComplete

			// @Override
			public void onIOException(IOException e, Object state) {
				// TODO Auto-generated method stub
				Log.e(TAG_CLASS_PACKAGE,"getUserInformation(): onIOException");
				
			}// end onIOException

			// @Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
				// TODO Auto-generated method stub
				Log.e(TAG_CLASS_PACKAGE,"getUserInformation(): onFileNotFoundException");
				
			}// end onFileNotFoundException

		//	@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
				// TODO Auto-generated method stub
				Log.e(TAG_CLASS_PACKAGE,"getUserInformation(): onMalformedURLException");
			}// end onMalformedURLException

			// @Override
			public void onFacebookError(FacebookError e, Object state) {
				// TODO Auto-generated method stub
				Log.e(TAG_CLASS_PACKAGE,"getUserInformation(): onFacebookError");
			}// end onFacebookError
		});
		Log.e(TAG_CLASS_PACKAGE,"getUserInformation(): End");
		// return result
		return values;		
	}// end getUserInformation
}// end activity