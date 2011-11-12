/**
 * 
 */
package com.sheel.datastructures;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;


/**
 * This class is a data structure for collecting information
 * about a Facebook user relevant to the app. For more information
 * about other available information 
 * @see http://developers.facebook.com/docs/reference/api/user/
 * 
 * @author passant
 *
 */
public class FacebookUser {
	
	/**
	 * Constant used for tracing purposes "class name (package name)"
	 */
	private final String TAG_PACKAGE_CLASS = "FacebookUser (com.sheel.datastructures): ";
	
	/**
	 * Numeric user ID in facebook to identify home page and other user-relevant
	 * stuff
	 */
	private int userId=-1;
	/**
	 * First name of user
	 */
	private String firstName="";
	/**
	 * Middle name of user
	 */
	private String middleName="";
	/**
	 * Last name of user
	 */
	private String lastName="";
	/**
	 * Indicates gender. If set to true ==> user is a female.
	 * Else either user is a male or N/A
	 */
	boolean isFemale=false;
	/**
	 * variable indicating trust worthiness of user.
	 * see {@link #isTrustWorthyUser()} for understanding the
	 * logical value
	 */
	private boolean isVerified = false;
	/**
	 * represents email of user in facebook and used to send
	 * confirmation messages to him/her
	 */
	private String email="";	
	
	/**
	 * Creates a new facebook user by analyzing data returned from
	 * the facebook page 
	 * @param responseJsonString response returned by the facebook on
	 * requesting after authentication is granted. Response must be in 
	 * JSON format. 
	 */
	public FacebookUser(String responseJsonString){
		
		try {
			
			// Parse the JSON string to key-value map
			JSONObject parsedValues = new JSONObject(responseJsonString);	
			
			// Get user ID
			if (parsedValues.has("id")){
				this.userId = parsedValues.getInt("id");
			}// end if: check if parameter exists
									
			// Get first name
			if (parsedValues.has("first_name")){
				this.firstName = parsedValues.getString("first_name");
			}// end if: check if parameter exists
							
			// Get middle name
			if (parsedValues.has("middle_name")){
				this.middleName = parsedValues.getString("middle_name");
			}// end if: check if parameter exists
						
			// Get last name
			if (parsedValues.has("last_name")){
				this.lastName = parsedValues.getString("last_name");
			}// end if: check if parameter exists			
					
			// Get gender
			if (parsedValues.has("gender")){
				String genderTmp= parsedValues.getString("gender");
				this.isFemale = genderTmp.equalsIgnoreCase("female");				
			}// end if: check if parameter exists			
											
			// Get verified or not 
			if (parsedValues.has("verified")){			
				this.isVerified = parsedValues.getBoolean("verified");
			}// end if: check if parameter exists	
			
			// Get e-mail
			if (parsedValues.has("email")){			
				this.email = parsedValues.getString("email");
			}// end if: check if parameter exists
			
		} catch (JSONException e) {
			Log.e(TAG_PACKAGE_CLASS,"User response info could NOT be parsed");
			e.printStackTrace();
		}// end catch
		
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
	public FacebookUser (){
		
	}// end constructor
	
	/** 
	 * Gets the facebook ID of a user
	 * @return unique number representing a user. If it could not be 
	 * found, it returns -1
	 */
	public int getUserId() {
		return userId;
	}// end getUserId

	/** 
	 * Gets the first name of a user
	 * @return first name if its exists, empty string otherwise
	 */
	public String getFirstName() {
		return firstName;
	}// end getFirstName

	/** 
	 * Gets the middle name of a user
	 * @return Middle name if its exists, empty string otherwise
	 */
	public String getMiddleName() {
		return middleName;
	}// end getMiddleName

	/** 
	 * Gets the last name of a user
	 * @return Last name if its exists, empty string otherwise
	 */
	public String getLastName() {
		return lastName;
	}// end getLastName

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
	 * Gets the email of a user
	 * @return the email if found, empty string otherwise
	 */
	public String getEmail() {		
		return email;
	}// end getEmail
	
	/**
	 * Gets the full name of a user (FirstName MiddleName LastName)
	 * @return in case any name is not found, it will be skipped
	 */
	public String getFullName(){
		return this.firstName + " " + this.middleName + " " +this.lastName;
	}// end getFullName	

	/**
	 * Checks if a user is trust worthy or not wrt Facebook.
	 * A user is defined to be trustworthy if he/she enters
	 * any of the following to facebook: 
	 * <ul>
	 * 		<li>Registers for mobile</li>
	 * 		<li>Confirms account using SMS</li>
	 * 		<li>Enters valid credit card</li>
	 * </ul>
	 * @return
	 * <ul>
	 * 		<li>True: user is verified by any of the previous methods</li>
	 * 		<li>False: otherwise</li>
	 * </ul>
	 */
	public boolean isTrustWorthyUser(){
		return isVerified;
	}// end isTrustWorthyUser
	
		
	@Override
	public String toString(){
		String result="";
		
		result += "Name: ("+this.firstName+")("+this.middleName+")("+this.lastName+")\n";
		result += "Gender: ";
		if (this.isFemale)
			result += "female";
		else
			result += "male";
		
		result += "\n";
		
		result += "Email: " + this.email + "\n";
		
		result += "Trust worthy ?: " + this.isVerified + "\n";
		
		result += "User ID: " + this.userId + "\n\n";
		
		return result;
	}// end toString
	
	
	
	
	
	
}// end class
