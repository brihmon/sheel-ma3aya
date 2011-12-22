/**
 * 
 */
package com.sheel.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sheel.datastructures.OfferDisplay2;

/**
 * @author 
 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
 *
 */
public class MyOffersResultsListAdapter extends SearchResultsListAdapter {

	/**
	 * Constructor to control the behavior of a "My Offers" ListView
	 * @param c
	 * 		Context of the application having the list view. 
	 * 		It is used to have access to (inflation) properties
	 * @param offersWrappers
	 * 		List of hybrid object used to display data about the 
	 * 		offer and its owner
	 */
	public MyOffersResultsListAdapter(Context c,
			ArrayList<OfferDisplay2> offersWrappers) {
		super(c, offersWrappers);		
	}// end constructor
	
	@Override
	/* (non-Javadoc)
	 * @see com.sheel.adapters.SearchResultsListAdapter#renderFirstTextView(android.widget.TextView, com.sheel.datastructures.OfferDisplay2,  android.view.View)
	 */
	public void renderFirstTextView(TextView textView,OfferDisplay2 offerDisplay,  View listRow) {
		textView.setText("The overriding is successful");
		
		/**
		 * If you want to set it to flight details instead of name
		 */
		//textView.setText(offerDisplay.getFlight().displayFlight());
		
	}// end renderFirstTextView

}// end class
