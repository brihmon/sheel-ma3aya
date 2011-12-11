package com.sheel.listeners;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.concurrent.Semaphore;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;
import com.sheel.datastructures.OfferDisplay;
import com.sheel.datastructures.enums.OwnerFacebookStatus;

/**
 * Listener used to handle filtering multiple offers using
 * Facebook connections to schedule returning all the filtered
 * results together but process each offer parrallel to the other
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
	 *  List of all available offers from different owners
	 */
	private Hashtable<String,OfferDisplay> offersFromUsers;
	/**
	 *  List of offers after filtering according to a certain condition
	 */
	private Hashtable<String,OfferDisplay> filteredOffers = new Hashtable<String,OfferDisplay>();
	
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
	 * 			<li>the <code>value</code> is object representing offer</li>
	 * 		</ul> 
	 * @param classPackageName
	 * 		class name (package name) where listener is called. It is used
	 * 		for tracing purposes in the log messages	
	 * @param methodName
	 * 		method name where the listener is used. It is used
	 * 		for tracing purposes in the log messages	 
	 */
	public OffersFilterListener( Hashtable<String, OfferDisplay> offersFromUsers, String classPackageName,String methodName){
		
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
	 * This method should be overriden to provide logic needed
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
	 * @link {@link OffersFilterListener#processRequest(JSONObject, Object)}
	 * 
	 * @return
	 * 		Hash table where:
	 * 		<ul>
	 * 			<li>the <code>key</code> is Facebook ID of requested offer owner</li>
	 * 			<li>the <code>value</code> is hybrid object containing important data
	 * 			about offer and user for displaying a search result</li>
	 * 		</ul> 
	 * 		If no offers are chosen due to filtering conditions, 
	 * 		empty hash table is returned.
	 */
	public Hashtable<String,OfferDisplay> getFilteredOffers(){
		return this.filteredOffers;
	}// end getFilteredOffers
	
	/**
	 * Gets a certain offer by its owner ID
	 * @param ownerId
	 * 		offer owner Facebook ID 
	 * @return
	 * 		Offer/user hubrid object containing data 
	 * 		used in displaying a search result
	 */
	public OfferDisplay getOfferDisplayBy(String ownerId){
		return this.offersFromUsers.get(ownerId);
	}// end getOfferDisplayBy
	
	/**
	 * Used to add a filtered offer to the result. An offer
	 * is mapped to its owner ID as its key and it automatically
	 * retrieved from the full list of offers
	 * 
	 * @param ownerId
	 * 		facebook ID of offer owner
	 * @param status
	 * 		defines relation between app user and offer owner
	 * 		
	 */
	public void addFilteredOfferDisplay(String ownerId, OwnerFacebookStatus status){
		this.offersFromUsers.get(ownerId).setFacebookStatus(status);
		this.filteredOffers.put(ownerId, this.offersFromUsers.get(ownerId));		
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