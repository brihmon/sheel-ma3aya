package com.sheel.datastructures;

import org.json.JSONObject;

import android.util.Log;

/**
 * Confirmation class to map the JSONObject into a normal confirmation as in the database.
 * @author Hossam_Amer
 *
 */

public class Confirmation {

	
	/**
	 * Constant used for tracing purposes "class name (package name)"
	 */
	private final static String TAG = Confirmation.class.getName();
	
	
	private User user1;
	private User user2;
	private Flight flight;
	private Offer offer;
	
	/**
	 * Indicates User confirmed first exchange of luggage
	 */
    private boolean statusTransactionUser1; 
    /**
	 * Indicates User confirmed first exchange of luggage
	 */
    private boolean statusTransactionUser2;

	/**
	 * Confirmation Statuses
	 */
	public final static String alreadyConfirmed = "alreadyConfirmed";
	public final static String confirmedByAnotherPerson = "confirmedByAnotherPerson";
	public final static String half_confirmed_offerOwner = "half_confirmed_1";
	public final static String half_confirmed_other = "half_confirmed_2";
	public final static String not_confirmed = "new";
	public final static String confirmed = "confirmed";
	
	 public Confirmation(User user1, User user2, Flight flight, Offer offer, 
			 boolean statusTransactionUser1, boolean statusTransactionUser2) {

		this.setUser1(user1);
		this.setUser2(user2);
		this.setFlight(flight);
		this.setOffer(offer);
		this.setStatusTransactionUser1(statusTransactionUser1);
		this.setStatusTransactionUser2(statusTransactionUser2);
		
	}
	
	
	/**
	 * Used for mapping the confirmation from JSONObject into a normal confirmation
	 * 
	 * @param
	 * 		offerJSON JSON Object sent to be converted to a normal offer
	 * @return OfferDisplay2 Object from the JSONObject
	 * @author Hossam_Amer
	 */
	
	public static Confirmation mapConfirmation(JSONObject confirmationJSON)
	{
		String ownerId;
		String firstName;
		String middleName;
		String lastName;
		String email;
		String mobile;
		String gender;
		String nationality;
		
		User user1 = new User("13", "hashas");
		User user2 = new User("13", "hashas");
		
		Long offerId;
		String offerstatus;
		int userstatus;
		int kgs;
		int price;
		
		String flightNumber;
		String source;
		String destination;
		String departureDate;
	
		Flight flight = new Flight("", "", "", "");
		
		Offer offer = new Offer(12, 12, 12, "12");
		
		JSONObject user1JSON = null;
		JSONObject user2JSON = null;
		JSONObject offerJSON = null;
		JSONObject flightJSON = null;

		try {
					
				try
				{
					user1JSON = confirmationJSON.getJSONObject("user1");
				}
				catch (Exception e) {
					// TODO: handle exception
					user1JSON = null;
					Log.e("hashas mapConfirmation1",  "mapConfirmation catch");
				}
				try
				{
					user2JSON = confirmationJSON.getJSONObject("user2");
				}
				catch (Exception e) {
					// TODO: handle exception
					user2JSON = null;
					Log.e("hashas mapConfirmation2",  "mapConfirmation catch");
				}
		
			offerJSON = confirmationJSON.getJSONObject("offer");
			flightJSON = offerJSON.getJSONObject("flight");
		
			//Get the statuses of transaction of confirmation
			boolean statusTransactionUser1 = confirmationJSON.getBoolean("statusTransactionUser1");
			boolean statusTransactionUser2 = confirmationJSON.getBoolean("statusTransactionUser2");
						
			// Create User1
		if(user1JSON != null)
		{
			ownerId = user1JSON.getString("facebookAccount");
			firstName = user1JSON.getString("firstName");
			middleName = user1JSON.getString("middleName");
			lastName = user1JSON.getString("lastName");
			email = user1JSON.getString("email");
			mobile = user1JSON.getString("mobileNumber");
			gender = user1JSON.getString("gender");
			nationality = user1JSON.getString("nationality");
			
			user1 = new User(ownerId, firstName, middleName, lastName, "", "", email, mobile, gender, nationality);
			
			Log.e(TAG, user1 + "");
		}	
			
			// Create User2	
		if(user2JSON != null)
		{
			ownerId = user2JSON.getString("facebookAccount");
			firstName = user2JSON.getString("firstName");
			middleName = user2JSON.getString("middleName");
			lastName = user2JSON.getString("lastName");
			email = user2JSON.getString("email");
			mobile = user2JSON.getString("mobileNumber");
			gender = user2JSON.getString("gender");
			nationality = user2JSON.getString("nationality");
			
			user2 = new User(ownerId, firstName, middleName, lastName, "", "", email, mobile, gender, nationality);
			
			Log.e(TAG, user2 + "");
		}	
			//Create offer
		
			offerId = offerJSON.getLong("id");
			offerstatus = offerJSON.getString("offerStatus");
			userstatus = offerJSON.getInt("userStatus");
			kgs = offerJSON.getInt("noOfKilograms");
			price = offerJSON.getInt("pricePerKilogram");
			
			offer = new Offer(offerId, kgs, price, userstatus, offerstatus);
			
			Log.e(TAG, offer + "");
			
			//Create flight
		
			flightNumber = flightJSON.getString("flightNumber");
			source = flightJSON.getString("source");
			destination = flightJSON.getString("destination");
			departureDate = flightJSON.getString("departureDate");
				
			flight = new Flight(flightNumber, source, destination, departureDate);
			
			Log.e(TAG, flight + "");
			
			Confirmation confirmation = new Confirmation(user1, user2, flight, offer, statusTransactionUser1, statusTransactionUser2);
			Log.e(TAG, confirmation + "");
			return confirmation;

			
		} catch (Exception e) {
			// TODO: handle exception
			
			return new Confirmation(user1, user2, flight, offer, false, false);
		}
	}


	/**
	 * @param statusTransactionUser2 the statusTransactionUser2 to set
	 */
	public void setStatusTransactionUser2(boolean statusTransactionUser2) {
		this.statusTransactionUser2 = statusTransactionUser2;
	}


	/**
	 * @return the statusTransactionUser2
	 */
	public boolean isStatusTransactionUser2() {
		return statusTransactionUser2;
	}


	/**
	 * @param statusTransactionUser1 the statusTransactionUser1 to set
	 */
	public void setStatusTransactionUser1(boolean statusTransactionUser1) {
		this.statusTransactionUser1 = statusTransactionUser1;
	}


	/**
	 * @return the statusTransactionUser1
	 */
	public boolean isStatusTransactionUser1() {
		return statusTransactionUser1;
	}


	/**
	 * @param offer the offer to set
	 */
	public void setOffer(Offer offer) {
		this.offer = offer;
	}


	/**
	 * @return the offer
	 */
	public Offer getOffer() {
		return offer;
	}


	/**
	 * @param flight the flight to set
	 */
	public void setFlight(Flight flight) {
		this.flight = flight;
	}


	/**
	 * @return the flight
	 */
	public Flight getFlight() {
		return flight;
	}


	/**
	 * @param user2 the user2 to set
	 */
	public void setUser2(User user2) {
		this.user2 = user2;
	}


	/**
	 * @return the user2
	 */
	public User getUser2() {
		return user2;
	}


	/**
	 * @param user1 the user1 to set
	 */
	public void setUser1(User user1) {
		this.user1 = user1;
	}


	/**
	 * @return the user1
	 */
	public User getUser1() {
		return user1;
	}


	/**
	 * @return the tAG
	 */
	public String getTAG() {
		return TAG;
	}

}
