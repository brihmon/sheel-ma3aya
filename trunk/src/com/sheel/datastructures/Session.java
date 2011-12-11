/**
 * 
 */
package com.sheel.datastructures;

/**
 * This class is used as a shared data structure containing
 * the information needed to be passed and shared between 
 * all activities of the app
 * 
 * @author 
 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
 *
 */
public class Session {

	/**
	 * user ID in facebook to identify home page and other user-relevant
	 * stuff
	 */
	public static String facebookUserId="";
	/**
	 * User access token indicating the user is logged in
	 */
	public static String facebookAccessToken = "";
	/**
	 * Expiry date of valid user access token
	 */
	public static String facebookAccessTokenExpiry = "";
	
	/**
	 * Used to set all the parameters relevant to facebook in one step
	 * 
	 * @param userId
	 * 		facebook ID of the logged-in user
	 * @param accessToken
	 * 		access token used in any query to the facebook API
	 * @param expiry
	 * 		expiry time of the access token (can be used to indicate)
	 * 		whether user is logged in or not
	 *
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public static void saveFaceookParameters(String userId, String accessToken, String expiry){
		facebookUserId = userId;
		facebookAccessToken = accessToken;
		facebookAccessTokenExpiry = expiry;				
	}// end method: 
	
}// end class
