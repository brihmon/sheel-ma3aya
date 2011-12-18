package com.sheel.utils;

import com.sheel.datastructures.enums.OfferWeightStatus;

/**
 * Interface for inserting all the constants of SheelMaayaa
 * To use this interface, you should import as follows:
 * <code>import static com.sheel.datastructures.enums.SheelMaayaaConstants.*</code>
 * 
 * @author Hossam_Amer
 *
 */

public interface SheelMaayaaConstants {

	
	
	/**
	 * Constants used for IntentService accessibility
	 * 
	 */
	
	/*
	 * pathKey: Used as a key for the path variable to be added in the intentService 
	 * HTTP_RESPONSE: Used as a key for the response string retrieved from the server
	 * HTTP_STATUS: Used as a key for the response status retrieved from the server
	 * SERVER: SheelMa3aya Server URL
	 */
	final String pathKey = "path";
	final String HTTP_RESPONSE = "HTTP_RESPONSE";
	final String HTTP_STATUS = "HTTP_STATUS";
	final String SERVER  = "http://sheelmaaayaa.appspot.com";
	
	//======================================================================
	
	/**
	 * Constants used for checking inside the SearchListAdapter
	 * 
	 */
	
	final String female = "female";
	final int OfferWeightStatus_LESS = 0;
	final int OfferWeightStatus_MORE = 1;
	
	
}
