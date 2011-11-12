package com.sheel.webservices;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
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

/**
 * This class is used as a web service to interact with facebook for requests
 * regarding the user
 * 
 * @author passant
 *
 */

public class FacebookWebservice {
	
	/*
	 * --------------------------------------- Enumerations ------------------------------------------------
	 *  
	 */
	
	enum FacebookUserProperties{
		id,
		first_name,
		middle_name,
		last_name,
		gender,
		verified,
		email		
	}
	
	/*
	 * --------------------------------------- Constants ---------------------------------------------------
	 * 
	 */
	
	/**
	 * Constant used for tracing purposes "class name (package name)"
	 */
	private final String TAG_CLASS_PACKAGE = "FacebookWebService (com.sheel.webservices): ";
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
	
	/*
	 * --------------------------------------- Instance parameters ------------------------------------------
	 * 
	 */
	
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
	/**
	 * Data structure for holding information about the facebook user
	 */	
	FacebookUser fbUser = null;

	
	/*
	 * --------------------------------------- Constructors ------------------------------------------
	 * 
	 */
	
	/**
	 * Gets the facebook user (data structure) storing all data that was received about the user
	 * @return data structure having all information retrieved so far about user
	 */
	public FacebookUser getFacebookUser(){
		return fbUser;
	}// end getFacebookUser
	
	public void login (Activity parentActivity){
		
		/*
		 * ########################################################## 
		 * # If the user is already logged in -> use his credentials#
		 * ##########################################################
		 */
		
		// Get the shared preferences of the user from the activity
		sharedPreferences = parentActivity.getPreferences(Context.MODE_PRIVATE);

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
			getUserInfoForApp();
			Log.e(TAG_CLASS_PACKAGE, "login: " + "FB token setting is done");			
		}// end if : user already logged in -> pass token + expiry
		
		/*
		 * ##############################################################
		 * # If the user is not logged in (session expired) -> request  #
		 * # authorization 												#
		 * ##############################################################
		 */
		if (!facebook.isSessionValid()) {
			Log.e(TAG_CLASS_PACKAGE, "login: " + "FB session expired");

			facebook.authorize(parentActivity, new DialogListener() {
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
					getUserInfoForApp();
				}// end onComplete:

				public void onFacebookError(FacebookError error) {
					Log.e(TAG_CLASS_PACKAGE, "onFacebookError");
				}// end onFacebookError

				public void onError(DialogError e) {
					Log.e(TAG_CLASS_PACKAGE, "onError " + e.getMessage());
				}// end onError

				public void onCancel() {
					Log.e(TAG_CLASS_PACKAGE, "onCancel");
				}// end onCancel
			});

		}// end if : facebook session expired -> login
	
		
	}// end login
	
	public void getUserInfoForApp(){
		// Create list of required Information from user object
		ArrayList<String> requiredInformation = new ArrayList<String>();
					
		requiredInformation.add(FacebookUserProperties.id.name());
		requiredInformation.add(FacebookUserProperties.first_name.name());
		requiredInformation.add(FacebookUserProperties.middle_name.name());
		requiredInformation.add(FacebookUserProperties.last_name.name());
		requiredInformation.add(FacebookUserProperties.gender.name());
		requiredInformation.add(FacebookUserProperties.verified.name());
		requiredInformation.add(FacebookUserProperties.email.name());		
		
		getUserInfoFor(requiredInformation);
	}// end getUserInfoForApp
	
	public void getUserInfoFor(ArrayList<String> userPropertiesRequired){
		
		if (facebook.isSessionValid()){
			
			// TODO needs optimization (see how to pass bundle for the method)
			
			// TODO choosing using properties is not enabled yet IMPORTANT
			/*String parameters = "?field=";
			int size = userPropertiesRequired.size();
			if ( size > 0){
				for (int i=0 ; i<size ; i++){
					parameters += userPropertiesRequired.get(i) + ",";
				}// end for : concatinate different options
			}// end if : check it has elements
			parameters = parameters.substring(0,parameters.length()-1);
			
			System.out.println("Parameters: " + parameters);*/
			
			// Generate an asynchronous call to avoid freezing the GUI 
			asyncFacebookRunner.request("me",new RequestListener(){

				public void onComplete(String response, Object state) {
					Log.e(TAG_CLASS_PACKAGE,"getUserInfoFor: onComplete : response : " + response);
					
					//analyze data and return an easy-to-handle object
					fbUser = new FacebookUser(response);
					System.out.println(fbUser);
					
					getUserFriends();
				
									
				}// end onComplete
				
				public void onIOException(IOException e, Object state) {
					// TODO Auto-generated method stub
					
				}// end onIOException

				public void onFileNotFoundException(FileNotFoundException e,Object state) {
					// TODO Auto-generated method stub
					
				}// end onFileNotFoundException
				
				public void onMalformedURLException(MalformedURLException e,
						Object state) {
					// TODO Auto-generated method stub
					
				}// end onMalformedURLException

				public void onFacebookError(FacebookError e, Object state) {
					// TODO Auto-generated method stub
					
				}// end onFacebookError		
			});	
					
			
		}// end if : session is still valid
		else{
			Log.e(TAG_CLASS_PACKAGE,"getUserInfoFor: session has expired");
		}
	
	}// end getUserInfoFor
	
	public void getUserFriends(){
		asyncFacebookRunner.request("me/friends", new RequestListener(){

			public void onComplete(String response, Object state) {
				Log.e(TAG_CLASS_PACKAGE,"getUserFriends: onComplete : response : " + response);
				
				// Parse the JSON string to key-value map
				try {
					JSONObject parsedValues = new JSONObject(response);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
								
			}// end onComplete
			
			public void onIOException(IOException e, Object state) {
				// TODO Auto-generated method stub
				
			}// end onIOException

			public void onFileNotFoundException(FileNotFoundException e,Object state) {
				// TODO Auto-generated method stub
				
			}// end onFileNotFoundException
			
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
				// TODO Auto-generated method stub
				
			}// end onMalformedURLException

			public void onFacebookError(FacebookError e, Object state) {
				// TODO Auto-generated method stub
				
			}// end onFacebookError		
		});
	}// end getUserFriends
	
	public Object[] getMutualFriends(){
		throw new UnsupportedOperationException("Not implemented yet");
	}// end getMutualFriends
	

}// end class
