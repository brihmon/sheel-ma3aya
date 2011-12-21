package com.sheel.datastructures;

import org.json.JSONObject;

public class Confirmation {

	
	/**
	 * Confirmation Statuses
	 */
	
	public final static String alreadyConfirmed = "alreadyConfirmed";
	public final static String notFromOfferOwner = "notFromOfferOwner";
	public final static String notSameUser = "notSameUser";
	public final static String confirmedByAnotherPerson = "confirmedByAnotherPerson";
	public final static String half_confirmed = "half-confirmed";
	public final static String not_confirmed = "new";
	
	
	/**
	 * Used for mapping the confirmation from JSONObject into a normal confirmation
	 * 
	 * @param
	 * 		offerJSON JSON Object sent to be converted to a normal offer
	 * @return OfferDisplay2 Object from the JSONObject
	 * @author Hossam_Amer
	 */
	
	public static OfferDisplay2 mapConfirmation(JSONObject confirmationJSON)
	{
		try {
			
			JSONObject userJSON = confirmationJSON.getJSONObject("user");
			JSONObject flightJSON = confirmationJSON.getJSONObject("flight");
			
			String ownerId = userJSON.getString("facebookAccount");
			String firstName = userJSON.getString("firstName");
			String middleName = userJSON.getString("middleName");
			String lastName = userJSON.getString("lastName");
			String email = userJSON.getString("email");
			String mobile = userJSON.getString("mobileNumber");
			String gender = userJSON.getString("gender");
			String nationality = userJSON.getString("nationality");
			
			User user = new User(ownerId, firstName, middleName, lastName, "", "", email, mobile, gender, nationality);
			
			Long offerId = confirmationJSON.getLong("id");
			String offerstatus = confirmationJSON.getString("offerStatus");
			int userstatus = confirmationJSON.getInt("userStatus");
			int kgs = confirmationJSON.getInt("noOfKilograms");
			int price = confirmationJSON.getInt("pricePerKilogram");
			
			Offer offer = new Offer(offerId, kgs, price, userstatus, offerstatus);
			    
			String flightNumber = flightJSON.getString("flightNumber");
			String source = flightJSON.getString("source");
			String destination = flightJSON.getString("destination");
			String departureDate = flightJSON.getString("departureDate");
				
			Flight flight = new Flight(flightNumber, source, destination, departureDate);
			
				
			return new OfferDisplay2(user, flight, offer);

			
		} catch (Exception e) {
			// TODO: handle exception
			
			return null;
		}
	}

}
