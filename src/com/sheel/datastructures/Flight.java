package com.sheel.datastructures;

import android.util.Log;

import com.sheel.utils.GuiUtils;

//import java.util.Date;
 
/**
 * @author Mohsen
 * Client side model for flight
 * Doesn't provide relations.
 */
public class Flight
{
		public Long id;
		
	  	public String flightNumber;

	    public String source;
		
	    public String destination;

	    public String departureDate;  //Date only
	    									 
	//    @Filter("flight")
	//    public Query <Offer> offers;
	    
	    public Flight(String flightNumber, String source, String destination, String departureDate) {
	        this.flightNumber = flightNumber;
	        this.source = source;
	        this.destination = destination;
	        this.departureDate = departureDate;
	        
	    }
	    
	    
	    public String getDepartureDate() {
	        return departureDate;
	    }

	    public void setDepartureDateTime(String departureDate) {
	        this.departureDate = departureDate;
	    }

	    public String getDestination() {
	        return destination;
	    }

	    public void setDestination(String destination) {
	        this.destination = destination;
	    }

	    public String getFlightNumber() {
	        return flightNumber;
	    }

	    public void setFlightNumber(String flightNumber) {
	        this.flightNumber = flightNumber;
	    }

	    public String getSource() {
	        return source;
	    }

	    public void setSource(String source) {
	        this.source = source;
	    }

	    
/*    
	    public Query<Offer> getOffers() {
	        return offers;
	    }
//
	    public void setOffers(Query<Offer> offers) {
	        this.offers = offers;
	    }
	    */

	    /**
	     * Used to didsplay flight data in the details view of the offers.
	     * 
	     * @return
	     * 		String with the format: 
	     * 		The format is: Flight (x) From src to destination on departureDate 
	     * 
	     * @author 
	     *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	     *@author
	     *		Hossam_Amer
	     * @param swypeCatsGuiUtils 
	     * 						Object contains all the localized strings.
	     */
	    public String displayFlight(GuiUtils swypeCatsGuiUtils) {
	    	
	    	Log.e("Flight details: ", swypeCatsGuiUtils.getFlightWord()+ "(" + flightNumber + ") " + 
    				swypeCatsGuiUtils.getFlightFrom() + " " + source + " " + swypeCatsGuiUtils.getFlightTo() + " " + destination + " " +  
    				swypeCatsGuiUtils.getFlightOn() + " " + departureDate );
	    	
	    	return swypeCatsGuiUtils.getFlightWord()+ "(" + flightNumber + ") " + 
	    				swypeCatsGuiUtils.getFlightFrom() + " " + source + " " + swypeCatsGuiUtils.getFlightTo() + " " + destination + " " +  
	    				swypeCatsGuiUtils.getFlightOn() + " " + departureDate ;
//	    else
//	    	return swypeCatsGuiUtils.getFlightWord()+ " )" + flightNumber + "( " + 
//			swypeCatsGuiUtils.getFlightFrom() + " " + source + " " + swypeCatsGuiUtils.getFlightTo() + " " + destination + " " +  
//			swypeCatsGuiUtils.getFlightOn() + " " + departureDate ;
	    }// end displayFlight
	    
		@Override
		public String toString() {
			return "Flight {id=" + id + ", flightNumber=" + flightNumber
					+ ", source=" + source + ", destination=" + destination
					+ ", departureDateTime=" + departureDate + "}";
		}

	    
}