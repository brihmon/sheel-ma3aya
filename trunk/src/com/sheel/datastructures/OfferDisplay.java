package com.sheel.datastructures;

import java.util.ArrayList;

import org.json.JSONArray;

import com.sheel.datastructures.enums.OfferWeightStatus;
import com.sheel.datastructures.enums.OwnerFacebookStatus;

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
	 * Indicates gender. If set to true ==> user is a female.
	 * Else either user is a male or N/A
	 */
	boolean isFemale=false;
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
	
	private String flightNumber = "";
	private String source = "";
	private String destination = "";
	private String nationality = "";
	private int userStatus = -1;
	
	private OwnerFacebookStatus ownerFbStatus = OwnerFacebookStatus.UNRELATED;
		
	/**
	 * Holds extra information about facebook relation depending on the 
	 * relation itself (value of <code>ownerFbStatus</code>). It is the 
	 * parsing of the facebook response to mutual friends request to a
	 * JSON object indexed by different available keys. 
	 * TO get any of the mutual friends, call {@link JSONArray#get(int)}
	 * <ul>
	 * 		<li><code>{@link OwnerFacebookStatus#FRIEND}</code> : object is empty </li>
	 * 		<li><code>{@link OwnerFacebookStatus#FRIEND_OF_FRIEND}</code> : object has names and IDs of mutual friends</li>
	 *  	<li><code>{@link OwnerFacebookStatus#COMMON_NETWORKS}</code> : object has names and IDs of networks</li>
	 *   	<li><code>{@link OwnerFacebookStatus#UNRELATED}</code> : object is empty</li>
	 * </ul>
	 */
	JSONArray facebookExtraInfo = null;
	
	
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
	 * @param isFemale
	 *  	<ul>
	 * 			<li>True: user is a female</li>
	 * 			<li>False: user is a male or N/A</li>
	 * 		</ul>
	 * 		
	 */
	public OfferDisplay(
			String ownerId, String offerId , String displayName , 
			String email, String mobile, OfferWeightStatus weightS,
			int numKg , int price , OwnerFacebookStatus facebookS, boolean isFemale, String nationality, String flightNumber, String source,
			String destination, int userStatus){
		
		initParameters(ownerId, offerId, displayName, email, mobile, weightS, numKg, price, facebookS, isFemale, nationality, 
				flightNumber, source, destination, userStatus);
		
	}// end constructor
	
	/**
	 * Constructor for creating object for testing
	 * @param ownerId 
	 * 		Owner facebook ID
	 * @param offerId 
	 * 		Offer ID in app database	 
	 * @param facebookS
	 * 		Relation between App user and offer owner
	 */
	public OfferDisplay(String ownerId , String offerId, OwnerFacebookStatus facebookS){
		initParameters(ownerId, offerId, "", "", "", OfferWeightStatus.LESS, -1, -1, facebookS,false, "",
				"", "", "", -1);
	}// end constructor
	
	/**
	 * Constructor for creating object for testing
	 * @param ownerId 
	 * 		Owner facebook ID
	 * @param offerId 
	 * 		Offer ID in app database	 
	 * @param displayName
	 * 		name of offer owner
	 */
	public OfferDisplay(String ownerId , String offerId , String displayName){
		initParameters(ownerId, offerId, displayName, "", "", OfferWeightStatus.UNDEFINED, -1, -1, OwnerFacebookStatus.UNRELATED,false,
				"", "", "", "", -1);
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
	 * Gets the gender of a user
	 * @return 
	 * <ul>
	 * 		<li>True: user is a female</li>
	 * 		<li>False: user is a male or N/A</li>
	 * </ul>
	 */
	public boolean isFemale() {
		return isFemale;
	}// end isFemale

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
	
	public String getFlightNumber() {
		return flightNumber;
	}// end getMobile
	
	public String getSource() {
		return source;
	}
	
	public String getDestination() {
		return destination;
	}
	
	public String getNationality() {
		return nationality;
		}
	
	public int getUserStatus() {
		
		return userStatus;
	}

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
	 * 		<ul>
	 * 			<li><code>{@link OwnerFacebookStatus#FRIEND}</code> : object is empty </li>
	 * 			<li><code>{@link OwnerFacebookStatus#FRIEND_OF_FRIEND}</code> : object has names
	 * 			and IDs of mutual friends where each (id-name) form an object with index from o-n</li>
	 *  		<li><code>{@link OwnerFacebookStatus#COMMON_NETWORKS}</code> : object has names 
	 *  		and IDs of common networks where each (id-name) form an object with index from o-n</li>
	 *   		<li><code>{@link OwnerFacebookStatus#UNRELATED}</code> : object is empty</li>
	 * 		</ul>
	 */
	public JSONArray getFacebookExtraInfo() {
		return facebookExtraInfo;
	}// end getFacebookExtraInfo	
	
	/**
	 * Formats the extra info received from @link {@link OfferDisplay#getFacebookExtraInfo()} 
	 * to a more understandable list
	 * 
	 * @return
	 * 		If relation is:
	 * 		<ul>
	 * 			<li><code>{@link OwnerFacebookStatus#FRIEND}</code> : list is empty </li>
	 * 			<li><code>{@link OwnerFacebookStatus#FRIEND_OF_FRIEND}</code> : list has names of mutual friends</li>
	 *  		<li><code>{@link OwnerFacebookStatus#COMMON_NETWORKS}</code> : list has names of common networks</li>
	 *   		<li><code>{@link OwnerFacebookStatus#UNRELATED}</code> : list is empty</li>
	 * 		</ul>
	 */
	public ArrayList<String> parseFacebookExtraInfo(){
		// TODO implement later
		return null;		
	}// end parseFacebookExtraInfo
	
	/**
	 * Holds extra information about facebook relation depending on the 
	 * relation itself (value of <code>ownerFbStatus</code>). 
	 * See @link {@link OfferDisplay#getFacebookExtraInfo()}
	 * 
	 * @param response
	 * 		response received from the facebook database on a certain request
	 * 		after extracting its data field.
	 */
	public void setFacebookExtraInfo(JSONArray response){
		this.facebookExtraInfo =response;
	}// end setFacebookExtraInfo
	
	/**
	 * Used to define the facebook relation between app user and offer owner
	 * 
	 * @param status
	 * 		one of the predefined statues
	 */
	public void setFacebookStatus(OwnerFacebookStatus status){
		this.ownerFbStatus = status;
	}// end setFacebookStatus
	
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
					" \n(FbExtraInfo: ";
		if (this.facebookExtraInfo != null)
			//result += this.facebookExtraInfo.toString()+")";
			result+= "There is extra info";
		else
			result += "No extra info available)";
		// TODO print the extra facebook properties
		/*for (int i=0 ; i<this.facebookExtraInfo.size();i++){
			result += this.facebookExtraInfo.get(i) + "  ";
		}// end for 	*/	
		
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
			int numKg , int price , OwnerFacebookStatus facebookS, boolean isFemale, String nationality,
			String flightNumber, String source, String destination, int userStatus){

		this.ownerFacebookId = ownerId;
		this.offerId = offerId;
		this.displayName = displayName;
		this.numOfKgs = numKg;
		this.price = price;
		this.mobile = mobile;
		this.email = email;
		this.weightStatus = weightS;
		this.ownerFbStatus = facebookS;	
		this.isFemale = isFemale;
		this.flightNumber = flightNumber;
		this.source = source;
		this.destination = destination;
		this.nationality = nationality;
		this.userStatus = userStatus;
	}// end initParameters
	
	
	
}// end class
