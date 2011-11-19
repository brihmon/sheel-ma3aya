package com.sheel.datastructures.enums;

/**
 * Collection of information always passed between all activities
 * 
 * @author passant
 *
 */
public enum SharedValuesBetweenActivities {
	/**
	 * Facebook ID of logged-in user
	 */
	userFacebookId,
	/**
	 * Facebook access token of the user 
	 */
	userAccessToken,
	/**
	 * Number of remaining seconds till session expire for user 
	 */
	accessTokenExpiry	
	
}// end SharedValuesBetweenActivities
