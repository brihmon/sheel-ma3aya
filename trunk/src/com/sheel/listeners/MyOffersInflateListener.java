/**
 * 
 */
package com.sheel.listeners;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.sheel.datastructures.FacebookUser;
import com.sheel.datastructures.OfferDisplay2;

/**
 * Used when the Stub in the (My Offers) view is inflated. 
 * It provides customized rendering features needed for 
 * such view over the (Search Results) view stub (accordian)
 * 
 * @author 
 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
 * @author
 * 		Hossam_Amer
 *
 */
public class MyOffersInflateListener extends InflateListener{

	/**
	 * 
	 * @param position
	 * 		Clicked position
	 * @param mContext
	 * 
	 * @param offerDisplay
	 * 		Data wrapper representing offer, flight and user used
	 * 		for displaying data in the stub
	 * 
	 * @author
	 * 		Hossam Amer 
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 *  
	 */
	public MyOffersInflateListener(int position, Context mContext,
			OfferDisplay2 offerDisplay, Activity activity, FacebookUser mUser) {
		super(position, mContext, offerDisplay, activity, mUser);	
	}// end MyOffersInflateListener
	

	@Override
	public void onInflate(ViewStub stub, View inflated) {
		super.onInflate(stub, inflated); // CRITICAL: must be there
		
		/**
		 * Example: Assume you want to hide call and send sms buttons
		 */
		setVisibilityButtonsVisibility(inflated, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
		
		/**
		 * If you don't need to change the visibility, you do not need to 
		 * override that method. Just override the render methods needed
		 */
	}// end onInflate
	
	@Override
	/* (non-Javadoc)
	 * @see com.sheel.listeners.InflateListener#renderFirstTextView(android.widget.TextView, com.sheel.datastructures.OfferDisplay2, android.view.View)
	 */
	public void renderFirstTextView(TextView textView,
			OfferDisplay2 offerDisplay, View inflated) {
		/**
		 * Example: assume you want to change the name
		 */
		textView.setText("Overriding is successful");
		
		/**
		 * Example: assume you want to change the visibility
		 */
	}// end renderFirstTextView

}// end class
