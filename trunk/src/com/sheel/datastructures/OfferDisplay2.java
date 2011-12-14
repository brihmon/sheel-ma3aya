package com.sheel.datastructures;


/**
 * This class is a data structure used to collect
 * the merging between information of user and offer
 * and flight displayed in the search results
 * 
 * @author magued
 *
 */
public class OfferDisplay2 extends OfferDisplay{	
	
	User user;
	Flight flight;
	Offer offer;
	
	public OfferDisplay2(User user, Flight flight, Offer offer) {
		
		this.user = user;
		this.flight = flight;
		this.offer = offer;
	}
	
	
	
	
}
