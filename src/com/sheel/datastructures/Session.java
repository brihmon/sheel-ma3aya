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
	 * User ID in facebook to identify home page and other user-relevant
	 * stuff
	 */
	public static String facebookUserId="";
	/**
	 * User access token indicating the user is logged in
	 */
	public static String facebookAccessToken = "";
	/**
	 * Remaining millisecs till the access token of facebook expires
	 */
	public static long facebookAccessTokenExpiry ;
	
	/**
	 * Used to set all the parameters relevant to facebook in one step
	 * 
	 * @param userId
	 * 		facebook ID of the logged-in user
	 * @param accessToken
	 * 		access token used in any query to the facebook API
	 * @param expiry
	 * 		number of millisecs remaining till the access token expires
	 * 	    (can be used to indicate whether user is logged in or not)
	 *
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public static void saveFaceookParameters(String userId, String accessToken, long expiry){
		facebookUserId = userId;
		facebookAccessToken = accessToken;
		facebookAccessTokenExpiry = expiry;				
	}// end saveFaceookParameters
	
	/**
	 * Used to set all the parameters relevant to facebook retrieved
	 * from the login process
	 *
	 * @param accessToken
	 * 		access token used in any query to the facebook API
	 * @param expiry
	 * 		number of millisecs remaining till the access token expires
	 * 	    (can be used to indicate whether user is logged in or not)
	 *
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public static void saveFaceookParameters(String accessToken, long expiry){
		saveFaceookParameters("", accessToken, expiry);
	}// end saveFaceookParameters 
	
	/**
	 * Represents information inside the session is readable way
	 * 
	 * @return 
	 * 		string representing informating inside the session to be displayed
	 *
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public static String showSessionInformation() {
		return "Fb ID= " + facebookUserId + " , access token = "+ facebookAccessToken + " , initial expiry = "+ facebookAccessTokenExpiry;
	}// end showSessionInformation
	
	@Override
	public String toString() {
		return "Fb ID= " + facebookUserId + " , access token = "+ facebookAccessToken + " , initial expiry = "+ facebookAccessTokenExpiry;
	}// end toString
}// end class
