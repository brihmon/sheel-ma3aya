package com.sheel.listeners;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.Semaphore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;
import com.sheel.datastructures.OfferDisplay2;
import com.sheel.datastructures.enums.OwnerFacebookStatus;

/**
 * Listener used to handle filtering multiple offers using
 * Facebook connections to schedule returning all the filtered
 * results together but process each offer parallel to the other
 * 
 * @author passant
 *
 */
public class OffersFilterListener implements RequestListener{

	//_____________________ Constants ____________________________

	/**
	 * Used for tracing purposes
	 */
	private final String TAG_CLASS_PACKAGE;
	/**
	 * Used for tracing purposes
	 */
	private final String METHOD_NAME;
	
	//_____________________ Instance parameters __________________
	
	/**
	 *  List of all available offers from different owners.
	 *  <code>Key</code> is the facebook ID of the offer owner. 
	 *  <code>value</code> list of offers from this owner. 
	 */
	private Hashtable<String,ArrayList<OfferDisplay2>> offersFromUsers;
	/**
	 *  List of offers after filtering according to a certain condition
	 */
	private ArrayList<OfferDisplay2> filteredOffers = new ArrayList<OfferDisplay2>();
	/**
	 * List of offer owners appearing in the results
	 */
	private ArrayList<String> filteredOwnersOfOffers = new ArrayList<String>();
	
	/**
	 * Total number of offers that must be processed. It is used for scheduling
	 * reasons to know when to allow return of result from the method
	 */
	private int totalNumberOfOffersRemaining =-1;
	/**
	 * Semaphore to indicate when to wake up the method thread and allow it 
	 * to return results. It is used to prevent returning of uncompleted result
	 * from the method because the thread executing the HTTP calls hasn't 
	 * finished yet
	 */
	private final Semaphore waitForAllOffersProcessing = new Semaphore(0);
	

	//_____________________ Constructor __________________________

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
	public OffersFilterListener( Hashtable<String, ArrayList<OfferDisplay2>> offersFromUsers, String classPackageName,String methodName){
		
		this.TAG_CLASS_PACKAGE = classPackageName;
		this.METHOD_NAME = methodName;
		
		this.offersFromUsers = offersFromUsers;
		this.totalNumberOfOffersRemaining = offersFromUsers.size();
		
	}// end constructor
	
	//_____________________ Different Actions _____________________

	public void onComplete(String response, Object state) {
		
		try {
			// Parse received data
			JSONObject receivedData = new JSONObject(response);
			Log.e(TAG_CLASS_PACKAGE,METHOD_NAME+": onComplete:" + "Offer start to process from OffersFilterListener" );
						
			// Do logic
			processRequest(receivedData, state);
			
			// Reduce counter
						
			/*
			 * Location of variable is CRITICAL
			 * It CANNOT be in the beginning of the onComplete because
			 * the result might fight before all offers owners are 
			 * processed.
			 * CONSIDER the following sequence 2 offer owners should be evaluated: 
			 * 		1) owner1 onComplete 1st line (counter --) 
			 * 		2) owner2 onComplete 1st line (counter --) 
			 * 		3) owner1 onComplete rest of method
			 * 			if statement (counter ==0 ) -> true -> release
			 * 			ALTHOUGH owner2 is NOT processed yet
			 */
			this.totalNumberOfOffersRemaining --;
			Log.e(TAG_CLASS_PACKAGE,METHOD_NAME+" totalNumberOfOffersRemaining: " + this.totalNumberOfOffersRemaining);
			

			// Check semaphore			
			if (totalNumberOfOffersRemaining == 0){	
				Log.e(TAG_CLASS_PACKAGE,METHOD_NAME+": onComplete:" + "Semaphore will be released ");
				waitForAllOffersProcessing.release();						
			}// end if : all offers are processed -> release semaphore to return result
						
		} catch (JSONException e) {
			Log.e(TAG_CLASS_PACKAGE,METHOD_NAME+":onComplete: Exception in parsing received data");
			e.printStackTrace();
		}// end catch
		
	}// end onComplete

	public void onIOException(IOException e, Object state) {
		Log.e(TAG_CLASS_PACKAGE,METHOD_NAME+":onIOException");
		e.printStackTrace();					
	}// end onIOException

	public void onFileNotFoundException(FileNotFoundException e,Object state) {
		Log.e(TAG_CLASS_PACKAGE,METHOD_NAME+":onFileNotFoundException");
		e.printStackTrace();				
	}// end onFileNotFoundException

	public void onMalformedURLException(MalformedURLException e,
			Object state) {
		Log.e(TAG_CLASS_PACKAGE,METHOD_NAME+":onMalformedURLException");
		e.printStackTrace();				
	}// end onMalformedURLException

	public void onFacebookError(FacebookError e, Object state) {
		Log.e(TAG_CLASS_PACKAGE,METHOD_NAME+":onFacebookError");
		e.printStackTrace();					
	}// end onFacebookError	
	
	/**
	 * This method is used to do all logic needed on receiving
	 * a response from the facebook server. It is called inside 
	 * the (onComplete) method.
	 * 
	 * This method should be overridden to provide logic needed
	 * 
	 * @param parsedResponse
	 * 		JSONObject representing the parsed response of the 
	 * 		facebook server.
	 * @param state
	 * 		Object sent with the request. It is variable and 
	 * 		flexible. Developers use it to send any data across
	 * 		with the request to be available for processing on
	 * 		receiving server response. 
	 */
	public void processRequest(JSONObject parsedResponse, Object state){
		
	}// end processRequest
	
	/**
	 * Returns total number of remaining offers to be processed.
	 * 
	 * @return
	 * 		total number of remaining offers to be processed
	 * 		for filtering
	 */
	public int getTotalNumberOfRemainingOffers(){
		return this.totalNumberOfOffersRemaining;
	}// end getTotalNumberOfRemainingOffers
	
	/**
	 * Returns semaphore used to wait for processing all offers
	 * before returning a final result. 
	 * 
	 * Note that this should be used very carefully. 
	 * 
	 * <br><b>Main use case:</b><br>
	 * When it is needed to stop the method where the listener is 
	 * initialized, call @link {@link Semaphore#acquire()}
	 * 
	 * @return
	 * 		Semaphore with 0 available permits
	 */
	public Semaphore getSemaphore() {
		return this.waitForAllOffersProcessing;
	}// end getSemaphore 
	
	/**
	 * Returns List of filtered offers according to logic
	 * implemented by developer in
	 * @link {@link OffersFilterListener#processRequest(JSONObject, Object)}.
	 * 
	 * @return
	 * 		List of filtered offers.  The list is ordered where all offers 
	 * 		of the same owner are after each other. If no offers are chosen
	 * 		due to filtering conditions, empty list is returned.
	 */
	public ArrayList<OfferDisplay2> getFilteredOffers(){
		return this.filteredOffers;
	}// end getFilteredOffers
	

	/**
	 * Returns List of filtered offers owners according to logic
	 * implemented by developer in
	 * {@link OffersFilterListener#processRequest(JSONObject, Object)}.
	 * <b>The list has no duplicates.</b>
	 * 
	 * @return
	 * 		List of filtered offers owners facebook IDs. The list
	 * 		is a set, i.e. has no duplicates
	 */
	public ArrayList<String> getFilteredOffersOwners(){
		return this.filteredOwnersOfOffers;
	}// end getFilteredOffers
	
	/**
	 * Gets a certain offer by its owner ID
	 * @param ownerId
	 * 		offer owner Facebook ID 
	 * @return
	 * 		List of hybrid objects used representing one
	 * 		search result (offer) by this owner
	 */
	public ArrayList<OfferDisplay2> getOfferDisplayBy(String ownerId){
		return this.offersFromUsers.get(ownerId);
	}// end getOfferDisplayBy
	
	/**
	 * Used to add filtered offers of a certain owner to the result. 
	 * Offers are retrieved and updated automatically using their 
	 * owner facebook ID
	 * 
	 * @param ownerId
	 * 		facebook ID of offer owner
	 * @param status
	 * 		defines relation between app user and offer owner
	 * Holds extra information about facebook relation depending on the 
	 * relation itself (value of <code>ownerFbStatus</code>).
	 * <b>Example</b>:It is the parsing of the facebook response to mutual 
	 * friends request to a  JSON object indexed by different available keys. 
	 * To get any of the mutual friends, call {@link JSONArray#get(int)}
	 * 	
	 * @return 
	 * 		If relation is:
	 * 		<ul>
	 * 			<li><code>{@link OwnerFacebookStatus#FRIEND}</code> : object is empty </li>
	 * 			<li><code>{@link OwnerFacebookStatus#FRIEND_OF_FRIEND}</code> : object has names
	 * 			and IDs of mutual friends where each (id-name) form an object with index from o-n</li>
	 *  		<li><code>{@link OwnerFacebookStatus#COMMON_NETWORKS}</code> : object has names 
	 *  		and IDs of common networks where each (id-name) form an object with index from o-n</li>
	 *   		<li><code>{@link OwnerFacebookStatus#UNRELATED}</code> : object is empty</li>
	 * 		</ul>
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 * 		
	 */
	public void addFilteredOfferDisplay(String ownerId, OwnerFacebookStatus status, JSONArray response){
		/* Add the facebook information (status + extra) to all offers
		 * relevant to this owner ID
		 */
		OfferDisplay2 currentOffer=null;
		ArrayList<OfferDisplay2> offersFromThisOwner = this.offersFromUsers.get(ownerId);
		for (int i=0 ; i<offersFromThisOwner.size() ; i++) {
			currentOffer = offersFromThisOwner.get(i);
			currentOffer.setFacebookStatus(status);
			currentOffer.setFacebookExtraInfo(response);
		}// end for: set facebook parameters to each
		
		/*
		 * Add Owner ID to filtered owners if it does not exist
		 */
		if (!filteredOwnersOfOffers.contains(ownerId)) {
			filteredOwnersOfOffers.add(ownerId);
		}// end if 
		
		/*
		 * Add all these offers to the output list
		 */
		this.filteredOffers.addAll(offersFromThisOwner);
	}// end addFilteredOffer
	
	/**
	 * Used to generate log messages with default format
	 * Message format is:<br>
	 * <ul>
	 * 		<li>Tag : className (packageName)</li>
	 * 		<li>Message : methodName: msg</li>
	 * </ul>
	 * @param msg
	 * 		message displayed in the log
	 */
	public void generateLogMessage(String msg){
		Log.e(TAG_CLASS_PACKAGE,METHOD_NAME+": "+msg);
	}// end generateLogMessage
}// end class