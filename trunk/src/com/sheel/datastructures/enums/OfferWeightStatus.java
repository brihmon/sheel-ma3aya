package com.sheel.datastructures.enums;

/**
 * Enumeration representing possible weight statuses of 
 * luggage in an offer
 * 
 * @author passant
 *
 */
public enum OfferWeightStatus{
	/**
	 * Offer owner has less weight, i.e. searching for people
	 * to give him/her luggage
	 */
	LESS,
	/**
	 * Offer owner has more weight, i.e. searching for people
	 * to take from him/her luggage
	 */
	MORE,
	/**
	 * Initial status of an offer before being defined
	 */
	UNDEFINED
}// end enum