package com.sheel.datastructures;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * This class is a data structure used to collect
 * the merging between information of user and offer
 * displayed in the search results
 * 
 * @author passant
 *
 */
public class OfferDisplay {
	
	/**
	 * Enumeration representing possible weight statuses of 
	 * luggage in an offer
	 * 
	 * @author passant
	 *
	 */
	enum OfferWeightStatus{
		/**
		 * Offer owner has less weight, i.e. searching for people
		 * to give him/her luggage
		 */
		LESS,
		/**
		 * Offer owner has more weight, i.e. searching for people
		 * to take from him/her luggage
		 */
		MORE,
		/**
		 * Initial status of an offer before being defined
		 */
		UNDEFINED
	}// end enum
	
	/**
	 * Enumeration representing possible relationships
	 * between app user and an offer owner
	 * 
	 * @author passant
	 *
	 */
	enum OwnerFacebookStatus{
		/**
		 * App user and offer owner are friends
		 */
		FRIEND,
		/**
		 * Offer owner is a friend of friend for the 
		 * app user (they have mutual friends but are
		 * not directly related)
		 */
		FRIEND_OF_FRIEND,
		/**
		 * App user and offer owner are not directly
		 * friends or related through friends but have 
		 * common networks
		 */
		COMMON_NETWORKS,
		/**
		 * App user and offer owner are not related in 
		 * any possible way
		 */
		UNRELATED
	}// end enum
	
	/**
	 * Constant used for tracing purposes "class name (package name)"
	 */
	private final String TAG_PACKAGE_CLASS = "OfferDisplay (com.sheel.datastructures): ";
	
	/**
	 * represents facebook ID of the offer owner
	 */
	private String ownerFacebookId="";
	/**
	 * represents offer ID is(Sheel ma3aya) database
	 */
	private String offerId="";
	/**
	 * represents name used to identify owner of the offer.
	 * This name is usually (First)(Middle)(last) name
	 */
	private String displayName="";
	/**
	 * represents email of user in facebook and used to send
	 * confirmation messages to him/her
	 */
	private String email="";
	/**
	 * represents mobile of offer owner
	 */
	private String mobile="";
	/**
	 * represents number of Kgs in the offer
	 */
	private int numOfKgs=-1;
	/**
	 * Status of offer weight (whether owner has less or 
	 * more weight)
	 */
	private OfferWeightStatus weightStatus = OfferWeightStatus.UNDEFINED;
	/**
	 * Price needed by the owner per Kg
	 */
	private int price=-1;
	/**
	 * Relation between App user and offer owner
	 */
	private OwnerFacebookStatus ownerFbStatus = OwnerFacebookStatus.UNRELATED;
	/**
	 * Holds extra information about facebook relation depending on the 
	 * relation itself (value of <code>ownerFbStatus</code>).
	 * <ul>
	 * 		<li><code>{@link OwnerFacebookStatus#FRIEND}</code> : list is empty</li>
	 * 		<li><code>{@link OwnerFacebookStatus#FRIEND_OF_FRIEND}</code> : list has names of mutual friends</li>
	 *  	<li><code>{@link OwnerFacebookStatus#COMMON_NETWORKS}</code> : list has names of networks</li>
	 *   	<li><code>{@link OwnerFacebookStatus#UNRELATED}</code> : list is empty</li>
	 * </ul>
	 */
	ArrayList<String> facebookExtraInfo= new ArrayList<String>();
	
	
	/**
	 * Constructor for creating object for testing
	 * @param ownerId 
	 * 		Owner facebook ID
	 * @param offerId 
	 * 		Offer ID in app database
	 * @param displayName
	 * 		Name to be displayed for the owner
	 * @param email
	 * 		E-mail used to send owner emails 
	 * @param mobile
	 * 		Mobile number used for calling owner
	 * @param weightS
	 * 		Status of offer weight (whether owner has less or 
	 * 		more weight)
	 * @param numKg
	 * 		Number of Kgs in the offer
	 * @param price
	 * 		Price requested by the owner per Kg
	 * @param facebookS
	 * 		Relation between App user and offer owner
	 */
	public OfferDisplay(
			String ownerId, String offerId , String displayName , 
			String email, String mobile, OfferWeightStatus weightS,
			int numKg , int price , OwnerFacebookStatus facebookS ){
		
		initParameters(ownerId, offerId, displayName, email, mobile, weightS, numKg, price, facebookS);
		
	}// end constructor
	
	/**
	 * Default constructor for initializing the object. 
	 * <ul>
	 * 		<li>All int values are set to -1</li>
	 *  	<li>All String values are set to empty string</li>
	 *   	<li>All boolean values are set to false</li>
	 * </ul>
	 * 
	 */
	public OfferDisplay(){
		// No need to initialize -> already initialized
	}// end constructor
	
	/**
	 * Constructor used to parse data received from the app database
	 * 
	 * @param databaseResponse
	 * 		Database response string. The string must be in JSON format
	 */
	public OfferDisplay(String databaseResponse){
		
		try {
			JSONObject parsedData = new JSONObject(databaseResponse);
			// TODO maged : get each element by id and insert in parameters
			//initParameters(ownerId, databaseResponse, databaseResponse, databaseResponse, databaseResponse, weightS, numKg, price, facebookS);
		} catch (JSONException e) {
			Log.e(TAG_PACKAGE_CLASS,"OfferDisplay(String databaseResponse): could not parse DB respose to JSON object");
			e.printStackTrace();
		}// end catch: in case it could not be parsed
		
	}// end constructor
	
	/** 
	 * Gets the facebook ID of offer owner
	 * 
	 * @return 
	 * 		facebook ID of owner. If not set, returns -1
	 */
	public String getOwnerFacebookId() {
		return ownerFacebookId;
	}// end getOwnerFacebookId

	/**
	 * Gets the offer ID in the app database
	 * @return 
	 * 		Offer ID. If not set, returns -1
	 */
	public String getOfferId() {
		return offerId;
	}// end getOfferId

	/**
	 * Gets the name of the owner to be displayed beside the offer.
	 * Usually the name is (first)(middle)(last) name
	 * 
	 * @return 
	 * 		If not set, returns "" (empty string)
	 */
	public String getDisplayName() {
		return displayName;
	}// end getDisplayName

	/**
	 * Gets the email of the offer owner used for communication
	 * 
	 * @return 
	 * 		If not set, returns "" (empty string)
	 */
	public String getEmail() {
		return email;
	}// end getEmail

	/**
	 * Gets the mobile number of the offer owner in the format
	 * (+)(COUNTRY_CODE)(MOBILE_NUMBER)
	 * 
	 * @return 
	 * 		If not set, returns -1
	 */
	public String getMobile() {
		return mobile;
	}// end getMobile

	/**
	 * Gets the number of Kgs offered by the owner 
	 * 
	 * @return
	 * 		If not set, returns -1		
	 */
	public int getNumberOfKgs() {
		return numOfKgs;
	}// end getNumOfKgs

	/**
	 * Gets the status of offer weight (whether owner has less or 
	 * more weight)
	 * 
	 * @return 
	 *		If not set, returns OfferWeightStatus.UNDEFINED
	 */
	public OfferWeightStatus getWeightStatus() {		
		return weightStatus;
	}// end getWeightStatus

	/**
	 * Gets the offer price requested per Kg
	 * @return 
	 *		If not set, returns -1 
	 */
	public int getPrice() {
		return price;
	}// end getPrice

	/**
	 * Gets the relation between app user and offer owner
	 * @return the ownerFbStatus
	 * 		If not set, returns OwnerFacebookStatus.UNRELATED
	 */
	public OwnerFacebookStatus getOwnerFacebookRelationWithUser() {		
		return ownerFbStatus;
	}// end getOwnerFacebookRelationWithUser

	/**
	 * Holds extra information about facebook relation depending on the 
	 * relation itself (value of <code>ownerFbStatus</code>).
	 * 	
	 * @return 
	 * 		If relation is:
	 *  	<ul>
	 * 			<li><code>{@link OwnerFacebookStatus#FRIEND}</code> : list is empty</li>
	 * 			<li><code>{@link OwnerFacebookStatus#FRIEND_OF_FRIEND}</code> : list has names of mutual friends</li>
	 *  		<li><code>{@link OwnerFacebookStatus#COMMON_NETWORKS}</code> : list has names of networks</li>
	 *   		<li><code>{@link OwnerFacebookStatus#UNRELATED}</code> : list is empty</li>
	 * 		</ul>
	 */
	public ArrayList<String> getFacebookExtraInfo() {
		return facebookExtraInfo;
	}// end getFacebookExtraInfo
	
	@Override
	public String toString() {
		String result =
					"OfferDisplay:"+
					" (ownerFbId= " +this.ownerFacebookId+")"+
					" (offerId= "+ this.offerId + ")" +
					" (ownerName= " + this.displayName + ")"+
					" (Email: " + this.email + ")"+
					" (Mobile: "+ this.mobile+ ")"+
					" (WeightStatus: " + this.weightStatus+")"+
					" (NumberOfKgs: " + this.numOfKgs+")"+
					" (Price: " + this.price+")"+					
					" (FbRelationWithUser: "+this.ownerFbStatus + ")"+
					" \nFbExtraInfo: ";
		
		for (int i=0 ; i<this.facebookExtraInfo.size();i++){
			result += this.facebookExtraInfo.get(i) + "  ";
		}// end for 		
		
		return result;
	}// end toString

	/**
	 * Checks if 2 <code>OfferDisplay</code> objects are 
	 * equal or not. 2 objects are defined to be equal if
	 * they have same offer id.
	 */
	@Override	
	public boolean equals(Object o) {
		
		if (o.getClass() == OfferDisplay.class){
			OfferDisplay castedO = (OfferDisplay)o;
			
			if ( this.offerId == castedO.offerId){
				return true;
			}// end if : same owner and offer and	
			
		}// end if: checked object is the same type

		return false;
	}// end equals
	
	/**
	 * Helper method used to initialize different parameters
	 * @param ownerId 
	 * 		Owner facebook ID
	 * @param offerId 
	 * 		Offer ID in app database
	 * @param displayName
	 * 		Name to be displayed for the owner
	 * @param email
	 * 		E-mail used to send owner emails 
	 * @param mobile
	 * 		Mobile number used for calling owner
	 * @param weightS
	 * 		Status of offer weight (whether owner has less or 
	 * 		more weight)
	 * @param numKg
	 * 		Number of Kgs in the offer
	 * @param price
	 * 		Price requested by the owner per Kg
	 * @param facebookS
	 * 		Relation between App user and offer owner
	 * 		
	 */
	private void initParameters(
			String ownerId, String offerId , String displayName , 
			String email, String mobile, OfferWeightStatus weightS,
			int numKg , int price , OwnerFacebookStatus facebookS){
		
		this.ownerFacebookId = ownerId;
		this.offerId = offerId;
		this.displayName = displayName;
		this.numOfKgs = numKg;
		this.price = price;
		this.mobile = mobile;
		this.email = email;
		this.weightStatus = weightS;
		this.ownerFbStatus = facebookS;	
	}// end initParameters
	
	
	
}// end class
