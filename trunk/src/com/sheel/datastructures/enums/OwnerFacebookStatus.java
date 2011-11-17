package com.sheel.datastructures.enums;

/**
 * Enumeration representing possible relationships
 * between app user and an offer owner
 * 
 * @author passant
 *
 */
public enum OwnerFacebookStatus{
	/**
	 * App user and offer owner are friends
	 */
	FRIEND,
	/**
	 * Offer owner is a friend of friend for the 
	 * app user (they have mutual friends but are
	 * not directly related)
	 */
	FRIEND_OF_FRIEND,
	/**
	 * App user and offer owner are not directly
	 * friends or related through friends but have 
	 * common networks
	 */
	COMMON_NETWORKS,
	/**
	 * App user and offer owner are not related in 
	 * any possible way
	 */
	UNRELATED
}// end enum