package com.sheel.datastructures;

import org.json.JSONObject;

/**
 * Confirmation class to map the JSONObject into a normal confirmation as in the database.
 * @author Hossam_Amer
 *
 */

public class Confirmation {

	
	/**
	 * Constant used for tracing purposes "class name (package name)"
	 */
	private final String TAG = Confirmation.class.getName();
	
	
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
	public final static String half_confirmed = "half-confirmed";
	public final static String not_confirmed = "new";
	
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
		try {
			
			JSONObject user1JSON = confirmationJSON.getJSONObject("user1");
			JSONObject user2JSON = confirmationJSON.getJSONObject("user2");
			JSONObject offerJSON = confirmationJSON.getJSONObject("offer");
			JSONObject flightJSON = confirmationJSON.getJSONObject("flight");
			
			//Get the statuses of transaction of confirmation
			boolean statusTransactionUser1 = confirmationJSON.getBoolean("statusTransactionUser1");
			boolean statusTransactionUser2 = confirmationJSON.getBoolean("statusTransactionUser2");
			
			// Create User1
			
			String ownerId = user1JSON.getString("facebookAccount");
			String firstName = user1JSON.getString("firstName");
			String middleName = user1JSON.getString("middleName");
			String lastName = user1JSON.getString("lastName");
			String email = user1JSON.getString("email");
			String mobile = user1JSON.getString("mobileNumber");
			String gender = user1JSON.getString("gender");
			String nationality = user1JSON.getString("nationality");
			
			User user1 = new User(ownerId, firstName, middleName, lastName, "", "", email, mobile, gender, nationality);
			
			// Create User2	
			
			ownerId = user2JSON.getString("facebookAccount");
			firstName = user2JSON.getString("firstName");
			middleName = user2JSON.getString("middleName");
			lastName = user2JSON.getString("lastName");
			email = user2JSON.getString("email");
			mobile = user2JSON.getString("mobileNumber");
			gender = user2JSON.getString("gender");
			nationality = user2JSON.getString("nationality");
			
			User user2 = new User(ownerId, firstName, middleName, lastName, "", "", email, mobile, gender, nationality);
			
			//Create offer
			
			Long offerId = offerJSON.getLong("id");
			String offerstatus = offerJSON.getString("offerStatus");
			int userstatus = offerJSON.getInt("userStatus");
			int kgs = offerJSON.getInt("noOfKilograms");
			int price = offerJSON.getInt("pricePerKilogram");
			
			Offer offer = new Offer(offerId, kgs, price, userstatus, offerstatus);
			
			//Create flight			    
			String flightNumber = flightJSON.getString("flightNumber");
			String source = flightJSON.getString("source");
			String destination = flightJSON.getString("destination");
			String departureDate = flightJSON.getString("departureDate");
				
			Flight flight = new Flight(flightNumber, source, destination, departureDate);
			
				
			return new Confirmation(user1, user2, flight, offer, statusTransactionUser1, statusTransactionUser2);

			
		} catch (Exception e) {
			// TODO: handle exception
			
			return null;
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
