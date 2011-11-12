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

public class SearchFacebookActivity extends Activity {

	/**
	 * Constant used for tracing purposes (package + activity name)
	 */
	private final String ERROR_TAG = "" + ":" + "ProjectActivity" + ":";
	/**
	 * Constant identifying the app ID in facebook
	 */
	private final String APP_ID = "301637916526853";
	/**
	 * URL in open graph to get the user information
	 */
	private final String GRAPH_PATH_FOR_USER = "http://graph.facebook.com/me";
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

		Log.e(ERROR_TAG + "onCreate: ", "start");

		login();

	}// end onCreate

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e(ERROR_TAG, "onActivityResult: start");
		facebook.authorizeCallback(requestCode, resultCode, data);
		Log.e(ERROR_TAG, "onActivityResult: end");
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

		Log.e(ERROR_TAG, "login: sharedPreferences: " + sharedPreferences);

		// Search for user access token and its expiry duration
		String accessToken = sharedPreferences.getString(SP_ACCESS_TOKEN, null);
		long accessTokenExpiry = sharedPreferences.getLong(
				SP_ACCESS_TOKEN_EXPIRY, -1);

		Log.e(ERROR_TAG, "login: accessToken( " + accessToken + ")  expiry("
				+ accessTokenExpiry + ")");

		if (accessToken != null && accessTokenExpiry >= 0) {
			Log.e(ERROR_TAG, "login: " + "FB session is working");
			facebook.setAccessToken(accessToken);
			facebook.setAccessExpires(accessTokenExpiry);
			Log.e(ERROR_TAG, "login: " + "FB token setting is done");
			
			getUserInformation();
		}// end if : user already logged in -> pass token + expiry

		/*
		 * ##############################################################
		 * # If the user is not logged in (session expired) -> request  #
		 * # authorization 												#
		 * ##############################################################
		 */
		if (!facebook.isSessionValid()) {
			Log.e(ERROR_TAG, "login: " + "FB session expired");

			facebook.authorize(this, new DialogListener() {
				// @Override
				public void onComplete(Bundle values) {

					/*
					 * Called when the loginning in for FB + APP is successful
					 * -> basic data of user is there
					 */
					Log.e(ERROR_TAG, "onComplete");
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
					Log.e(ERROR_TAG, "onFacebookError");
				}

				// @Override
				public void onError(DialogError e) {
					Log.e(ERROR_TAG, "onError " + e.getMessage());
				}

				// @Override
				public void onCancel() {
					Log.e(ERROR_TAG, "onCancel");
				}
			});

		}// end if : facebook session expired -> login
	}// end login

	private Bundle getUserInformation(){
		// Result
		Bundle values = new Bundle();
		Log.e(ERROR_TAG,"getUserInformation(): Start");
		/* new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					String result = facebook.request(GRAPH_PATH_FOR_USER);
					Log.e(ERROR_TAG+":run(): ","result: "+ result );
				} catch (MalformedURLException e) {
					Log.e(ERROR_TAG+":run(): ","Error: MalformedURLException");
					e.printStackTrace();
				} catch (IOException e) {
					Log.e(ERROR_TAG+":run(): ","Error: IOException");
					e.printStackTrace();
				}
				
			}
		}.run();*/
				
		asyncFacebookRunner.request("me", new RequestListener() {
		//	@Override
			public void onComplete(String response, Object state) {
				// TODO Auto-generated method stub
				Log.e(ERROR_TAG,"getUserInformation(): onComplete: response("+response+")  state("+state+")");
				
			}// end onComplete

			// @Override
			public void onIOException(IOException e, Object state) {
				// TODO Auto-generated method stub
				Log.e(ERROR_TAG,"getUserInformation(): onIOException");
				
			}// end onIOException

			// @Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
				// TODO Auto-generated method stub
				Log.e(ERROR_TAG,"getUserInformation(): onFileNotFoundException");
				
			}// end onFileNotFoundException

		//	@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
				// TODO Auto-generated method stub
				Log.e(ERROR_TAG,"getUserInformation(): onMalformedURLException");
			}// end onMalformedURLException

			// @Override
			public void onFacebookError(FacebookError e, Object state) {
				// TODO Auto-generated method stub
				Log.e(ERROR_TAG,"getUserInformation(): onFacebookError");
			}// end onFacebookError
		});
		Log.e(ERROR_TAG,"getUserInformation(): End");
		// return result
		return values;		
	}// end getUserInformation
}// end activity