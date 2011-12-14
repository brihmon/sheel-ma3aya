package com.sheel.datastructures;

import org.json.JSONArray;

import com.sheel.datastructures.enums.OwnerFacebookStatus;


/**
 * This class is a data structure used to collect
 * the merging between information of user and offer
 * and flight displayed in the search results
 * 
 * @author magued
 * @author
 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
 *
 */
public class OfferDisplay2 extends OfferDisplay{	
	
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
	
	public OfferDisplay2(User user, Flight flight, Offer offer) {
		
		this.user = user;
		this.flight = flight;
		this.offer = offer;
	}
	
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
		return user.firstName + user.middleName + user.lastName;
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
	 * 
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
	
	
	
}// end class
