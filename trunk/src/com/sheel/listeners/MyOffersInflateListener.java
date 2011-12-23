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
import com.sheel.utils.GuiUtils;

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
			OfferDisplay2 offerDisplay, Activity activity, FacebookUser mUser, GuiUtils swypeCatsGuiUtils) {
		super(position, mContext, offerDisplay, activity, mUser, swypeCatsGuiUtils);	
	}// end MyOffersInflateListener
	

	@Override
	public void onInflate(ViewStub stub, View inflated) {
		super.onInflate(stub, inflated); // CRITICAL: must be there
		
		/**
		 * Example: Assume you want to hide call and send sms buttons
		 */
		
		//setVisibilityButtonsVisibility(inflated, View.VISIBLE, View.GONE, View.GONE);

		
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
//		textView.setText("Overriding is successful");
		
	//	textView.Visit
		/**
		 * Example: assume you want to change the visibility
		 */
	}// end renderFirstTextView
	
	/**
	 * Called when (Edit Offer) button is clicked
	 */
	@Override	
	public void onClick_button1(View v) {		
		/**
		 * Logic will be implemented by ahmad
		 */
		System.out.println("Edit offer button has been clicked");
	}// end onClick_button1
	
	/**
	 * Called when (Delete Offer) button is clicked
	 */
	@Override	
	public void onClick_button2(View v) {
		/**
		 * Logic will be implemented by ahmad
		 */
		System.out.println("Delete offer button has been clicked");
	}// end onClick_button2
	
	

}// end class
