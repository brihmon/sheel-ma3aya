/**
 * 
 */
package com.sheel.listeners;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.sheel.app.R;
import com.sheel.datastructures.Confirmation;
import com.sheel.datastructures.FacebookUser;
import com.sheel.datastructures.OfferDisplay2;
import com.sheel.utils.DemoPopupWindow;
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
		textView.setVisibility(View.GONE);
		/**
		 * Example: assume you want to change the visibility
		 */
	}// end renderFirstTextView
	
	/**
	 * Called when second text view is rendered
	 * Overrides the super class renderSecondTextView Method
	 * @author Hossam_Amer
	 */
	@Override
	public void renderSecondTextView(TextView textView,
			OfferDisplay2 offer, View inflated) {
			
		try
		{
		// To check if the offer is half confirmed by an offer owner
		// of to check of the offer is confirmed and I am that offer owner
		// or to check if the offer is not confirmed and I am the offer owner
		if (offer.getOffer().offerStatus.equals(Confirmation.half_confirmed_offerOwner) || 
				(offer.getOffer().offerStatus.equals(Confirmation.not_confirmed) && mUser.getUserId().equals(offer.getUser().getFacebookId())))
			textView.setVisibility(View.GONE);
			
		else if((offer.getOffer().offerStatus.equals(Confirmation.confirmed)))
		{
			// If I am the offer owner, get the other
			if(mUser.getUserId().equals(offer.getUser().getFacebookId()))
				textView.setText(offerDisplay.userOther.email);
			// else put the offer owner
			else 
				textView.setText(offerDisplay.getUser().email);
			
			if (inflated != null) {
				this.swypeCatsGuiUtils.setIconForATextField(mContext, inflated, textView,
						R.drawable.sheel_result_email, 3);
			}// end if
		}
		
		else if(offer.getOffer().offerStatus.equals(Confirmation.half_confirmed_other))
		{
			textView.setText(offerDisplay.userOther.email);
			if (inflated != null) {
				this.swypeCatsGuiUtils.setIconForATextField(mContext, inflated, textView,
						R.drawable.sheel_result_email, 3);
			}// end if
		}
		}
		catch (Exception e) {
			textView.setText("No e-mail");
		}
	}
	
	/**
	 * Called when the third text view is rendered
	 * Overrides the super class renderThirdTextView Method
	 * @author Hossam_Amer
	 */
	@Override
	public void renderThirdTextView(TextView textView,
			OfferDisplay2 offer, View inflated) {
			
		try
		{
		// To check if the offer is half confirmed by an offer owner
		// of to check of the offer is confirmed and I am that offer owner
		// or to check if the offer is not confirmed and I am the offer owner
		if (offer.getOffer().offerStatus.equals(Confirmation.half_confirmed_offerOwner) || 
				(offer.getOffer().offerStatus.equals(Confirmation.not_confirmed) && mUser.getUserId().equals(offer.getUser().getFacebookId())))
			textView.setVisibility(View.GONE);
			
		else if((offer.getOffer().offerStatus.equals(Confirmation.confirmed)))
		{
			// If I am the offer owner, get the other
			if(mUser.getUserId().equals(offer.getUser().getFacebookId()))
				textView.setText(offerDisplay.userOther.mobileNumber);
			// else put the offer owner
			else 
				textView.setText(offerDisplay.getUser().mobileNumber);
			
			if (inflated != null) {
				this.swypeCatsGuiUtils.setIconForATextField(mContext, inflated, textView,
						R.drawable.sheel_result_email, 3);
			}// end if
		}
		
		else if(offer.getOffer().offerStatus.equals(Confirmation.half_confirmed_other))
		{
			textView.setText(offerDisplay.userOther.mobileNumber);
			if (inflated != null) {
				this.swypeCatsGuiUtils.setIconForATextField(mContext, inflated, textView,
						R.drawable.sheel_result_email, 3);
			}// end if
		}
		}
		catch (Exception e) {
			textView.setText("No email");
		}
	}
	
	
	
	/**
	 * Called on clicking the fourth button form the left.
	 * By default this button performs call functions for
	 * offer owner. To change such behavior, override the
	 * method in child class
	 * 
	 * @param v
	 * 		Clicked button
	 * @author 
	 *		Mohsen
	 */
	@Override
	public void onClick_options(View v) {
		Log.e("Options button", "For the quick action menu");
		//DemoPopupWindow.enabledButtons = 3;
		DemoPopupWindow.enabledButtons = 0;
		
		if (offerDisplay.getOffer().offerStatus.equals(Confirmation.half_confirmed_offerOwner)){
			DemoPopupWindow.enabledButtons = DemoPopupWindow.enabledButtons| BUTTON_DEACTIVATE | BUTTON_EDIT;
		}
		else if (offerDisplay.getOffer().offerStatus.equals(Confirmation.half_confirmed_other)){
			DemoPopupWindow.enabledButtons = DemoPopupWindow.enabledButtons| BUTTON_CONFIRM | BUTTON_CALL | BUTTON_SMS;
		}else if (offerDisplay.getOffer().offerStatus.equals(Confirmation.not_confirmed)){
			DemoPopupWindow.enabledButtons = DemoPopupWindow.enabledButtons| BUTTON_CONFIRM | BUTTON_DEACTIVATE | BUTTON_EDIT;
		}
		
		DemoPopupWindow dw = new DemoPopupWindow(v,this.offerDisplay,this.mActivity, this);
		
		dw.showLikeQuickAction();

	}// end onClick_options
	

}// end class
