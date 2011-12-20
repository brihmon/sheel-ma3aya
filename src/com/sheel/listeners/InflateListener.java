package com.sheel.listeners;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.ViewStub.OnInflateListener;
import android.widget.Button;
import android.widget.TextView;

import com.sheel.app.R;
import com.sheel.datastructures.Flight;
import com.sheel.datastructures.OfferDisplay2;
import com.sheel.datastructures.enums.OwnerFacebookStatus;
import com.sheel.utils.GuiUtils;
import com.sheel.utils.HTTPManager;
import com.sheel.utils.SheelMaayaaConstants;



/**
 * InflateListener is the listener used when the Stub is inflated
 * 
 * It is used to save the position of the buttons inside their tags
 * 
 * @author Hossam_Amer
 *
 */

public class InflateListener implements OnInflateListener, OnClickListener {
	
	private static final String TAG = InflateListener.class.getName();

	
	/**
	 *  Position Clicked
	 */
	int mPos;
	/**
	 * Wrapper representing data of an offer and other data that should
	 * be displayed in the view stub
	 */
	OfferDisplay2 offerDisplay;
	
	/**
	 * Current context of the Activity
	 */
	Context mContext;
	
	/**
	 * Number of buttons
	 */
	int numButtons = 3;
	
	/**
	 * Array of Button IDs
	 */
	private int [] buttonIDs = new int [numButtons];
	
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
	public InflateListener(int position, Context mContext, OfferDisplay2 offerDisplay)
	{
		mPos = position;

		this.offerDisplay = offerDisplay;

		this.mContext = mContext;
		
		// <=== List your buttons here ===>
		buttonIDs[0] = R.id.details_button_call; 
		buttonIDs[1] = R.id.details_button_confirm;
		buttonIDs[2] = R.id.details_button_sendSms;

	}

	/**
	 * 
	 */
	public void onInflate(ViewStub stub, View inflated) {
			
		/**
		 * Initialize the summary (nationality + gender + facebook extra info)
		 */
		TextView details_summary = (TextView)inflated.findViewById(R.id.sheel_details_textView_summary);
		String detailsSummary = offerDisplay.getUser().nationality ;		
	
		if (offerDisplay.getOwnerFacebookRelationWithUser() == OwnerFacebookStatus.FRIEND) {
			detailsSummary += " Friend";
		}// end if: offer owner is the user's friend
		else if (offerDisplay.getOwnerFacebookRelationWithUser() == OwnerFacebookStatus.FRIEND_OF_FRIEND) {
			detailsSummary += " Friend of friend";
		}// end if: offer owner is the user's friend of friend
		else if (offerDisplay.getOwnerFacebookRelationWithUser() == OwnerFacebookStatus.UNRELATED) {
			detailsSummary += " Stranger";
		}// end if: offer owner is a stranger
		details_summary.setText(detailsSummary);
		
		if (offerDisplay.getUser().gender.equals(SheelMaayaaConstants.GENDER_FEMALE)) {
			GuiUtils.setIconForATextField(mContext, inflated, details_summary, R.drawable.sheel_gender_female, 3);
		}// end if : offer owner is a female
		else {
			GuiUtils.setIconForATextField(mContext, inflated, details_summary, R.drawable.sheel_gender_male, 3);
		}// end if:offer owner is a male or unknown -> show icon
		
		
		/**
		 * Initialize email
		 */
		TextView details_email = (TextView)inflated.findViewById(R.id.sheel_details_textView_email);
		details_email.setText(offerDisplay.getUser().email);
		GuiUtils.setIconForATextField(mContext, inflated, details_email, R.drawable.sheel_result_email, 3);
		
		/**
		 * Initialize telephone
		 */
		TextView details_telephone = (TextView)inflated.findViewById(R.id.sheel_details_textView_telephone);
		details_telephone.setText(offerDisplay.getUser().mobileNumber);
		GuiUtils.setIconForATextField(mContext, inflated, details_telephone, R.drawable.sheel_result_phone, 3);
			
		/**
		 * Initialize flight
		 */
		TextView details_flight = (TextView)inflated.findViewById(R.id.sheel_details_textView_flight);
		details_flight.setText(offerDisplay.getFlight().displayFlight());
		GuiUtils.setIconForATextField(mContext, inflated, details_flight, R.drawable.sheel_result_flight, 0);
		
		
		/**
		 * Initialize the buttons
		 */
		for(int i=0; i<buttonIDs.length;i++)
		{
			Button btn = (Button) inflated.findViewById(buttonIDs[i]);
			btn.setTag(mPos);
			Log.e(TAG, "Button: " + btn.getId());
			
			btn.setOnClickListener(this);
			
			Log.e(TAG, "Button Listener done: " + btn.getId());
		}
				
	}// end onInflate

	/**
	 * 
	 */
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.details_button_call:
			
			Log.e("Call button", "Hello");
			
			
			break;
		case R.id.details_button_confirm:
			
			Log.e("Confirm button", "Hello");
		break;
		case R.id.details_button_sendSms:
			
			Log.e("SMS button", "Hello");
			
			break;
		default:
			break;
		}
		
		
	}
	
	private void sendSMS(String sms_content, String number)
	{
		Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null));
		sendIntent.putExtra("sms_body", sms_content);
		mContext.startActivity(sendIntent);
	}
	
	private void call(String number) {
		
		Intent callIntent = new Intent(Intent.ACTION_DIAL);
		callIntent.setData(Uri.parse("tel: " + number));
		mContext.startActivity(callIntent);
	}
	
	private void confirmOffer() {
		
		HTTPManager.startHttpService("", "", mContext);
		
	}
 

}
