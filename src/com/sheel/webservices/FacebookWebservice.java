package com.sheel.webservices;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.sheel.datastructures.FacebookUser;
import com.sheel.datastructures.OfferDisplay2;
import com.sheel.datastructures.enums.OwnerFacebookStatus;
import com.sheel.listeners.AppDialogListener;
import com.sheel.listeners.AppRequestListener;
import com.sheel.listeners.LoginDataBaseListener;
import com.sheel.listeners.OffersFilterListener;
import com.sheel.utils.InternetManager;


/**
 * This class is used as a web service to interact with facebook for requests
 * regarding the user. A new instance of the class should be created per user.
 * 
 * @author 
 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
 *
 */

public class FacebookWebservice {
	
		
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
	
	
	/*
	 * --------------------------------------- Instance parameters ------------------------------------------
	 * 
	 */
	
	/**
	 * Instance of facebook for accessing all the features
	 */
	private final Facebook facebook = new Facebook(APP_ID);
	/**
	 * Asynchronous API requests to avoid blocking the UI thread
	 */
	private AsyncFacebookRunner asyncFacebookRunner = new AsyncFacebookRunner(facebook);
	/**
	 * Data structure for holding information about the facebook user
	 */	
	private FacebookUser fbUser = new FacebookUser();

	
	/*
	 * --------------------------------------- Constructors ------------------------------------------
	 * 
	 */
	
	/**
	 * Default constructor
	 */
	public FacebookWebservice(){
		//this.facebook.setAccessExpires(3600000);
		
	}// end constructor
	
	public FacebookWebservice (String facebookId, String accessToken , long expiryTime){
		// Set access token and expiry time
		this.facebook.setAccessToken(accessToken);
		this.facebook.setAccessExpires(expiryTime);
		
		// Set user ID
		this.fbUser = new FacebookUser(facebookId);
	}// end constructor
	/*
	 * --------------------------------------- Logic ------------------------------------------
	 * 
	 */
	
	/**
	 * Gets the facebook user (data structure) storing all data that was received about the user
	 * 
	 * @return 
	 * 		data structure having all information retrieved so far about user. If not initialized,
	 * 		it will return null
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public FacebookUser getFacebookUser(){
		return fbUser;
	}// end getFacebookUser
	
	/**
	 * Returns session status (whether user is still logged in 
	 * or not)
	 * 
	 * @return
	 *		<ul>
	 * 			<li><code>true</code>: session is valid</li>
	 * 			<li><code>false</code>: session has expired and you should log in</li>
	 * 		</ul>	
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public boolean isSessionValid(){
		return facebook.isSessionValid();
	}// end isSessionValid
	
	/**
	 * Returns facebook access token for generating issues on HTTP
	 * @return
	 * 		String representing the access token that is checked by 
	 * 		the API upon any requests 
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public String getUserAccessToken(){
		return facebook.getAccessToken();
	}// end getUserAccessToken
	
	/**
	 * Returns after how many milliseconds will the session expire
	 * @return
	 * 		long representing number of remaining millisecs before
	 * 		the session expires
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public long getUserAccessTokenExpiryTime(){
		return facebook.getAccessExpires();
	}// end getUserAccessTokenExpiryTime
	
	/**
	 * Open login window in browser for signing-in facebook and app and optionally
	 * retrieve user basic information. If user has facebook app on the mobile, it
	 * will do auto single-sign on. It guarantees the basic permissions needed for 
	 * the app the first time user registers. Permission covered is: email 
	 * 
	 * @param parentActivity
	 * 		activity that the user will be diverted to after signing-in
	 * @param getUserInfo
	 * 		<ul>
	 * 			<li><code>true</code>: retrieve user basic information
	 * 			after signing in. See 
	 * 			{@link FacebookWebservice#getUserInformation(boolean)} for more 
	 * 			information about retrieved data.</li>
	 * 			<li><code>false</code>: just sign in without retrieving data</li>
	 * 		</ul>
	 * @param isInfoForApp
	 * 		<ul>
	 * 			<li><code>true</code>: retrieve user basic information
	 * 			related to APP ONLY. See 
	 * 			{@link FacebookWebservice#getUserInformation(boolean)} for more 
	 * 			information about retrieved data.</li>
	 * 			<li><code>false</code>: retrieve all public information of the user</li>
	 * 		</ul>	
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public void login(Activity parentActivity ,final boolean getUserInfo , final boolean isInfoForApp, final LoginDataBaseListener dbListener, final Context appContext){
		
		/**
		 * Inner class for listening to different 
		 * events relevant to login process dialog
		 * 
		 * @author 
		 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
		 *
		 */
		class LoginListener extends AppDialogListener{
			
			/**
			 * Number of seconds after which the session expires
			 */
			private long expiryTime = -1;
			
			/**
			 * Default constructor. It does not set an expiry time for the 
			 * session. It uses the default one sent by facebook
			 */
			public LoginListener() {
				
			}// end constructor
			
			/**
			 * Constructor used to set expiry time for the session in seconds
			 * 
			 * @param expiryTime
			 * 		Expiry time in seconds with no fractions (number of seconds
			 * 		after which the session expires)
			 * 
			 * Note that the session does not expire from facebook server side
			 * except after default time. However, {@link isSessionValid} can be
			 * used to detect if session is valid or not. If not then log out
			 * explicitly and then promote logging in again
			 */
			public LoginListener(long expiryTime) {
				this.expiryTime = expiryTime; 
			}// end constructor
					
			@Override
			public void onComplete(Bundle values) {
				Log.e(TAG_CLASS_PACKAGE,"login: onComplete: Login successful " );
				
				if (expiryTime > -1) {
					facebook.setAccessExpiresIn(expiryTime+"");
				}
				
				Log.e(TAG_CLASS_PACKAGE,"login: onComplete: curernt remaining num of millisecs: " + facebook.getAccessExpires());
			
				if (getUserInfo){
					Log.e(TAG_CLASS_PACKAGE, "login: onComplete: getUserInformation for app");
					getUserInformation(isInfoForApp,dbListener,appContext);						
				}// end if : 1st time to log in -> get user data relevant for app
				else {
					Log.e(TAG_CLASS_PACKAGE, "login: onComplete: getUserId");
					requestUserFacebookId();
				}// end else: get User facebook ID to be saved in session
				
			}// end onComplete	
		
		}// end class
		
		if (!facebook.isSessionValid()){
			
			Log.e(TAG_CLASS_PACKAGE,"Login: session expired");
			
			String[] permissions = new String[]{"email"};
			// Session will expire after 1 hour (number in seconds)
			long sessionExpiryTime = 60;//3600;
			//facebook.authorize(parentActivity, permissions, new LoginListener(sessionExpiryTime));
			facebook.authorize(parentActivity, permissions, new LoginListener());
			
		}// end if : session is ended -> non access token -> request new one
	
	}// end login
	
	/**
	 * Ends the current session of user in the app
	 * 
	 * @param parentActivity
	 * 		activity where the logout is called upon to retrieve context
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public void logout(Activity parentActivity){
		asyncFacebookRunner.logout(parentActivity.getApplicationContext(),new AppRequestListener());
	}// end logout
	
	/**
	 * Ends the current session of user in the app
	 * 
	 * @param c
	 * 		application context
	 * 
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public void logout(Context c){
		asyncFacebookRunner.logout(c,new AppRequestListener());
	}// end logout
	
	/**
	 * Used to retrieve user basic information. User must be signed in
	 * and having active access_token for the method to perform properly.
	 * The information is loaded and can be retrieved using
	 * {@link FacebookWebservice#getFacebookUser()}
	 * 
	 * @param isForApp
	 * 		<ul>
	 * 			<li><code>true</code>: retrieve user basic information
	 * 			related to APP ONLY. See {@link FacebookUser} for more 
	 * 			information about retrieved data.</li>
	 * 			<li><code>false</code>: retrieve all public information 
	 * 			of the user</li>
	 * 		</ul>	
	 * 
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public void getUserInformation(boolean isForApp,final LoginDataBaseListener dbListener, final Context appContext){
			
		System.out.println("Remaining time for session timeout: " + facebook.getAccessExpires());
		if (facebook.isSessionValid()){
			String fields="";
			
			if (isForApp){
				fields="?fields=id,first_name,middle_name,last_name,gender,verified,email&";
				getUserInformation(fields, true, dbListener, appContext);
			}// end if: get only needed parameters for the app
			else{
				getUserInformation(fields);				
			}// end else: get all possible info about the user		
	
		}// end if : get information if session is valid
		else {
			Log.e(TAG_CLASS_PACKAGE,"getUserInformation: session has expired");
		}
		
	}// end getUserInformationForApp
		
	/**
	 * Used to retrieve user basic information. User must be signed in
	 * and having active access_token for the method to perform properly.
	 * The information is loaded and can be retrieved using
	 * {@link FacebookWebservice#getFacebookUser()}
	 * 
	 * 
	 * @param fields
	 * 		requested fields from the facebook API. For more information
	 * 		on available parameters, see 
	 * 		{@linkplain http://developers.facebook.com/tools/explorer/?method=GET&path=me}
	 * 		. <b>You should always include <code>id</code> in the fields if you specify any</b>.
	 * 		The format needed is: ?fields=NEEDED_FIELD1,NEEDED_FIELD2& 
	 * 		(<b>Example:</b> <code>?fields=id&</code> to get ID of user)
	 * 		If you enter "", it will get all available information about user.
	 *
	 *@author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public void getUserInformation(String fields){
		
		getUserInformation(fields, false, null, null);
	}// end getUserInformationForApp
	
	public void getUserInformation(String fields,final boolean checkLoginStatusFromDb , final LoginDataBaseListener dbListener,final Context appContext ){
		
		class BasicInfoListener extends AppRequestListener{
			
			Semaphore dataIsReceived = new Semaphore(0);
			
			@Override
			public void onComplete(String response, Object state) {
				Log.e(TAG_CLASS_PACKAGE,"getUserInformation: onComplete: LoggedIn user response=" + response);
				fbUser = new FacebookUser(response,true);
				Log.e(TAG_CLASS_PACKAGE,"getUserInformation: onComplete: LoggedIn user=" + fbUser);
				
				if (checkLoginStatusFromDb){
					InternetManager.isRegisteredUser(appContext, fbUser.getUserId(),dbListener);
				}// end if: check if the user is registered or not
				dataIsReceived.release();
			}// end onComplete
			
			public Semaphore getSemaphore(){
				return this.dataIsReceived;
			}
		}// end class
		
		if (facebook.isSessionValid()){
			fields="";
			Log.e(TAG_CLASS_PACKAGE,"getUserInformation: Requested fields: " + fields);
			BasicInfoListener listener = new BasicInfoListener();
			asyncFacebookRunner.request("me"+fields, listener);
			blockThreadUntilAllOffersAreProcessed(listener.getSemaphore());
		}// end if : get information if session is valid
		
	}// end getUserInformationForApp
		
	/**
	 * Requests the facebook user ID and saves it in the session.
	 * <br><b>IMPORTANT:</b> It will overwrite the content of the 
	 * facebook user inside the facebook service and all its 
	 * references 
	 *  
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public void requestUserFacebookId() {
		if (facebook.isSessionValid()) {
			getUserInformation("?fields=id&");
		}// end if : check that the session is still valid
	}// end getUserId
			
	/**
	 * This method filters offers coming from user's friends from a more
	 * generic set of offers. This method will issue (N) HTTP requests for checking
	 * whether owners are friends with user or not. N is the number of owners to
	 * be checked
	 * 
	 * @param offersFromUsers 
	 * 		Hash table where:
	 * 		<ul>
	 * 			<li>the <code>key</code> is Facebook ID of requested offer owner</li>
	 * 			<li>the <code>value</code> different offers from this offer owner</li>
	 * 		</ul> 
	 * 		<b>IMPORTANT: the minimum number of allowed elements in the list is 1,
	 * 		i.e. the list must be checked that it is not empty before calling the 
	 * 		method</b>
	 * @return
	 * 		List of 2 items containing all needed information about offers.
	 * 		<ul>
	 * 			<li> <code> index= 0 ==> ArrayList of OfferDisplay2</code>:
	 * 			List of search results having owners who are friends with app
	 * 			user.  The list is ordered where all offers of the same owner 
	 * 			are after each other. If no owners who are friends were found, 
	 * 			list is returned empty. In case of any exceptions (usually about 
	 * 			connection), null is returned. By default it will check 
 	 *			facebook status of returned results to	
	 *			{@link OwnerFacebookStatus#FRIEND}
	 *			<li> <code> index= 1 ==> ArrayList of String</code>:
	 * 			List of filtered offer owners Facebook IDs. This list is a set, 
	 * 			i.e. has no duplicates 
	 *			</li>
	 *		</ul>
	 *		In case no results were found, an empty list of size 0 is returned
	 *
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public ArrayList<ArrayList<?>>  filterOffersFromFriends(Hashtable<String,ArrayList<OfferDisplay2>> offersFromUsers){
		
		/**
		 * Inner class for listening to different actions while requesting
		 * friendship status between the user and an owner of an offer
		 * 
		 * @author passant
		 *
		 */
		class FriendShipStatusCheckListener extends OffersFilterListener{

			/**
			 * Constructor
			 * @param offersFromUsers
			 * 		Hash table where:
			 * 		<ul>
			 * 			<li>the <code>key</code> is Facebook ID of requested offer owner</li>
			 * 			<li>the <code>value</code> different offers from this offer owner</li>
			 * 		</ul> 
			 * @param classPackageName
			 * 		class name (package name) where listener is called. It is used
			 * 		for tracing purposes in the log messages	
			 * @param methodName
			 * 		method name where the listener is used. It is used
			 * 		for tracing purposes in the log messages	
			 * 
			 *  @author 
			 *  	Passant El.Agroudy (passant.elagroudy@gmail.com)
			 */
			public FriendShipStatusCheckListener( Hashtable<String, ArrayList<OfferDisplay2>> offersFromUsers,
					String classPackageName,String methodName){
				super(offersFromUsers, classPackageName, methodName);
				
			}// end constructor
			
			@Override
			public void processRequest (JSONObject parsedResponse, Object state) {
				// get data relevant to mutual friends
				JSONObject receivedDataOfFriend = extractDataJsonObject(parsedResponse);
				if (receivedDataOfFriend != null){	
					try{
						// Get owner facebook ID currently checked if a friend or not
						String ownerId = receivedDataOfFriend.getString("id");
						// Get the OfferDisplay of that owner and save in result
						addFilteredOfferDisplay(ownerId,OwnerFacebookStatus.FRIEND,null);
						generateLogMessage(": processRequest: New offers from friend added ->ownerID:" + ownerId);
					}catch(JSONException e){
						generateLogMessage("processRequest: Error: could not get id ");
						e.printStackTrace();
					}// end catch
							
				}/* end if : if both are friends => FB returns FB ID of checked friend mapped to his/her name */
			}// end processRequest			
		}// end class
		
		// Used for tracing purposes
		String methodName = "filterOffersFromFriends(Hashtable<String,ArrayList<OfferDisplay2>> offersFromUsers)";
		
		if (this.facebook.isSessionValid()){
			// Create new listener for friends of friends status
			FriendShipStatusCheckListener friendshipStatusCheckListener = new FriendShipStatusCheckListener(offersFromUsers,TAG_CLASS_PACKAGE,methodName);
				
			/* Send HTTP request. Graph API path: me/friends/friendId.  
			 * The only permissions needed for operation to be performed is <access_token>
			 * to be granted 
			 */
			requestOfferFilteringByRelationBetweenAppUserAnd(offersFromUsers, "friends", friendshipStatusCheckListener, false);
			
			blockThreadUntilAllOffersAreProcessed(friendshipStatusCheckListener.getSemaphore());
			
			ArrayList<ArrayList<?>> result = new ArrayList<ArrayList<?>>();
			result.add(friendshipStatusCheckListener.getFilteredOffers());
			result.add(friendshipStatusCheckListener.getFilteredOffersOwners());
			return result;
		}else{
			Log.e(TAG_CLASS_PACKAGE,methodName + " : session expired -> no search -> return empty list");
			ArrayList<OfferDisplay2> offers = new ArrayList<OfferDisplay2>();
			ArrayList<String> offersOwnersIds = new ArrayList<String>();
			ArrayList<ArrayList<?>> result = new ArrayList<ArrayList<?>>();
			result.add(offers); result.add(offersOwnersIds);
			return result;
		}// end else
		
	}// end filterOffersFromOwnersWithMutualFriends
		
	/**
	 * This method is used to filter offers coming from offer owners with mutual 
	 * friends with the app user from a more generic set of offers. This method
	 * will issue (N) HTTP requests for checking whether owners have mutual
	 * friends with the user or not.  (N) is the number of owners to be checked.
	 * 
	* @param offersFromUsers 
	 * 		Hash table where:
	 * 		<ul>
	 * 			<li>the <code>key</code> is Facebook ID of requested offer owner</li>
	 * 			<li>the <code>value</code> different offers from this offer owner</li>
	 * 		</ul> 
	 * 		<b>IMPORTANT: the minimum number of allowed elements in the list is 1,
	 * 		i.e. the list must be checked that it is not empty before calling the 
	 * 		method</b>
	 * @return	 
	 * 		List of 2 items containing all needed information about offers.
	 * 		<ul>
	 * 			<li> <code> index= 0 ==> ArrayList of OfferDisplay2</code>:
	 * 			List of search results having owners who are friends with app
	 * 			user.  The list is ordered where all offers of the same owner 
	 * 			are after each other. If no owners who are friends were found, 
	 * 			list is returned empty. In case of any exceptions (usually about 
	 * 			connection), null is returned. By default it will check 
 	 *			facebook status of returned results to	
	 *			{@link OwnerFacebookStatus#FRIEND_OF_FRIEND}
	 *			<li> <code> index= 1 ==> ArrayList of String</code>:
	 * 			List of filtered offer owners Facebook IDs. This list is a set, 
	 * 			i.e. has no duplicates 
	 *			</li>
	 *		</ul>
	 *		In case no results were found, an empty list of size 0 is returned
	 *
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */	
	public ArrayList<ArrayList<?>>  filterOffersFromOwnersWithMutualFriends (Hashtable<String,ArrayList<OfferDisplay2>> offersFromUsers){
				
		/**
		 * Inner class for listening to different actions while requesting
		 * mutual friends between the user and an owner of an offer
		 * 
		 * @author 
		 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
		 *
		 */
		class MutualFriendsCheckListener extends OffersFilterListener{

			/**
			 * Constructor
			 * @param offersFromUsers
			 * 		Hash table where:
			 * 		<ul>
			 * 			<li>the <code>key</code> is Facebook ID of requested offer owner</li>
			 * 			<li>the <code>value</code> different offers from this offer owner</li>
			 * 		</ul> 
			 * @param classPackageName
			 * 		class name (package name) where listener is called. It is used
			 * 		for tracing purposes in the log messages	
			 * @param methodName
			 * 		method name where the listener is used. It is used
			 * 		for tracing purposes in the log messages	
			 * 
			 *  @author 
			 *  	Passant El.Agroudy (passant.elagroudy@gmail.com)
			 */
			public MutualFriendsCheckListener( Hashtable<String, ArrayList<OfferDisplay2>> offersFromUsers,
					String classPackageName,String methodName){
				super(offersFromUsers, classPackageName, methodName);
				
			}// end constructor
			
			@Override
			public void processRequest(JSONObject parsedResponse, Object state) {
				// get data relevant to mutual friends
				JSONArray receivedDataOfMutualFriends = extractDataJsonArray(parsedResponse);
				if (receivedDataOfMutualFriends != null){	
					// Get owner ID currently checked for mutual friends
					String ownerId = (String)state;
					generateLogMessage(": onComplete(processRequest): has mutual friends ownerId: " + ownerId);
					// Add the owner relevant offers to the filtered list + update their Facebook Info
					generateLogMessage(" : onComplete(processRequest): Mutual Friends for " + ownerId + " are: " + receivedDataOfMutualFriends);
					addFilteredOfferDisplay(ownerId,OwnerFacebookStatus.FRIEND_OF_FRIEND,receivedDataOfMutualFriends);
					generateLogMessage(": onComplete(processRequest): Extra info set for offers of ownerId " + ownerId);		
				}// end if : if owner and user have common friends => FB returns mutual friends between both
			}// end processRequest			
		}// end class
		
		// Used for tracing purposes
		String methodName = "filterOffersFromOwnersWithMutualFriends (Hashtable<String,ArrayList<OfferDisplay2>> offersFromUsers)";
		
		if (this.isSessionValid()){
			
			// Create new listener for friends of friends status
			MutualFriendsCheckListener mutualFriendsCheckListener = new MutualFriendsCheckListener(offersFromUsers,TAG_CLASS_PACKAGE,methodName);
				
			/* Send HTTP request. Graph API path: me/mutualfriends/friendId.  
			 * The only permissions needed for operation to be performed is <access_token>
			 * to be granted 
			 */
			requestOfferFilteringByRelationBetweenAppUserAnd(offersFromUsers, "mutualfriends", mutualFriendsCheckListener, true);
			
			blockThreadUntilAllOffersAreProcessed(mutualFriendsCheckListener.getSemaphore());
			
			ArrayList<ArrayList<?>> result = new ArrayList<ArrayList<?>>();
			result.add(mutualFriendsCheckListener.getFilteredOffers());
			result.add(mutualFriendsCheckListener.getFilteredOffersOwners());
			return result;
		}else{
			Log.e(TAG_CLASS_PACKAGE,methodName + " : session expired -> no search -> return empty list");
			
			ArrayList<OfferDisplay2> offers = new ArrayList<OfferDisplay2>();
			ArrayList<String> offersOwnersIds = new ArrayList<String>();
			ArrayList<ArrayList<?>> result = new ArrayList<ArrayList<?>>();
			result.add(offers); result.add(offersOwnersIds);
			return result;
		}// end else
	
	}// end filterOffersFromOwnersWithMutualFriends
	
	/**
	 * IMPORTANT: This method must be invoked at the top of the calling
     * activity's onActivityResult() function or Facebook authentication will
     * not function properly! (FROMO FACEBOOK SDK)
     * It must be called in (onActivityResult) method "overriden method
     * in activity handling authentication
     * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void authorizeCallback(int requestCode, int resultCode, Intent data){
		facebook.authorizeCallback(requestCode, resultCode, data);
	}// end authorizeCallback
	
	/**
	 * Helper method for acquiring a semaphore with the try and 
	 * catch needed for handling exceptions
	 * 
	 * @param s
	 * 		Semaphore that will trying acquiring a permit
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	private void blockThreadUntilAllOffersAreProcessed(Semaphore s){
		try {
			/* Since no permits are available -> thread will sleep till .release() is called
			 * .release() won't be called until all offers are processed 
			 * (look at onComplete method in the inner class)
			 * 
			 * This is done to allow parallel processing of results but force sequential order
			 * for result returning
			 */
			s.acquire();
		} catch (InterruptedException e) {
			Log.e(TAG_CLASS_PACKAGE,"blockThreadUntilAllOffersAreProcessed: semaphore was interrupted");
			e.printStackTrace();
		}// end catch
	}// end blockThreadUntilAllOffersAreProcessed
	
	/**
	 * Helper method used as to perform the same connection status 
	 * request between the app user and set of owners for offers
	 * to filter them. The method does the REQUEST part only, while
	 * the listener does the FILTERING PART
	 * 
	  * @param offersFromUsers
	 * 		Hash table where:
	 * 		<ul>
	 * 			<li>the <code>key</code> is Facebook ID of requested offer owner</li>
	 * 			<li>the <code>value</code> different offers from this offer owner</li>
	 * 		</ul> 
	 * @param filterName
	 * 		Name of connection that will be used as the path in the
	 * 		graph API. 
	 * 		<br><br>The format is:<code>me/FILTER_NAME/ownerFbId</code>,
	 *  	where:
	 * 		me= fixed keyword representing signed-in user
	 * 		friendId= facebook ID of offer owner to be checked
	 * 		<br><br>Available options are: 
	 * 		<code>friends</code>,<code>mutualfriends</code><br><br>
	 * @param listener
	 * 		Listener that handles the results upon receiving them from 
	 * 		the facebook server. It should contain all the filtering logic
	 * @param isSendOwnerIdInState
	 * 			<ul>
	 * 				<li><code>True</code>: send owner ID in the state variable
	 * 				to be accessible on receiving a response</li>
	 * 				<li><code>False</code>: leave state variable null</li>
	 * 			</ul> 
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	private void requestOfferFilteringByRelationBetweenAppUserAnd(
			Hashtable<String, ArrayList<OfferDisplay2>> offersFromUsers, 
			String filterName, OffersFilterListener listener,
			boolean isSendOwnerIdInState){
		
		// Get an iterator to loop the set of owners IDs for checking
		Iterator<String> ownersIdsIterator = offersFromUsers.keySet().iterator();
	
		while(ownersIdsIterator.hasNext()){
			
			// Get owner ID
			String currentOwnerFacebookId = ownersIdsIterator.next();
			
			/* Issue an HTTP request to check if an offer owner and app user have
			 * a certain relation/connection			 
			 */
			if (isSendOwnerIdInState){
				asyncFacebookRunner.request("me/"+filterName+ "/"+currentOwnerFacebookId, listener, currentOwnerFacebookId);
			}// end if: send ownerID in state
			else{
				asyncFacebookRunner.request("me/"+filterName+ "/"+currentOwnerFacebookId, listener);
			}// end if: leave state null
			
		}// end while : check IDs of all offer owners
	}// end requestOfferFilteringByRelationBetweenAppUserAnd
	
	/**
	 * Helper method used to parse some responses from facebook server.
	 * Some responses are formatted as 2 arrays: one containing data and 
	 * another containing pagination information. The method extracts the
	 * data part and returns it as a JSONObject
	 * 
	 * @param receviedResponse
	 * 		the response received from the facebook server, parsed to
	 * 		a JSONObject
	 * 
	 * @return
	 * 		data object. If not found (no data was transfered) or 
	 * 		in case of exceptions, it will return null
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	private JSONObject extractDataJsonObject(JSONObject receviedResponse){
		
		if (receviedResponse.has("error")){
			return null;
		}// end if: returned response is an error -> process was not valid
		
		// Get friend data	
		JSONArray responseData;
		try {
			Log.e("passant", "extractDataJsonObject receivedResponse: " + receviedResponse);
			responseData = receviedResponse.getJSONArray("data");
		
			if (responseData.length() > 0){
				return responseData.getJSONObject(0);				
			}// end if: array has any data -> make it an object
			else{
				return null;
			}// end else: no data was sent
		} catch (JSONException e) {
			Log.e(TAG_CLASS_PACKAGE,"extractDataJsonObject(JSONObject receviedResponse): error in JSON parsing");
			e.printStackTrace();
			return null;
		}// end catch
				
	}// end extractDataJsonObject
	
	private JSONArray extractDataJsonArray(JSONObject receviedResponse){
		
		if (receviedResponse.has("error")){
			return null;
		}// end if: returned response is an error -> process was not valid
		
		// Get friend data	
		JSONArray responseData;
		try {
			Log.e("passant", "extractDataJsonObject receivedResponse: " + receviedResponse);
			responseData = receviedResponse.getJSONArray("data");
		
			if (responseData.length() > 0){
				return responseData;				
			}// end if: array has any data -> make it an object
			else{
				return null;
			}// end else: no data was sent
		} catch (JSONException e) {
			Log.e(TAG_CLASS_PACKAGE,"extractDataJsonObject(JSONObject receviedResponse): error in JSON parsing");
			e.printStackTrace();
			return null;
		}// end catch
				
	}// end extractDataJsonObject
		
	
	// ________________________________TESTING METHODS________________
		
	public void tester_filterOffersFromFriends(){
		
		// TODO : change testing IDs to ones relevant to sheelma3aya
		
		// Input list: cannot make value = null -> exception
		Hashtable<String, ArrayList<OfferDisplay2>>offersFromUsers = new Hashtable<String, ArrayList<OfferDisplay2>>();
		
		ArrayList<OfferDisplay2> offersList1 = new ArrayList<OfferDisplay2>();
		ArrayList<OfferDisplay2> offersList2 = new ArrayList<OfferDisplay2>();
		ArrayList<OfferDisplay2> offersList3 = new ArrayList<OfferDisplay2>();
		ArrayList<OfferDisplay2> offersList4 = new ArrayList<OfferDisplay2>();
		
		offersList1.add(new OfferDisplay2("32529",1,OwnerFacebookStatus.UNRELATED));// friend
		offersList1.add(new OfferDisplay2("32529",5,OwnerFacebookStatus.UNRELATED));// friend
		offersList2.add(new OfferDisplay2("48304588",2,OwnerFacebookStatus.UNRELATED));// friend
		offersList3.add(new OfferDisplay2("58215973",3,OwnerFacebookStatus.UNRELATED));// friend
		offersList4.add(new OfferDisplay2("1207059",4,OwnerFacebookStatus.UNRELATED));// not friend
		
		offersFromUsers.put("32529", offersList1);
		offersFromUsers.put("48304588", offersList2);
		offersFromUsers.put("58215973", offersList3);
		offersFromUsers.put("1207059", offersList4);
			
		// Expected output : Arraylist having 32529 (ofr1 , ofr5), 48304588 (2), 58215973 (3)
		// STATUS : SUCCESSFUL
		// STATUS AFTER SUPPORTING MULTIPLE OFFERS/SAME USER: SUCCESSFUL
		
		
		/* Comments: CANNOT TEST ON YOUR OWN ACCOUNTS : user- dependent
		 * 
		 *  Bugs:
		 *  	1) total number of things to be processed before releasing
		 *  	semaphore is number of users NOT offers  -> FIXED (sprint2)
		 *  
		 */
		
		
		// Execute method
		ArrayList<ArrayList<?>> results = filterOffersFromFriends(offersFromUsers);
		ArrayList<OfferDisplay2>friendsOffers  = (ArrayList<OfferDisplay2>)results.get(0);
		ArrayList<String>friendsIds  = (ArrayList<String>)results.get(1);
				
		System.out.println("Number of offers from friends: " + friendsOffers.size());
		System.out.println("Friends results: " + friendsOffers);
		System.out.println("Friends IDs: " + friendsIds);
	}// end tester_filterOffersFromFriends
	
	public void tester_filterOffersFromOwnersWithMutualFriends(){
		
		// Input list
		OwnerFacebookStatus fbStatus = OwnerFacebookStatus.FRIEND_OF_FRIEND;
		String usrId1 = "32529"; 	// will have mutual friends
		String usrId2 = "48304588";	// will have mutual friends
		String usrId3 = "1207059";	// no mutual friends
		
		OfferDisplay2 ofr1 = new OfferDisplay2(usrId1,1,fbStatus);
		OfferDisplay2 ofr4 = new OfferDisplay2(usrId1,4,fbStatus);
		OfferDisplay2 ofr2 = new OfferDisplay2(usrId2,2,fbStatus);
		OfferDisplay2 ofr3 = new OfferDisplay2(usrId3,3,fbStatus);
	
		ArrayList<OfferDisplay2> offersList1 = new ArrayList<OfferDisplay2>();
		ArrayList<OfferDisplay2> offersList2 = new ArrayList<OfferDisplay2>();
		ArrayList<OfferDisplay2> offersList3 = new ArrayList<OfferDisplay2>();
	
		offersList1.add(ofr1);
		offersList1.add(ofr4);
		offersList2.add(ofr2);
		offersList3.add(ofr3);		
		
		Hashtable<String, ArrayList<OfferDisplay2>> offersFromUsers = new Hashtable<String, ArrayList<OfferDisplay2>>();
		offersFromUsers.put(usrId1, offersList1);
		offersFromUsers.put(usrId2, offersList2);
		offersFromUsers.put(usrId3, offersList3);
		
		// Expected output : Arraylist having ofr1 ,ofr4, ofr2 , 
		// STATUS : SUCCESSFUL
		
		/* Comments: CANNOT TEST ON YOUR OWN ACCOUNTS : user- dependent
		 * 
		 * Bugs:
		 *  	1) Sync as result is returned before processing all
		 *  	offers -> FIXED (sprint1)
		 *  	2) Data should be extracted from response and not the whole
		 *  	response including pagination -> FIXED (sprint1)
		 *  	
		 */
		
		
		// Execute method
				ArrayList<ArrayList<?>> results = filterOffersFromOwnersWithMutualFriends(offersFromUsers);
				ArrayList<OfferDisplay2>friendsOffers  = (ArrayList<OfferDisplay2>)results.get(0);
				ArrayList<String>friendsIds  = (ArrayList<String>)results.get(1);
						
				System.out.println("Number of offers from friends: " + friendsOffers.size());
				System.out.println("Friends results: " + friendsOffers);
				System.out.println("Friends IDs: " + friendsIds);
	}// end tester_filterOffersFromOwnersWithMutualFriends
	

	
}// end class
