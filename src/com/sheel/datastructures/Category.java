/**
 * 
 */
package com.sheel.datastructures;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * This class is used as a wrapper class for displaying
 * a category in horizontal swyping manner.
 *  
 * @author 
 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
 *
 */
public class Category {

	/**
	 * Name of the category that will appear in the indicator
	 */
	private String name="";
	/**
	 * Layout ID of the XML file used for inflating the category
	 */
	private int layoutId = -1;
	/**
	 * List of offersWrappers displayed in the category
	 */
	ArrayList<OfferDisplay2> offersWrappers = new ArrayList<OfferDisplay2>();
	
	/**
	 * Constructor for creating a new category
	 * 
	 * @param name
	 * 		Name of the category that will appear in the indicator
	 * 		on the top of the page
	 * @param layoutId
	 * 		Layout ID of the XML file used for inflating the category.
	 * 		Use <code>R.layout.STH</code>
	 */
	public Category (String name , int layoutId ) {
		this.name = name;
		this.layoutId = layoutId;
	}// end constructor
	
	/**
	 * Constructor for creating a new category
	 * 
	 * @param name
	 * 		Name of the category that will appear in the indicator
	 * 		on the top of the page
	 * @param layoutId
	 * 		Layout ID of the XML file used for inflating the category.
	 * 		Use <code>R.layout.STH</code>
	 * @param offersWrappers
	 * 		Offers to be displayed 		
	 * @return
	 * 		List of components to be displayed. 
	 */
	public Category (String name , int layoutId , ArrayList<OfferDisplay2> offersWrappers) {
		this.name = name;
		this.layoutId = layoutId;
		this.offersWrappers = offersWrappers;
	}// end constructor
	
	/**
	 * Constructor for creating a new category
	 * 
	 * @param name
	 * 		Name of the category that will appear in the indicator
	 * 		on the top of the page
	 * @param layoutId
	 * 		Layout ID of the XML file used for inflating the category.
	 * 		Use <code>R.layout.STH</code>
	 * @param
	 * 		new offer to be added
	 * @return
	 * 		List of components to be displayed. 
	 */
	public Category (String name , int layoutId , OfferDisplay2 offerWrapper) {
		this.name = name;
		this.layoutId = layoutId;
		this.offersWrappers = new ArrayList<OfferDisplay2>();
		this.offersWrappers.add(offerWrapper);
	}// end constructor
	
	/**
	 * Returns name of the category that will appear in the indicator
	 * @return
	 * 		Empty list if not initialized
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public String getName() {
		return this.name;
	}// end getName
	
	/**
	 * Returns layout ID of the XML file used for inflating the category. Use as input for 
	 * {@link Inflater} after checking it is not -1
	 * 
	 * @return
	 * 		-1 if not initialized
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public int getResourceId() {
		return this.layoutId;
	}// end getResourceId
	
	/**
	 * List of components to be displayed
	 * 
	 * @return
	 * 		List of components to be displayed. If empty, it will
	 * 		return empty list
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public ArrayList<OfferDisplay2> getOffersDisplayed (){
		return this.offersWrappers;
	}// end getOffersDisplayed
	
	/**
	 * List of components to be displayed
	 * 
	 * @return
	 * 		List of components to be displayed. 
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public void setOffersDisplayed (ArrayList<OfferDisplay2> newOffersWrappers , boolean isAppend){
		if (isAppend) {
			this.offersWrappers.addAll(newOffersWrappers);
		}// end if : add the offers to the existing ones
		else {
			this.offersWrappers = newOffersWrappers;
		}// end else: refresh the whole list
	}// end getOffersDisplayed
	
	public void addOffer(OfferDisplay2 newOffer) {
		this.offersWrappers.add(newOffer);
	}// end addOffer
	
	public void deleteOffer(OfferDisplay2 offer) {
		this.offersWrappers.remove(offer);
	}
	
}// end class
