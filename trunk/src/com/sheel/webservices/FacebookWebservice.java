package com.sheel.webservices;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import org.json.JSONArray;
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
import com.sheel.app.SheelMaaayaClient;
import com.sheel.datastructures.FacebookUser;
import com.sheel.datastructures.OfferDisplay;
import com.sheel.datastructures.enums.OwnerFacebookStatus;
import com.sheel.listeners.OffersFilterListener;


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
		
		
		sharedPreferences = parentActivity.getPreferences(Context.MODE_PRIVATE);
		
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
					 * Called when the Log in for FB + APP is successful
					 * -> basic data of user is there
					 */
					Log.e(TAG_CLASS_PACKAGE, "login : onComplete");
					// Log.e(ERROR_TAG,"Welcome "+name);

					/*
					 * Edit the shared preferences and add to them the user
					 * access token and its expiry date
					 * 
					 * It is used to avoid showing the user a transition
					 * dialog between facebook and app during the same 
					 * session
					 */
					
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString(SP_ACCESS_TOKEN, facebook.getAccessToken());
					editor.putLong(SP_ACCESS_TOKEN_EXPIRY,
							facebook.getAccessExpires());
					editor.commit();
					getUserInfoForApp();

					methodTester();
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
		else{
			//tester_filterOffersFromOwnersWithMutualFriends();
			methodTester();
		}
		
	}// end login
	
	
	public void logout(Activity parentActivity){
		asyncFacebookRunner.logout(parentActivity.getApplicationContext(), new RequestListener(){

			@Override
			public void onComplete(String response, Object state) {
				Log.e(TAG_CLASS_PACKAGE,"logout: onComplete");
				
			}

			@Override
			public void onIOException(IOException e, Object state) {
				Log.e(TAG_CLASS_PACKAGE,"logout: onIOException");
				
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
				Log.e(TAG_CLASS_PACKAGE,"logout: onFileNotFoundException");
				
				
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
				Log.e(TAG_CLASS_PACKAGE,"logout: onMalformedURLException");
				
				
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
				Log.e(TAG_CLASS_PACKAGE,"logout: onFacebookError");
				
				
			}
			
		});
		
	}// end logout
	
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
					
					//getUserFriends();
				
									
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
					
					Iterator it = parsedValues.keys();
					
						//Map<String , String> dic = new Map<String, String>();
						
					
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
	
	/**
	 * This method is filter offers coming from user's friends from a more
	 * generic set of offers. This method assumes the list of user friends
	 * is already retrieved.
	 * 
	 * @param userFriends
	 * 		JSON string representing user friends. see @link {@link FacebookWebservice#getUserFriends()}
	 * 
	 * @param offersFromUsers 
	 * 		Hash table where:
	 * 			<ul>
	 * 				<li>the <code>key</code> is Facebook ID of requested offer owner</li>
	 * 				<li>the <code>value</code> is object representing offer</li>
	 * 			</ul> 
	 * @return
	 * 		list containing Facebook IDs of offer owners who are friends with 
	 * 		the user. In case all owners are NOT friends with the user, the list
	 * 		is returned empty (size=0). This list can be used later to index the 
	 * 		offers (in the input hash table for example) and retrieve them for
	 * 		displaying.
	 * 
	 */
	public ArrayList<String> filterOffersFromFriends(JSONObject userFriends,Hashtable<String,Object> offersFromUsers){
		
		// List of result of owners in friends of the user
		ArrayList<String> ownersIdsFromFriends = new ArrayList<String>();
		
		// List of facebook IDs of offer owners without duplicates
		Set<String> ownersIds= offersFromUsers.keySet();
		
		// Get an iterator to loop the set of owners IDs for checking
		Iterator<String> ownersIdsIterator = ownersIds.iterator();
		
		while(ownersIdsIterator.hasNext()){
			
			// Owner ID to be checked in the iteration
			String currentOwnerFacebookId = ownersIdsIterator.next();
			if (userFriends.has(currentOwnerFacebookId)){
				ownersIdsFromFriends.add(currentOwnerFacebookId);
			}// end if : offer owner ID exists in friends of user -> add to output list
			
		}// end while : check IDs of all offer owners		
		
		return ownersIdsFromFriends;
	}// end filterOffersFromFriends
	
	
	/**
	 * This method is filter offers coming from user's friends from a more
	 * generic set of offers. This method will issue (N) HTTP requests for checking
	 * whether owners are friends with user or not. N is the number of owners to
	 * be checked
	 * 
	 * @param offersFromUsers 
	 * 		Hash table where:
	 * 			<ul>
	 * 				<li>the <code>key</code> is Facebook ID of requested offer owner</li>
	 * 				<li>the <code>value</code> is object representing offer</li>
	 * 			</ul> 
	 * @return
	 * 		Hashtable of search results having owners who are friends with app
	 * 		user. If no owners who are friends were found, list is returned
	 * 		empty.
	 *		Hash table where:
	 * 			<ul>
	 * 				<li>the <code>key</code> is Facebook ID of offer owner</li>
	 * 				<li>the <code>value</code> is object representing offer 
	 * 				and user details needed to display a search result. By default
	 * 				it will check its facebook status to
	 * 				@link {@link OwnerFacebookStatus#FRIEND}</li>
	 * 			</ul> 
	 */
	public Hashtable<String,OfferDisplay>  filterOffersFromFriends(Hashtable<String,OfferDisplay> offersFromUsers){
		
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
			 * 			<li>the <code>value</code> is object representing offer</li>
			 * 		</ul> 
			 * @param classPackageName
			 * 		class name (package name) where listener is called. It is used
			 * 		for tracing purposes in the log messages	
			 * @param methodName
			 * 		method name where the listener is used. It is used
			 * 		for tracing purposes in the log messages	 
			 */
			public FriendShipStatusCheckListener(Hashtable<String, OfferDisplay> offersFromUsers,
					String classPackageName, String methodName) {
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
						addFilteredOfferDisplay(ownerId,OwnerFacebookStatus.FRIEND);
						generateLogMessage(": processRequest: New offer from friend added ->ownerID:" + ownerId);
					}catch(JSONException e){
						generateLogMessage("processRequest: Error: could not get id ");
						e.printStackTrace();
					}// end catch
							
				}/* end if : if both are friends => FB returns FB ID of checked friend mapped to his/her name */
			}// end processRequest			
		}// end class
		
		// Used for tracing purposes
		String methodName = "filterOffersFromFriends (Hashtable<String,OfferDisplay> offersFromUsers)";
		
		// Create new listener for friends of friends status
		FriendShipStatusCheckListener friendshipStatusCheckListener = new FriendShipStatusCheckListener(offersFromUsers,TAG_CLASS_PACKAGE,methodName);
			
		/* Send HTTP request. Graph API path: me/friends/friendId.  
		 * The only permissions needed for operation to be performed is <access_token>
		 * to be granted 
		 */
		requestOfferFilteringByRelationBetweenAppUserAnd(offersFromUsers, "friends", friendshipStatusCheckListener, false);
		
		blockThreadUntilAllOffersAreProcessed(friendshipStatusCheckListener.getSemaphore());
		
		return friendshipStatusCheckListener.getFilteredOffers();
	}// end filterOffersFromOwnersWithMutualFriends
	
	
	/**
	 * This method is used to filter offers coming from offer owners with mutual 
	 * friends with the app user from a more generic set of offers. This method
	 * will issue (N) HTTP requests for checking whether owners have mutual
	 * friends with the user or not.  (N) is the number of owners to be checked.
	 * 
	 * @param offersFromUsers
	 * 		Hash table where:
	 * 			<ul>
	 * 				<li>the <code>key</code> is Facebook ID of offer owner</li>
	 * 				<li>the <code>value</code> is object representing offer 
	 * 				and user details needed to display a search result</li>
	 * 			</ul> 
	 * @return
	 * 		Hashtable of search results having owners with common friends with app
	 * 		user. If no owners with mutual friends were found, list is returned
	 * 		empty.
	 *		Hash table where:
	 * 			<ul>
	 * 				<li>the <code>key</code> is Facebook ID of offer owner</li>
	 * 				<li>the <code>value</code> is object representing offer 
	 * 				and user details needed to display a search result. By default
	 * 				it will check its facebook status to
	 * 				@link {@link OwnerFacebookStatus#FRIEND_OF_FRIEND}</li>
	 * 			</ul> 
	 */	
	public Hashtable<String,OfferDisplay>  filterOffersFromOwnersWithMutualFriends (Hashtable<String,OfferDisplay> offersFromUsers){
		
		/**
		 * Inner class for listening to different actions while requesting
		 * mutual friends between the user and an owner of an offer
		 * 
		 * @author passant
		 *
		 */
		class MutualFriendsCheckListener extends OffersFilterListener{

			/**
			 * Constructor
			 * @param offersFromUsers
			 * 		Hash table where:
			 * 		<ul>
			 * 			<li>the <code>key</code> is Facebook ID of requested offer owner</li>
			 * 			<li>the <code>value</code> is object representing offer</li>
			 * 		</ul> 
			 * @param classPackageName
			 * 		class name (package name) where listener is called. It is used
			 * 		for tracing purposes in the log messages	
			 * @param methodName
			 * 		method name where the listener is used. It is used
			 * 		for tracing purposes in the log messages	 
			 */
			public MutualFriendsCheckListener(Hashtable<String, OfferDisplay> offersFromUsers,
					String classPackageName, String methodName) {
				super(offersFromUsers, classPackageName, methodName);
				
			}// end constructor
			
			@Override
			public void processRequest(JSONObject parsedResponse, Object state) {
				// get data relevant to mutual friends
				JSONObject receivedDataOfMutualFriends = extractDataJsonObject(parsedResponse);
				if (receivedDataOfMutualFriends != null){	
					// Get owner ID currently checked for mutual friends
					String ownerId = (String)state;
					generateLogMessage(": onComplete: has mutual friends ownerId: " + ownerId);
					// Get the OfferDisplay of that owner and save mutual friends
					getOfferDisplayBy(ownerId).setFacebookExtraInfo(receivedDataOfMutualFriends);
					addFilteredOfferDisplay(ownerId,OwnerFacebookStatus.FRIEND_OF_FRIEND);
					generateLogMessage(": onComplete: Extra info set for ownerId " + ownerId);		
				}// end if : if owner and user have common friends => FB returns mutual friends between both
			}// end processRequest			
		}// end class
		
		// Used for tracing purposes
		String methodName = "filterOffersFromOwnersWithMutualFriends (Hashtable<String,OfferDisplay> offersFromUsers)";
		
		// Create new listener for friends of friends status
		MutualFriendsCheckListener mutualFriendsCheckListener = new MutualFriendsCheckListener(offersFromUsers,TAG_CLASS_PACKAGE,methodName);
			
		/* Send HTTP request. Graph API path: me/mutualfriends/friendId.  
		 * The only permissions needed for operation to be performed is <access_token>
		 * to be granted 
		 */
		requestOfferFilteringByRelationBetweenAppUserAnd(offersFromUsers, "mutualfriends", mutualFriendsCheckListener, true);
		
		blockThreadUntilAllOffersAreProcessed(mutualFriendsCheckListener.getSemaphore());
		
		return mutualFriendsCheckListener.getFilteredOffers();
	}// end filterOffersFromOwnersWithMutualFriends2
	
	/**
	 * Used to filter all elements of <code>needsFiltering</code> from <code>main</code>
	 * @param main
	 * 		Reference hashtable that will be kept as is
	 * @param needsFiltering
	 * 		Hashtable that will get filtered (elements existing in main will be removed)
	 */
	public static void removeDuplicates(Hashtable<String, OfferDisplay> main , Hashtable<String, OfferDisplay> needsFiltering){
		
		Iterator contentIt = main.keySet().iterator();
		
		while(contentIt.hasNext()){
			String key = (String)contentIt.next();
			
			if (needsFiltering.containsKey(key)){
				needsFiltering.remove(key);
			}// end if : needsFiltering has duplicate from main -> remove
			
		}// end while: remove all elements in main from needsFiltering
		
	}// end removeDuplicates
		
	/**
	 * Helper method for acquiring a semaphore with the try and 
	 * catch needed for handling exceptions
	 * 
	 * @param s
	 * 	Semaphore that will trying acquiring a permit
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
	 * 			<ul>
	 * 				<li>the <code>key</code> is Facebook ID of offer owner</li>
	 * 				<li>the <code>value</code> is object representing offer 
	 * 				and user details needed to display a search result</li>
	 * 			</ul> 
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
	 */
	private void requestOfferFilteringByRelationBetweenAppUserAnd(
			Hashtable<String, OfferDisplay> offersFromUsers, 
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
	 */
	private JSONObject extractDataJsonObject(JSONObject receviedResponse){
		
		// Get friend data	
		JSONArray responseData;
		try {
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
	
	
	// ________________________________TESTING METHODS________________
	
	private void methodTester(){
		tester_filterOffersFromFriends();
		//tester_filterOffersFromOwnersWithMutualFriends();
	}
	
	private void tester_filterOffersFromFriends(){
		
		// Input list: cannot make value = null -> exception
		Hashtable<String, OfferDisplay>offersFromUsers = new Hashtable<String, OfferDisplay>();
		offersFromUsers.put("32529", new OfferDisplay("32529","ofr1",OwnerFacebookStatus.UNRELATED)); 	// friend
		offersFromUsers.put("48304588",new OfferDisplay("48304588","ofr2",OwnerFacebookStatus.UNRELATED)); 	// friend
		offersFromUsers.put("58215973",new OfferDisplay("58215973","ofr3",OwnerFacebookStatus.UNRELATED)); 	// friend
		offersFromUsers.put("1207059", new OfferDisplay("1207059","ofr4",OwnerFacebookStatus.UNRELATED)); 	// non friend
		
		// Expected output : Arraylist having 32529 , 48304588 , 58215973
		// STATUS : SUCCESSFUL
		
		/* Comments: CANNOT TEST ON YOUR OWN ACCOUNTS : user- dependent */
		
		
		// Execute method
		Hashtable<String, OfferDisplay> friendsIds  = filterOffersFromFriends(offersFromUsers);
		
		// Display results
		//String friendsIdsForDisplay = "friends IDs retrieved: ";		
		//for (int i = 0 ; i < friendsIds.size() ; i++)
			//friendsIdsForDisplay += friendsIds.get(i) + " , ";
		
		//Log.e(TAG_CLASS_PACKAGE, "tester_filterOffersFromFriends: " + friendsIdsForDisplay);
		System.out.println("Number of offers from friends: " + friendsIds.size());
		System.out.println("Friends results: " + friendsIds);
	}// end tester_filterOffersFromFriends
	
	private void tester_filterOffersFromOwnersWithMutualFriends(){
		
		// Input list
		OwnerFacebookStatus fbStatus = OwnerFacebookStatus.FRIEND_OF_FRIEND;
		String usrId1 = "32529"; 	// will have mutual friends
		String usrId2 = "48304588";	// will have mutual friends
		String usrId3 = "1207059";	// no mutual friends
		
		OfferDisplay ofr1 = new OfferDisplay(usrId1,"1",fbStatus);
		OfferDisplay ofr2 = new OfferDisplay(usrId2,"2",fbStatus);
		OfferDisplay ofr3 = new OfferDisplay(usrId1,"3",fbStatus);
		
		Hashtable<String, OfferDisplay> offersFromUsers = new Hashtable<String, OfferDisplay>();
		offersFromUsers.put(usrId1, ofr1);
		offersFromUsers.put(usrId2, ofr2);
		offersFromUsers.put(usrId3, ofr3);
		
		// Expected output : Arraylist having ofr1 , ofr2 
		// STATUS : SUCCESSFUL
		
		/* Comments: CANNOT TEST ON YOUR OWN ACCOUNTS : user- dependent
		 * 
		 * Bugs:
		 *  	1) Sync as result is returned before processing all
		 *  	offers -> FIXED
		 *  	2) Data should be extracted from response and not the whole
		 *  	response including pagination -> FIXED
		 *  	
		 */
		
		// call method
		Hashtable<String, OfferDisplay> result = filterOffersFromOwnersWithMutualFriends(offersFromUsers);
		
		Log.e(TAG_CLASS_PACKAGE,"tester_filterOffersFromOwnersWithMutualFriends: result retrieved ");
		System.out.println("Result is: " + result);
		//for (int i=0 ; i<result.size(); i++)
			//Log.e(TAG_CLASS_PACKAGE,"tester_filterOffersFromOwnersWithMutualFriends: OfferDisplay: " + result.get(i));
			//System.out.println(result.get(i));
		
	}// end tester_filterOffersFromOwnersWithMutualFriends
	
	public void tester_removeDuplicates(){
		
		// Input	
		Hashtable<String, OfferDisplay> main = new Hashtable<String, OfferDisplay>();
		main.put("1", new OfferDisplay());
		main.put("2", new OfferDisplay());
		
		Hashtable<String, OfferDisplay> filter = new Hashtable<String, OfferDisplay>();
		filter.put("1", new OfferDisplay());
		filter.put("2", new OfferDisplay());
		filter.put("3", new OfferDisplay("myOwner", "myOffer",OwnerFacebookStatus.FRIEND ));
		
		// Expected output : filter having only 1 element (last one) 
				// STATUS : SUCCESS
				
				/* Comments:
				 *  	
				 */
		
		// call method
		removeDuplicates(main, filter);
		
		for (int i=0 ; i<filter.size() ; i++)
			System.out.println("Result: " + filter);
		
	}
	public void tester(){
		
		Hashtable<String, Object> x=null;
		boolean hasKey = x.containsKey("gjgdgf");
		boolean hasValue = x.containsValue("jhkjdf");
		boolean has = x.contains(new Object());
		
	
		
		/*RequestListener reqList = new RequestListener() {
			
			public ArrayList<String> myArray = new ArrayList<String>();
			
			@Override
			public void onMalformedURLException(MalformedURLException e, Object state) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onIOException(IOException e, Object state) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFileNotFoundException(FileNotFoundException e, Object state) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFacebookError(FacebookError e, Object state) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(String response, Object state) {
				myArray.add("HelloWorld");
				
			}
			
			
			public ArrayList<String> getMyArray(){
				return myArray;
			}
		}; */
		
		class MyRequestListener implements RequestListener{

			private ArrayList<String> myArray = new ArrayList<String>();
			
			public void onComplete(String response, Object state) {
				myArray.add("HelloWorld");
				
			}

			public void onIOException(IOException e, Object state) {
				// TODO Auto-generated method stub
				
			}

			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
				// TODO Auto-generated method stub
				
			}

			public void onMalformedURLException(MalformedURLException e,
					Object state) {
				// TODO Auto-generated method stub
				
			}

			public void onFacebookError(FacebookError e, Object state) {
				// TODO Auto-generated method stub
				
			}
			
		}
		
		MyRequestListener reqList = new MyRequestListener();
		ArrayList<String> arr = reqList.myArray;
		
		
		
		
	}// end tester
	
	private void tester2(){
		
		class Client2 extends SheelMaaayaClient{

			@Override
			public void doSomething() {
				// TODO Auto-generated method stub
				
			}
			
		}// end class
		
		Client2 c= new Client2();
		c.runHttpRequest("path");
	}// end tester2

	
	
}// end class
