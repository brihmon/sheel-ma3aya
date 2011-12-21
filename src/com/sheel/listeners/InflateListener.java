package com.sheel.listeners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.view.ViewStub.OnInflateListener;
import android.widget.Button;
import android.widget.TextView;

import com.sheel.app.R;
import com.sheel.datastructures.FacebookUser;
import com.sheel.datastructures.OfferDisplay2;
import com.sheel.datastructures.enums.OwnerFacebookStatus;
import com.sheel.utils.GuiUtils;
import com.sheel.utils.HTTPManager;
import com.sheel.utils.SheelMaayaaConstants;

import static com.sheel.utils.SheelMaayaaConstants.HTTP_CONFIRM_OFFER;;

/**
 * InflateListener is the listener used when the Stub is inflated
 * 
 * It is used to save the position of the buttons inside their tags
 * 
 * @author Hossam_Amer
 * 		    Passant El.Agroudy (passant.elagroudy@gmail.com)	
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
	 * Activity to launch the operations on
	 */
	private Activity mActivity;
	
	/**
	 * Current user logged In
	 */
	private FacebookUser mUser;
	
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
	public InflateListener(int position, Context mContext, OfferDisplay2 offerDisplay,
			Activity activity, FacebookUser mUser)
	{
		mPos = position;

		this.offerDisplay = offerDisplay;

		this.mContext = mContext;
		
		this.mActivity = activity;
		
		this.mUser = mUser;
		
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
	 * Click Action listener for the buttons inside the view stub
	 * @author Hossam_Amer
	 */
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.details_button_call:
			
			Log.e("Call button", this.offerDisplay.getUser().mobileNumber);
			call(this.offerDisplay.getUser().mobileNumber);
			break;
			
		case R.id.details_button_sendSms:
			
			Log.e("SMS button", this.offerDisplay.getUser().mobileNumber);
			sendSMS(getSMSMessage(), this.offerDisplay.getUser().mobileNumber);
			break;
			
		case R.id.details_button_confirm:
			Log.e("Confirm button", "Hello");
			confirmOffer();
			break;
	
		default:
			break;
		}
		
		
	}
	
	/**
	 * Launches the activity of sending SMS
	 * @param sms_content The template form of the SMS
	 * @param number The destination number the SMS will be sent to
	 * @author Hossam_Amer
	 */
	private void sendSMS(String sms_content, String number)
	{
//		Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null));
//		sendIntent.putExtra("sms_body", sms_content);
//		mActivity.startActivity(sendIntent);
		Log.e("SMS button", number);
		Uri uri = Uri.parse("smsto:"+ number);  
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);  
		intent.putExtra("sms_body", sms_content); 
		mActivity.startActivity(intent);
	}
	
	/**
	 * Starts the activity for calling
	 * @param number The phone number the user will contact
	 * @author Hossam_Amer
	 */
	private void call(String number) {
		
		Intent callIntent = new Intent(Intent.ACTION_DIAL);
		callIntent.setData(Uri.parse("tel: " + number));
		mActivity.startActivity(callIntent);
	}
	
	private void confirmOffer() {
		
//		String path = "/insertconfirmation/" + mUser.getUserId()+"/"+this.offerDisplay.getOffer().id;
		String path = "/insertconfirmation/" + 673780564+"/"+this.offerDisplay.getOffer().id;
		Log.e("Confirm offer path: ", path);
		HTTPManager.startHttpService(path, HTTP_CONFIRM_OFFER, mContext);
		
		
	}
	
	/**
	 * Gets the SMS template form depending on the offer status either less weight or extra weight
	 * @return the SMS template form
	 * @author Hossam_Amer
	 */

	/**XXXXXXNeed to check how to get the name
	 *XXXXXhashas
	 */
	private String getSMSMessage() {
		
		String sms_content = "";
		String sms_content1 = "";
		String sms_content2 = "";
		String sms_content3 = "";
		String sms_content4 = "";
		String sms_content5 = "";
		String sms_content6 = "";
		
		/**XXXXXXNeed to check how to get the name
		 *XXXXXhashas
		 */
		
	if(this.offerDisplay.getOffer().userStatus == 0)
	{
		sms_content1 = "Hello " + this.offerDisplay.getDisplayName() +",";
		sms_content2 = "I have seen your offer on Sheel M3aaya app that you " +
				"have an extra space ("+ this.offerDisplay.getOffer().noOfKilograms +  "Kilograms) in your bags." +
				" So, I would like to inform you that " +
				"I am interested in putting some of my stuff in your bags.";
		sms_content3 = "Please contact me at this number if your space is still available.";
		sms_content6 = "Best Regards,\n" + mUser.getFirstName() ;
		
		
	}	
	
	else
	{
		
		sms_content1 = "Hello "+ this.offerDisplay.getDisplayName() +",";
		sms_content2 = "I have seen your request on Sheel M3aaya app that you " +
				"need an extra space ("+ this.offerDisplay.getOffer().noOfKilograms + " Kilograms) in your bags." +
				" So, I would like to inform you that " +
				"I am interested in offering you some of my space in my bags.";
		sms_content3 = "Please contact me at this number if you are still intrested.";
		sms_content6 = "Best Regards,\n" + mUser.getFirstName();
	}
	
	sms_content4 = "Thanks in advance :)";
	sms_content5 = "Wish you a nice flight.";
	sms_content = sms_content1 
				+ "\n" + "\n" + sms_content2
				+ "\n" + "\n" + sms_content3
				+ "\n" + "\n" + sms_content4
				+ "\n" + "\n" + sms_content5
				+ "\n" + "\n" + sms_content6;
	
		return sms_content;
	}

}
