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
//		textView.setText("Overriding is successful");
		
	//	textView.Visit
		/**
		 * Example: assume you want to change the visibility
		 */
	}// end renderFirstTextView
	
	/**
	 * Called when (Edit Offer) button is clicked
	 */
	
	
	
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
