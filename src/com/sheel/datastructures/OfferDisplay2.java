package com.sheel.datastructures;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.sheel.datastructures.enums.OwnerFacebookStatus;


/**
 * This class is a data structure used to collect
 * the merging between information of user and offer
 * and flight displayed in the search results
 * 
 * @author Magued George (magued.george1990@gmail.com)
 * @author
 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
 *
 */
public class OfferDisplay2 {	
	
	/**
	 * Constant used for tracing purposes "class name (package name)"
	 */
	private final String TAG_PACKAGE_CLASS = "OfferDisplay2 (com.sheel.datastructures): ";
	
	
	private User user;
	private Flight flight;
	private Offer offer;
	
	/**
	 * The relation between app user and offer owner. See {@link OwnerFacebookStatus}
	 * enumeration for available options
	 */
	private OwnerFacebookStatus ownerFbStatus = OwnerFacebookStatus.UNRELATED;
	
	/**
	 * Holds extra information about facebook relation depending on the 
	 * relation itself (value of <code>ownerFbStatus</code>). <br>
	 * <ul>
	 * 		<li><code>{@link OwnerFacebookStatus#FRIEND}</code> : object is empty </li>
	 * 		<li><code>{@link OwnerFacebookStatus#FRIEND_OF_FRIEND}</code> : object has names and IDs of mutual friends</li>
	 *  	<li><code>{@link OwnerFacebookStatus#COMMON_NETWORKS}</code> : object has names and IDs of networks</li>
	 *   	<li><code>{@link OwnerFacebookStatus#UNRELATED}</code> : object is empty</li>
	 * </ul>
	 * <b>Example</b>:It is the 
	 * parsing of the facebook response to mutual friends request to a
	 * JSON object indexed by different available keys. 
	 * To get any of the mutual friends, call {@link JSONArray#get(int)}
	 */
	JSONArray facebookExtraInfo = null;
	
	public OfferDisplay2(User user, Flight flight, Offer offer) {
		
		this.user = user;
		this.flight = flight;
		this.offer = offer;
	}
	
	/**
	 * Constructor for creating object for testing
	 * @param ownerId 
	 * 		Owner facebook ID
	 * @param offerId 
	 * 		Offer ID in app database	 
	 * @param displayName
	 * 		name of offer owner
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)	
	 */
	public OfferDisplay2(String ownerId , long offerId , String displayName){
		this.offer = new Offer(offerId);
		this.user = new User(ownerId, displayName);
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
	public OfferDisplay2(String ownerId , long offerId, OwnerFacebookStatus facebookS){
		this.offer = new Offer(offerId);
		this.user = new User(ownerId, "user"+ownerId);
		this.ownerFbStatus = facebookS;
	}// end constructor
	
	/**
	 * Gets the owner of the offer displayed as a search result
	 *
	 * @return 
	 * 		User model from the database representing all needed info
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)	
	 */
	public User getUser() {
		return this.user;
	}// end getUser


	/**
	 * Gets the flight where the offer displayed as a search result
	 * is available
	 * 
	 * @return 
	 * 		Flight model from the database representing all needed info
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)	
	 */
	public Flight getFlight() {
		return this.flight;
	}// end getFlight


	/**
	 * Gets the offer displayed as a search result
	 * 
	 * @return 
	 * 		Offer model from the database representing all needed info
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)	
	 */
	public Offer getOffer() {
		return this.offer;
	}// end getOffer


	/**
	 * Gets the name of the owner to be displayed beside the offer.
	 * Usually the name is (first)(middle)(last) name
	 * 
	 * @return 
	 * 		If not set, returns "" (empty string)
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public String getDisplayName() {
		return user.firstName + " "+ user.middleName + " "+user.lastName;
	}// end getDisplayName
	
	/**
	 * Gets the relation between app user and offer owner
	 * 
	 * @return the ownerFbStatus
	 * 		If not set, returns {@link OwnerFacebookStatus#UNRELATED}. 
	 * 		See {@link OwnerFacebookStatus} enumeration for 
	 * 		other available options.
	 * 
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public OwnerFacebookStatus getOwnerFacebookRelationWithUser() {		
		return ownerFbStatus;
	}// end getOwnerFacebookRelationWithUser

	/**
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
	 */
	public JSONArray getFacebookExtraInfo() {
		return facebookExtraInfo;
	}// end getFacebookExtraInfo
	
	/**
	 * Holds extra information about facebook relation depending on the 
	 * relation itself (value of <code>ownerFbStatus</code>). 
	 * See @link {@link OfferDisplay2#getFacebookExtraInfo()}
	 * 
	 * @param response
	 * 		response received from the facebook database on a certain request
	 * 		after extracting its data field.
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public void setFacebookExtraInfo(JSONArray response){
		this.facebookExtraInfo =response;
	}// end setFacebookExtraInfo
	
	/**
	 * Used to define the facebook relation between app user and offer owner
	 * 
	 * @param status
	 * 		one of the predefined statues. See {@link OwnerFacebookStatus} 
	 * 		enumeration for available options.
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public void setFacebookStatus(OwnerFacebookStatus status){
		this.ownerFbStatus = status;
	}// end setFacebookStatus
	
	/**
	 * Used to parse the extra data provided by facebook about
	 * an offer owner
	 * 
	 * @return
	 * 		If relation between offer owner and app user is:
	 * 		<ul>
	 * 			<li><code>{@link OwnerFacebookStatus#FRIEND}</code> : null </li>
	 * 			<li><code>{@link OwnerFacebookStatus#FRIEND_OF_FRIEND}</code> : String array
	 * 			of mutual friends names</li>
	 *  		<li><code>{@link OwnerFacebookStatus#COMMON_NETWORKS}</code> : String array
	 * 			of common networks</li>
	 *   		<li><code>{@link OwnerFacebookStatus#UNRELATED}</code> : null</li>
	 * 		</ul>
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public String[] parseFacebookExtraInfo() {

		JSONArray data = this.getFacebookExtraInfo();

		if (data == null)
			return null;

		String[] mutualFriends = new String[data.length()];

		String mutualFriendName = "";

		for (int i = 0; i < data.length(); i++) {
			try {
				mutualFriendName = (data.getJSONObject(i)).getString("name");
			} catch (JSONException e) {
				mutualFriendName = "";
				e.printStackTrace();
			}
			mutualFriends[i] = mutualFriendName;
		}// end for: get names of the mutual friends for displaying

		return mutualFriends;

	}// end parseFacebookExtraInfo

	@Override
	public String toString() {
		// TODO : implement properly ==> just quick implementation for testing
		return "[UserFbID: " + this.user.getFacebookId() + 
				" , displayName: " + this.getDisplayName() + 
				" , offerId: " + this.offer.getId() + "]";
	}
	
	/**
	 * Checks if 2 <code>OfferDisplay</code> objects are 
	 * equal or not. 2 objects are defined to be equal if
	 * they have same offer id.
	 * If the input object is not a compatible objects, it
	 * returns false
	 * 
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	@Override
	public boolean equals(Object o) {
		if (o.getClass() != OfferDisplay2.class)
			return false;
		
		OfferDisplay2 comparedOffer = (OfferDisplay2)o;
		if (comparedOffer.offer.id == this.offer.id)
			return true;
		else
			return false;
	}// end equals
	
	/**
	 * Used for mapping the offer from JSONObject into a normal OfferDisplay2
	 * 
	 * @param
	 * 		offerJSON JSON Object sent to be converted to a normal offer
	 * @return OfferDisplay2 Object from the JSONObject
	 * 
	 * @author Magued George (magued.george1990@gmail.com)
	 */
	static int i = 0;
	public static OfferDisplay2 mapOffer(JSONObject offerJSON, String[] airports, String[] nationalities)
	{
		try {
			
			Log.e("Map Offer: ", (i++) + "" );// 0
			
			JSONObject userJSON = offerJSON.getJSONObject("user");
			
			Log.e("Map Offer: ", (i++) + "" );// 1
			
			JSONObject flightJSON = offerJSON.getJSONObject("flight");
			Log.e("Map Offer: ", (i++) + "" );// 2
			
			String ownerId = userJSON.getString("facebookAccount");
			String firstName = userJSON.getString("firstName");
			String middleName = userJSON.getString("middleName");
			String lastName = userJSON.getString("lastName");
			String email = userJSON.getString("email");
			String mobile = userJSON.getString("mobileNumber");
			String gender = userJSON.getString("gender");
			String nationality = userJSON.getString("nationality");
			
			Log.e("Map Offer: ", (i++) + "" );// 3
			
			nationality = nationalities[Integer.parseInt(nationality)];
			
			Log.e("Map Offer: ", (i++) + "" );// 4
			
			User user = new User(ownerId, firstName, middleName, lastName, "", "", email, mobile, gender, nationality);
			
			Log.e("Map Offer: ", (i++) + "" );// 5
			
			Long offerId = offerJSON.getLong("id");
			String offerstatus = offerJSON.getString("offerStatus");
			int userstatus = offerJSON.getInt("userStatus");
			int kgs = offerJSON.getInt("noOfKilograms");
			int price = offerJSON.getInt("pricePerKilogram");
			
			Offer offer = new Offer(offerId, kgs, price, userstatus, offerstatus);
			
			Log.e("Map Offer: ", (i++) + " offer: " + offer.getId() );// 6
			String flightNumber;
		try
		{
			flightNumber = flightJSON.getString("flightNumber");
			Log.e("Map Offer: ", (i++) + " offer: " + offer.getId() );// 7
		}
		catch (Exception e) {
			// TODO: handle exception
			flightNumber = "";
			Log.e("Map Offer: ", (i++) + " offer: " + offer.getId() );// 8
		}
		String source = "";
		String destination = "";
		String departureDate = "";
		try
		{
			source = flightJSON.getString("source");
			Log.e("Map Offer: ", (i++) + " offer: " + offer.getId() );// 9
			
		}
		catch (Exception e) {
			// TODO: handle exception
			Log.e("Map Offer: ", (i++) + " offer: " + offer.getId() );// 10
		}
		try
		{
			 destination = flightJSON.getString("destination");
			 departureDate = flightJSON.getString("departureDate");
		} 
		catch (Exception e) {
			// TODO: handle exception
			Log.e("Map Offer: ", (i++) + " offer: " + offer.getId() );// 11
		}
			
			String sourceNew = airports[Integer.parseInt(source)];
			String destinationNew = airports[Integer.parseInt(destination)];
				
			Flight flight = new Flight(flightNumber, sourceNew, destinationNew, departureDate);
			
			Log.e("Map Offer: ", (i++) + "" );// 12
			
			i = 0;
			return new OfferDisplay2(user, flight, offer);

			
		} 
	catch (JSONException e) {
			// TODO: handle exception
			Log.e("Map Offer: ", (i++) + "" );// 12
			i = 0;
			return null;
		}
	}
	
	
}// end class
